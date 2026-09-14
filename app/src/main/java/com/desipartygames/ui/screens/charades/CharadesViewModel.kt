package com.desipartygames.ui.screens.charades

import androidx.lifecycle.ViewModel
import com.desipartygames.data.content.CharadesBank
import com.desipartygames.data.content.CharadesCategory
import com.desipartygames.data.content.CharadesItem
import com.desipartygames.data.repository.PartyGamesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class CharadesGameState {
    SETUP,
    PASS_TO_ACTOR,
    PERFORMING,
    RESULT
}

data class CharadesTeam(
    val name: String,
    var score: Int = 0,
    val emoji: String = "🎭"
)

data class CharadesUiState(
    val gameState: CharadesGameState = CharadesGameState.SETUP,
    val selectedCategory: CharadesCategory = CharadesBank.allCategories,
    val teams: List<CharadesTeam> = listOf(
        CharadesTeam("Team Sholay", 0, "🔥"),
        CharadesTeam("Team Lagaan", 0, "🏏")
    ),
    val currentTeamIndex: Int = 0,
    val currentPrompt: CharadesItem? = null,
    val timerSeconds: Int = 60,
    val roundNumber: Int = 1,
    val isCheatSheetOpen: Boolean = false,
    val isRulesOpen: Boolean = false,
    val isScoreboardOpen: Boolean = false
)

class CharadesViewModel(private val repository: PartyGamesRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CharadesUiState())
    val uiState: StateFlow<CharadesUiState> = _uiState.asStateFlow()

    fun selectCategory(category: CharadesCategory) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun setTimerSeconds(seconds: Int) {
        _uiState.value = _uiState.value.copy(timerSeconds = seconds)
    }

    fun renameTeam(index: Int, name: String) {
        if (name.isBlank() || index !in _uiState.value.teams.indices) return
        val updatedTeams = _uiState.value.teams.toMutableList()
        updatedTeams[index] = updatedTeams[index].copy(name = name.trim())
        _uiState.value = _uiState.value.copy(teams = updatedTeams)
    }

    fun startTurn() {
        val category = _uiState.value.selectedCategory
        val item = category.items.random()
        _uiState.value = _uiState.value.copy(
            currentPrompt = item,
            gameState = CharadesGameState.PASS_TO_ACTOR
        )
    }

    fun startActing() {
        _uiState.value = _uiState.value.copy(gameState = CharadesGameState.PERFORMING)
    }

    fun recordTurnResult(guessedCorrectly: Boolean) {
        val currentIdx = _uiState.value.currentTeamIndex
        val updatedTeams = _uiState.value.teams.toMutableList()

        if (guessedCorrectly && currentIdx in updatedTeams.indices) {
            updatedTeams[currentIdx] = updatedTeams[currentIdx].copy(
                score = updatedTeams[currentIdx].score + 10
            )
        }

        val nextIdx = (currentIdx + 1) % updatedTeams.size

        _uiState.value = _uiState.value.copy(
            teams = updatedTeams,
            currentTeamIndex = nextIdx,
            gameState = CharadesGameState.SETUP,
            roundNumber = if (nextIdx == 0) _uiState.value.roundNumber + 1 else _uiState.value.roundNumber
        )
    }

    fun toggleCheatSheet() {
        _uiState.value = _uiState.value.copy(isCheatSheetOpen = !_uiState.value.isCheatSheetOpen)
    }

    fun setRulesOpen(open: Boolean) {
        _uiState.value = _uiState.value.copy(isRulesOpen = open)
    }

    fun setScoreboardOpen(open: Boolean) {
        _uiState.value = _uiState.value.copy(isScoreboardOpen = open)
    }
}
