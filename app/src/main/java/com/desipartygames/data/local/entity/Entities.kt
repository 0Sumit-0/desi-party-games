package com.desipartygames.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val groupTag: String = "Hostel Gang", // e.g. "Hostel Gang", "Family", "Chai Adda"
    val avatarEmoji: String = "🦁",
    val defaultOrder: Int = 0
)

@Entity(tableName = "custom_prompts")
data class CustomPromptEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gameType: String, // "TRUTH", "DARE", "IMPOSTER_WORD", "CHARADES_MOVIE"
    val category: String, // "Friends", "Family", "Spicy", "Bollywood"
    val content: String,
    val contentHindi: String = "",
    val isSpicy: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "game_history")
data class GameHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gameName: String,
    val winnerInfo: String,
    val scoreDetails: String,
    val dateTimestamp: Long = System.currentTimeMillis()
)
