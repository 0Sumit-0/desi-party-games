package com.desipartygames.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import com.desipartygames.data.local.entity.CustomPromptEntity
import com.desipartygames.data.local.entity.GameHistoryEntity
import com.desipartygames.data.local.entity.PlayerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PartyGamesDao {
    // Players
    @Query("SELECT * FROM players ORDER BY groupTag, defaultOrder, id ASC")
    fun getAllPlayers(): Flow<List<PlayerEntity>>

    @Query(value = "SELECT EXISTS (SELECT 1 FROM players)")
    suspend fun isPlayersExist(): Boolean

    @Query("SELECT * FROM players WHERE groupTag = :groupTag ORDER BY defaultOrder, id ASC")
    fun getPlayersByGroup(groupTag: String): Flow<List<PlayerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: PlayerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayers(players: List<PlayerEntity>)

    @Delete
    suspend fun deletePlayer(player: PlayerEntity)

    @Query("DELETE FROM players WHERE id = :id")
    suspend fun deletePlayerById(id: Int)

    // Custom Prompts
    @Query("SELECT * FROM custom_prompts ORDER BY id DESC")
    fun getAllCustomPrompts(): Flow<List<CustomPromptEntity>>

    @Query("SELECT * FROM custom_prompts WHERE gameType = :gameType ORDER BY id DESC")
    fun getCustomPromptsByType(gameType: String): Flow<List<CustomPromptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomPrompt(prompt: CustomPromptEntity): Long

    @Delete
    suspend fun deleteCustomPrompt(prompt: CustomPromptEntity)

    // Game History
    @Query("SELECT * FROM game_history ORDER BY dateTimestamp DESC LIMIT 30")
    fun getRecentGameHistory(): Flow<List<GameHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameHistory(history: GameHistoryEntity): Long

    @Query("DELETE FROM game_history")
    suspend fun clearHistory()
}
