package com.desipartygames.ui.screens.rajamantri

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desipartygames.data.local.entity.PlayerEntity
import com.desipartygames.data.repository.PartyGamesRepository
import com.desipartygames.ui.components.PlayerScore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlin.collections.ifEmpty

enum class RajaRole(val title: String, val titleHindi: String, val emoji: String, val basePoints: Int) {
    RAJA("Raja", "राजा", "👑", 1000),
    MANTRI("Mantri", "मंत्री", "📜", 800),
    CHOR("Chor", "चोर", "🎭", 0), // 0 if caught, 500 if escapes
    SIPAHI("Sipahi", "सिपाही", "🛡️", 500), // 500 if correct, 0 if wrong
    RANI("Rani", "रानी", "👸", 900),
    SENAPATI("Senapati", "सेनापति", "⚔️", 700)
}

enum class RajaGameState {
    SETUP,
    PASS_CHITS,
    RAJA_CALL,
    SIPAHI_GUESS,
    REVEAL,
    SUMMARY
}

data class RajaPlayerChit(
    val player: PlayerEntity,
    val role: RajaRole,
    var isChitOpened: Boolean = false
)

data class RajaMantriUiState(
    val gameState: RajaGameState = RajaGameState.SETUP,
    val players: List<PlayerEntity> = emptyList(),
    val chits: List<RajaPlayerChit> = emptyList(),
    val currentPassingIndex: Int = 0,
    val rajaIndex: Int = -1,
    val mantriIndex: Int = -1,
    val sipahiIndex: Int = -1,
    val chorIndex: Int = -1,
    val suspectedChorIndex: Int? = null,
    val isChorCaught: Boolean = false,
    val scores: List<PlayerScore> = emptyList(),
    val roundNumber: Int = 1,
    val isRulesOpen: Boolean = false,
    val isScoreboardOpen: Boolean = false
)

class RajaMantriViewModel(private val repository: PartyGamesRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RajaMantriUiState())
    val uiState: StateFlow<RajaMantriUiState> = _uiState.asStateFlow()

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
                if (_uiState.value.gameState == RajaGameState.SETUP) {
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
    fun addPlayer(name: String) {
        if (name.isBlank() || _uiState.value.players.size >= 6) return
        val updated = _uiState.value.players.toMutableList().apply {
            add(PlayerEntity(name = name.trim(), avatarEmoji = getRandomEmoji()))
        }
        _uiState.value = _uiState.value.copy(
            players = updated,
            scores = updated.map { PlayerScore(it.name, 0, it.avatarEmoji) }
        )
    }

    fun removePlayer(index: Int) {
        if (_uiState.value.players.size <= 4) return
        val updated = _uiState.value.players.toMutableList().apply { removeAt(index) }
        _uiState.value = _uiState.value.copy(
            players = updated,
            scores = updated.map { PlayerScore(it.name, 0, it.avatarEmoji) }
        )
    }

    fun startChitShuffling() {
        val players = _uiState.value.players
        val rolesPool = when (players.size) {
            4 -> listOf(RajaRole.RAJA, RajaRole.MANTRI, RajaRole.CHOR, RajaRole.SIPAHI)
            5 -> listOf(RajaRole.RAJA, RajaRole.RANI, RajaRole.MANTRI, RajaRole.CHOR, RajaRole.SIPAHI)
            else -> listOf(RajaRole.RAJA, RajaRole.RANI, RajaRole.MANTRI, RajaRole.SENAPATI, RajaRole.CHOR, RajaRole.SIPAHI)
        }.shuffled()

        val chits = players.mapIndexed { index, player ->
            RajaPlayerChit(player = player, role = rolesPool[index])
        }

        val rajaIdx = chits.indexOfFirst { it.role == RajaRole.RAJA }
        val mantriIdx = chits.indexOfFirst { it.role == RajaRole.MANTRI }
        val sipahiIdx = chits.indexOfFirst { it.role == RajaRole.SIPAHI }
        val chorIdx = chits.indexOfFirst { it.role == RajaRole.CHOR }

        _uiState.value = _uiState.value.copy(
            gameState = RajaGameState.PASS_CHITS,
            chits = chits,
            currentPassingIndex = 0,
            rajaIndex = rajaIdx,
            mantriIndex = mantriIdx,
            sipahiIndex = sipahiIdx,
            chorIndex = chorIdx,
            suspectedChorIndex = null,
            isChorCaught = false
        )
    }

    fun nextChitPass() {
        val nextIdx = _uiState.value.currentPassingIndex + 1
        if (nextIdx < _uiState.value.chits.size) {
            _uiState.value = _uiState.value.copy(currentPassingIndex = nextIdx)
        } else {
            // All chits collected, proceed to Raja's Court Declaration!
            _uiState.value = _uiState.value.copy(gameState = RajaGameState.RAJA_CALL)
        }
    }

    fun proceedToSipahiGuess() {
        _uiState.value = _uiState.value.copy(gameState = RajaGameState.SIPAHI_GUESS)
    }

    fun selectSuspectedChor(index: Int) {
        _uiState.value = _uiState.value.copy(suspectedChorIndex = index)
    }

    fun confirmSipahiGuess() {
        val suspectedIdx = _uiState.value.suspectedChorIndex ?: return
        val chorIdx = _uiState.value.chorIndex
        val isCaught = suspectedIdx == chorIdx
        val currentScores = _uiState.value.scores.map { it.copy() }
        val chits = _uiState.value.chits

        // Apply classic Indian scoring
        chits.forEachIndexed { i, chit ->
            if (i < currentScores.size) {
                when (chit.role) {
                    RajaRole.RAJA -> currentScores[i].score += 1000
                    RajaRole.MANTRI -> currentScores[i].score += 800
                    RajaRole.RANI -> currentScores[i].score += 900
                    RajaRole.SENAPATI -> currentScores[i].score += 700
                    RajaRole.SIPAHI -> {
                        if (isCaught) currentScores[i].score += 500
                    }
                    RajaRole.CHOR -> {
                        if (!isCaught) currentScores[i].score += 500
                    }
                }
            }
        }

        viewModelScope.launch {
            repository.recordGame(
                gameName = "Raja Mantri Chor Sipahi",
                winnerInfo = if (isCaught) "Sipahi Caught Chor!" else "Chor Escaped!",
                scoreDetails = "Round ${_uiState.value.roundNumber}"
            )
        }

        _uiState.value = _uiState.value.copy(
            gameState = RajaGameState.REVEAL,
            isChorCaught = isCaught,
            scores = currentScores
        )
    }

    fun nextRound() {
        _uiState.value = _uiState.value.copy(
            roundNumber = _uiState.value.roundNumber + 1,
            gameState = RajaGameState.SETUP
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
        return listOf("👑", "💂", "🕵️", "🥷", "🌸", "🔥", "✨", "🦁", "☕").random()
    }
}
