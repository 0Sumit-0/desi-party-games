package com.desipartygames.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desipartygames.core.ActiveGroupManager
import com.desipartygames.core.AppLanguage
import com.desipartygames.data.local.entity.PlayerEntity
import com.desipartygames.data.repository.PartyGamesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val language: AppLanguage = AppLanguage.HINGLISH,
    val selectedGroup: String = "Hostel Gang",
    val availableGroups: List<String> = listOf("Hostel Gang", "Family Adda", "Chai Pe Charcha"),
    val playersInGroup: List<PlayerEntity> = emptyList(),
    val totalMatchesPlayed: Int = 0
)

class HomeViewModel(private val repository: PartyGamesRepository) : ViewModel() {

    private val _language = MutableStateFlow(AppLanguage.HINGLISH)

    val uiState: StateFlow<HomeUiState> = combine(
        _language,
        ActiveGroupManager.currentGroup,
        repository.allPlayers,
        repository.gameHistory
    ) { lang, group, players, history ->
        val groups = players.map { it.groupTag }.distinct().ifEmpty { listOf("Hostel Gang", "Family Adda") }
        val currentGroupPlayers = players.filter { it.groupTag == group }.ifEmpty {
            listOf(
                PlayerEntity(name = "Aarav", groupTag = group, avatarEmoji = "🔥"),
                PlayerEntity(name = "Pooja", groupTag = group, avatarEmoji = "💃"),
                PlayerEntity(name = "Kabir", groupTag = group, avatarEmoji = "😎"),
                PlayerEntity(name = "Ananya", groupTag = group, avatarEmoji = "✨")
            )
        }

        HomeUiState(
            language = lang,
            selectedGroup = group,
            availableGroups = groups,
            playersInGroup = currentGroupPlayers,
            totalMatchesPlayed = history.size
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    init {
        viewModelScope.launch {
            repository.seedDefaultsIfEmpty()
        }
    }

    fun setLanguage(language: AppLanguage) {
        _language.value = language
    }

    fun selectGroup(group: String) {
        ActiveGroupManager.currentGroup.value = group
    }
}
