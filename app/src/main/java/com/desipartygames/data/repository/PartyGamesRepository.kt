package com.desipartygames.data.repository

import com.desipartygames.data.local.dao.PartyGamesDao
import com.desipartygames.data.local.entity.CustomPromptEntity
import com.desipartygames.data.local.entity.GameHistoryEntity
import com.desipartygames.data.local.entity.PlayerEntity
import kotlinx.coroutines.flow.Flow

class PartyGamesRepository(private val dao: PartyGamesDao) {

    val allPlayers: Flow<List<PlayerEntity>> = dao.getAllPlayers()
    val allCustomPrompts: Flow<List<CustomPromptEntity>> = dao.getAllCustomPrompts()
    val gameHistory: Flow<List<GameHistoryEntity>> = dao.getRecentGameHistory()

    fun getPlayersByGroup(groupTag: String): Flow<List<PlayerEntity>> = dao.getPlayersByGroup(groupTag)
    fun getCustomPromptsByType(gameType: String): Flow<List<CustomPromptEntity>> = dao.getCustomPromptsByType(gameType)

    suspend fun addPlayer(player: PlayerEntity) = dao.insertPlayer(player)
    suspend fun addPlayers(players: List<PlayerEntity>) = dao.insertPlayers(players)
    suspend fun removePlayer(player: PlayerEntity) = dao.deletePlayer(player)
    suspend fun removePlayerById(id: Int) = dao.deletePlayerById(id)

    suspend fun addCustomPrompt(prompt: CustomPromptEntity) = dao.insertCustomPrompt(prompt)
    suspend fun removeCustomPrompt(prompt: CustomPromptEntity) = dao.deleteCustomPrompt(prompt)

    suspend fun recordGame(gameName: String, winnerInfo: String, scoreDetails: String) {
        dao.insertGameHistory(
            GameHistoryEntity(
                gameName = gameName,
                winnerInfo = winnerInfo,
                scoreDetails = scoreDetails
            )
        )
    }

    suspend fun clearHistory() = dao.clearHistory()

    suspend fun seedDefaultsIfEmpty() {
        val defaultPlayers = listOf(
            PlayerEntity(name = "Aarav", groupTag = "Hostel Gang", avatarEmoji = "🔥", defaultOrder = 0),
            PlayerEntity(name = "Pooja", groupTag = "Hostel Gang", avatarEmoji = "💃", defaultOrder = 1),
            PlayerEntity(name = "Kabir", groupTag = "Hostel Gang", avatarEmoji = "😎", defaultOrder = 2),
            PlayerEntity(name = "Ananya", groupTag = "Hostel Gang", avatarEmoji = "✨", defaultOrder = 3),
            PlayerEntity(name = "Rohan", groupTag = "Family Adda", avatarEmoji = "👑", defaultOrder = 0),
            PlayerEntity(name = "Simran", groupTag = "Family Adda", avatarEmoji = "🌸", defaultOrder = 1),
            PlayerEntity(name = "Chacha Ji", groupTag = "Family Adda", avatarEmoji = "☕", defaultOrder = 2),
            PlayerEntity(name = "Bua Ji", groupTag = "Family Adda", avatarEmoji = "🍿", defaultOrder = 3)
        )

        if (!dao.isPlayersExist()) {
            dao.insertPlayers(defaultPlayers)
        }
    }
}
