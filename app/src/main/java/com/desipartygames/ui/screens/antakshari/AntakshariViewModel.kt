package com.desipartygames.ui.screens.antakshari

import androidx.lifecycle.ViewModel
import com.desipartygames.data.content.AntakshariBank
import com.desipartygames.data.content.AntakshariLetter
import com.desipartygames.data.repository.PartyGamesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AntakshariTeam(
    val name: String,
    var score: Int = 0,
    val emoji: String = "🎵"
)

data class AntakshariUiState(
    val teams: List<AntakshariTeam> = listOf(
        AntakshariTeam("Deewane", 0, "🔥"),
        AntakshariTeam("Parwane", 0, "⚡")
    ),
    val currentTeamIndex: Int = 0,
    val currentLetter: AntakshariLetter = AntakshariBank.letters.first(),
    val selectedEra: String = "All Eras",
    val turnSeconds: Int = 45,
    val isHintRevealed: Boolean = false,
    val roundNumber: Int = 1,
    val isRulesOpen: Boolean = false,
    val isScoreboardOpen: Boolean = false
)

class AntakshariViewModel(private val repository: PartyGamesRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AntakshariUiState())
    val uiState: StateFlow<AntakshariUiState> = _uiState.asStateFlow()

    init {
        generateRandomLetter()
    }

    fun generateRandomLetter() {
        val letter = AntakshariBank.letters.random()
        _uiState.value = _uiState.value.copy(
            currentLetter = letter,
            isHintRevealed = false
        )
    }

    fun selectLetterByChar(char: String) {
        val matched = AntakshariBank.letters.find {
            it.devanagari.contains(char, ignoreCase = true) || it.roman.equals(char, ignoreCase = true)
        } ?: AntakshariBank.letters.random()

        _uiState.value = _uiState.value.copy(
            currentLetter = matched,
            isHintRevealed = false
        )
    }

    fun toggleHint() {
        _uiState.value = _uiState.value.copy(isHintRevealed = !_uiState.value.isHintRevealed)
    }

    fun selectEra(era: String) {
        _uiState.value = _uiState.value.copy(selectedEra = era)
    }

    fun setTurnSeconds(seconds: Int) {
        _uiState.value = _uiState.value.copy(turnSeconds = seconds)
    }

    fun recordTeamResult(points: Int) {
        val currentIdx = _uiState.value.currentTeamIndex
        val updatedTeams = _uiState.value.teams.toMutableList()
        if (currentIdx in updatedTeams.indices) {
            updatedTeams[currentIdx] = updatedTeams[currentIdx].copy(
                score = updatedTeams[currentIdx].score + points
            )
        }

        val nextTeamIdx = (currentIdx + 1) % updatedTeams.size
        val nextLetter = AntakshariBank.letters.random()

        _uiState.value = _uiState.value.copy(
            teams = updatedTeams,
            currentTeamIndex = nextTeamIdx,
            currentLetter = nextLetter,
            isHintRevealed = false,
            roundNumber = if (nextTeamIdx == 0) _uiState.value.roundNumber + 1 else _uiState.value.roundNumber
        )
    }

    fun addTeam(name: String) {
        if (name.isBlank() || _uiState.value.teams.size >= 4) return
        val updated = _uiState.value.teams.toMutableList().apply {
            add(AntakshariTeam(name.trim(), 0, listOf("🎸", "🎺", "🥁", "✨").random()))
        }
        _uiState.value = _uiState.value.copy(teams = updated)
    }

    fun setRulesOpen(open: Boolean) {
        _uiState.value = _uiState.value.copy(isRulesOpen = open)
    }

    fun setScoreboardOpen(open: Boolean) {
        _uiState.value = _uiState.value.copy(isScoreboardOpen = open)
    }
}
