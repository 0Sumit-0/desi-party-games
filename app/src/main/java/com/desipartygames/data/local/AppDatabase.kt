package com.desipartygames.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.desipartygames.data.local.dao.PartyGamesDao
import com.desipartygames.data.local.entity.CustomPromptEntity
import com.desipartygames.data.local.entity.GameHistoryEntity
import com.desipartygames.data.local.entity.PlayerEntity

@Database(
    entities = [
        PlayerEntity::class,
        CustomPromptEntity::class,
        GameHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun partyGamesDao(): PartyGamesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "desi_party_games.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
