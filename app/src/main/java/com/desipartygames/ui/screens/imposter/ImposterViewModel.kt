package com.desipartygames.ui.screens.imposter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desipartygames.data.content.ImposterCategory
import com.desipartygames.data.content.ImposterWordsBank
import com.desipartygames.core.randomDifferentFrom
import com.desipartygames.data.local.entity.PlayerEntity
import com.desipartygames.data.repository.PartyGamesRepository
import com.desipartygames.ui.components.PlayerScore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

enum class ImposterGameState {
    SETUP,
    PASS_PHONE,
    DISCUSSION,
    VOTING,
    REVEAL,
    SUMMARY
}

data class ImposterPlayerAssignment(
    val player: PlayerEntity,
    val isImposter: Boolean,
    val secretWord: String,
    val categoryName: String,
    var votesReceived: Int = 0
)

data class ImposterUiState(
    val gameState: ImposterGameState = ImposterGameState.SETUP,
    val selectedCategory: ImposterCategory = ImposterWordsBank.allCategories,
    val imposterCount: Int = 1,
    val discussionSeconds: Int = 120,
    val players: List<PlayerEntity> = emptyList(),
    val currentPassingIndex: Int = 0,
    val assignments: List<ImposterPlayerAssignment> = emptyList(),
    val secretWord: String = "",
    val accusedPlayerIndex: Int? = null,
    val roundResultText: String = "",
    val scores: List<PlayerScore> = emptyList(),
    val roundNumber: Int = 1,
    val isRulesOpen: Boolean = false,
    val isScoreboardOpen: Boolean = false
)

class ImposterViewModel(private val repository: PartyGamesRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ImposterUiState())
    val uiState: StateFlow<ImposterUiState> = _uiState.asStateFlow()
    private var lastWord: String? = null

    init {
        // 1. Launch on Dispatchers.IO to keep the UI thread fast and smooth
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                repository.allPlayers,
                com.desipartygames.core.ActiveGroupManager.currentGroup
            ) { all, currentGroup ->

                val filteredPlayers = all.filter { it.groupTag == currentGroup }

                filteredPlayers.ifEmpty {
                    listOf(
                        PlayerEntity(name = "Aarav", avatarEmoji = "🔥", groupTag = currentGroup),
                        PlayerEntity(name = "Pooja", avatarEmoji = "💃", groupTag = currentGroup),
                        PlayerEntity(name = "Kabir", avatarEmoji = "😎", groupTag = currentGroup)
                    )
                }
            }.collect { activePlayers ->
                // 2. SAFETY CHECK: Only update the player list if the game hasn't started.
                // This prevents scores from resetting to 0 mid-game if the database changes.
                if (_uiState.value.gameState == ImposterGameState.SETUP) {
                    _uiState.value = _uiState.value.copy(
                        players = activePlayers,
                        scores = activePlayers.map {
                            PlayerScore(it.name, 0, it.avatarEmoji)
                        }
                    )
                }
            }
        }
    }
    fun selectCategory(category: ImposterCategory) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun setImposterCount(count: Int) {
        _uiState.value = _uiState.value.copy(imposterCount = count)
    }

    fun setDiscussionSeconds(seconds: Int) {
        _uiState.value = _uiState.value.copy(discussionSeconds = seconds)
    }

    fun addPlayer(name: String) {
        if (name.isBlank()) return
        val updated = _uiState.value.players.toMutableList().apply {
            add(PlayerEntity(name = name.trim(), avatarEmoji = getRandomEmoji()))
        }
        _uiState.value = _uiState.value.copy(
            players = updated,
            scores = updated.map { PlayerScore(it.name, 0, it.avatarEmoji) }
        )
    }

    fun removePlayer(index: Int) {
        if (_uiState.value.players.size <= 3) return
        val updated = _uiState.value.players.toMutableList().apply { removeAt(index) }
        _uiState.value = _uiState.value.copy(
            players = updated,
            scores = updated.map { PlayerScore(it.name, 0, it.avatarEmoji) }
        )
    }

    fun startNewGame() {
        val category = _uiState.value.selectedCategory
        val word = randomDifferentFrom(category.words, lastWord)
        lastWord = word
        val players = _uiState.value.players
        val imposterCount = _uiState.value.imposterCount.coerceAtMost(players.size - 1)

        val imposterIndices = players.indices.shuffled().take(imposterCount).toSet()

        val assignments = players.mapIndexed { index, player ->
            val isImp = index in imposterIndices
            ImposterPlayerAssignment(
                player = player,
                isImposter = isImp,
                secretWord = if (isImp) "IMPOSTER" else word,
                categoryName = category.title
            )
        }

        _uiState.value = _uiState.value.copy(
            gameState = ImposterGameState.PASS_PHONE,
            secretWord = word,
            assignments = assignments,
            currentPassingIndex = 0,
            accusedPlayerIndex = null
        )
    }

    fun nextPassingPlayer() {
        val nextIdx = _uiState.value.currentPassingIndex + 1
        if (nextIdx < _uiState.value.assignments.size) {
            _uiState.value = _uiState.value.copy(currentPassingIndex = nextIdx)
        } else {
            // All players saw their cards, start discussion!
            _uiState.value = _uiState.value.copy(gameState = ImposterGameState.DISCUSSION)
        }
    }

    fun startVotingPhase() {
        _uiState.value = _uiState.value.copy(gameState = ImposterGameState.VOTING)
    }

    fun selectAccusedPlayer(index: Int) {
        _uiState.value = _uiState.value.copy(accusedPlayerIndex = index)
    }

    fun confirmVotingAndReveal() {
        val accusedIdx = _uiState.value.accusedPlayerIndex ?: return
        val assignments = _uiState.value.assignments
        val accused = assignments[accusedIdx]
        val currentScores = _uiState.value.scores.map { it.copy() }

        val resultMsg: String
        if (accused.isImposter) {
            resultMsg = "🎉 Great Detective Work! ${accused.player.name} was indeed the IMPOSTER!\nInnocent players win +100 points!"
            // Award innocent players
            assignments.forEachIndexed { i, a ->
                if (!a.isImposter && i < currentScores.size) {
                    currentScores[i].score += 100
                }
            }
        } else {
            val imposters = assignments.filter { it.isImposter }.joinToString { it.player.name }
            resultMsg = "❌ Oops! ${accused.player.name} was INNOCENT!\nThe real Imposter ($imposters) successfully deceived everyone (+200 pts)!"
            // Award imposters
            assignments.forEachIndexed { i, a ->
                if (a.isImposter && i < currentScores.size) {
                    currentScores[i].score += 200
                }
            }
        }

        viewModelScope.launch {
            repository.recordGame(
                gameName = "Who's the Imposter",
                winnerInfo = if (accused.isImposter) "Innocents Caught Imposter" else "Imposter Fooled Group",
                scoreDetails = "Secret Word: ${_uiState.value.secretWord}"
            )
        }

        _uiState.value = _uiState.value.copy(
            gameState = ImposterGameState.REVEAL,
            roundResultText = resultMsg,
            scores = currentScores
        )
    }

    fun playNextRound() {
        _uiState.value = _uiState.value.copy(
            roundNumber = _uiState.value.roundNumber + 1,
            gameState = ImposterGameState.SETUP

        )
    }

    fun updateScore(index: Int, newScore: Int) {
        val updated = _uiState.value.scores.toMutableList()
        if (index in updated.indices) {
            updated[index] = updated[index].copy(score = newScore)
            _uiState.value = _uiState.value.copy(scores = updated)
        }
    }

    fun setRulesOpen(open: Boolean) {
        _uiState.value = _uiState.value.copy(isRulesOpen = open)
    }

    fun setScoreboardOpen(open: Boolean) {
        _uiState.value = _uiState.value.copy(isScoreboardOpen = open)
    }

    private fun getRandomEmoji(): String {
        val emojis = listOf("🦁", "🐯", "🦊", "👑", "🔥", "✨", "💃", "😎", "🎩", "🚀", "🍿", "☕")
        return emojis.random()
    }
}
