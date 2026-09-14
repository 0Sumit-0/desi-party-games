package com.desipartygames.ui.screens.truthdare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desipartygames.core.ActiveGroupManager
import com.desipartygames.core.randomDifferentFrom
import com.desipartygames.data.content.TruthDareBank
import com.desipartygames.data.content.TruthDarePrompt
import com.desipartygames.data.local.entity.CustomPromptEntity
import com.desipartygames.data.local.entity.PlayerEntity
import com.desipartygames.data.repository.PartyGamesRepository
import com.desipartygames.ui.components.PlayerScore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlinx.coroutines.flow.combine

enum class TruthDareState {
    SPIN_BOTTLE,
    CHOOSE_TRUTH_OR_DARE,
    SHOW_PROMPT,
    SUMMARY
}

data class TruthDareUiState(
    val gameState: TruthDareState = TruthDareState.SPIN_BOTTLE,
    val selectedCategory: String = "FRIENDS", // "FAMILY" or "FRIENDS"
    val players: List<PlayerEntity> = emptyList(),
    val selectedPlayerIndex: Int = 0,
    val bottleRotationAngle: Float = 0f,
    val isSpinning: Boolean = false,
    val currentPromptType: String = "TRUTH", // "TRUTH" or "DARE"
    val currentPrompt: TruthDarePrompt? = null,
    val scores: List<PlayerScore> = emptyList(),
    val isDareTimerActive: Boolean = false,
    val isRulesOpen: Boolean = false,
    val isScoreboardOpen: Boolean = false,
    val isAddPromptDialogOpen: Boolean = false
)

class TruthDareViewModel(private val repository: PartyGamesRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TruthDareUiState())
    val uiState: StateFlow<TruthDareUiState> = _uiState.asStateFlow()
    private var lastPromptId: String? = null

    init {
        viewModelScope.launch {
            combine(repository.allPlayers, ActiveGroupManager.currentGroup) { all, group ->
                all.filter { it.groupTag == group }
            }.collect { groupPlayers ->
                if (groupPlayers.isNotEmpty() && _uiState.value.players.isEmpty()) {
                    val defaultList = groupPlayers.take(15).ifEmpty {
                        listOf(
                            PlayerEntity(name = "Aarav", avatarEmoji = "🔥"),
                            PlayerEntity(name = "Pooja", avatarEmoji = "🌸"),
                            PlayerEntity(name = "Kabir", avatarEmoji = "😎"),
                            PlayerEntity(name = "Ananya", avatarEmoji = "✨"),
                            PlayerEntity(name = "Rohan", avatarEmoji = "👑")
                        )
                    }
                    _uiState.value = _uiState.value.copy(
                        players = defaultList,
                        scores = defaultList.map { PlayerScore(it.name, 0, it.avatarEmoji) }
                    )
                }
            }
        }
    }

    fun selectCategory(category: String) {
        if (category == "FAMILY" || category == "FRIENDS") {
            _uiState.value = _uiState.value.copy(selectedCategory = category)
        }
    }

    fun spinBottle() {
        if (_uiState.value.players.isEmpty()) return
        val extraSpins = 3 + Random.nextInt(4)
        val targetIndex = Random.nextInt(_uiState.value.players.size)
        val anglePerPlayer = 360f / _uiState.value.players.size
        val targetAngle = extraSpins * 360f + (targetIndex * anglePerPlayer) + (anglePerPlayer / 2)

        _uiState.value = _uiState.value.copy(
            isSpinning = true,
            bottleRotationAngle = _uiState.value.bottleRotationAngle + targetAngle,
            selectedPlayerIndex = targetIndex
        )
    }

    fun onBottleSpinFinished() {
        _uiState.value = _uiState.value.copy(
            isSpinning = false,
            gameState = TruthDareState.CHOOSE_TRUTH_OR_DARE
        )
    }

    fun chooseTruthOrDare(type: String) {
        val category = _uiState.value.selectedCategory
        val matchingPrompts = TruthDareBank.prompts.filter {
            it.type == type && it.category == category
        }.ifEmpty {
            TruthDareBank.prompts.filter { it.type == type }
        }

        val chosen = randomDifferentFrom(matchingPrompts, matchingPrompts.find { it.id == lastPromptId })
        lastPromptId = chosen.id

        _uiState.value = _uiState.value.copy(
            currentPromptType = type,
            currentPrompt = chosen,
            gameState = TruthDareState.SHOW_PROMPT,
            isDareTimerActive = type == "DARE"
        )
    }

    fun completeChallenge(points: Int = 10) {
        val playerIdx = _uiState.value.selectedPlayerIndex
        val updatedScores = _uiState.value.scores.toMutableList()
        if (playerIdx in updatedScores.indices) {
            updatedScores[playerIdx] = updatedScores[playerIdx].copy(
                score = updatedScores[playerIdx].score + points
            )
        }

        _uiState.value = _uiState.value.copy(
            scores = updatedScores,
            gameState = TruthDareState.SPIN_BOTTLE
        )
    }

    fun forfeitChallenge() {
        // 0 points or skip
        _uiState.value = _uiState.value.copy(gameState = TruthDareState.SPIN_BOTTLE)
    }

    fun saveCustomPrompt(type: String, category: String, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.addCustomPrompt(
                CustomPromptEntity(
                    gameType = type,
                    category = category,
                    content = text.trim(),
                    isSpicy = false
                )
            )
        }
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

    fun setAddPromptDialogOpen(open: Boolean) {
        _uiState.value = _uiState.value.copy(isAddPromptDialogOpen = open)
    }
}
