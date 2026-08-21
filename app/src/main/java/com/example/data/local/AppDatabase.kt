package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.AcademyProgressDao
import com.example.data.local.dao.ChatMessageDao
import com.example.data.local.dao.QuizResultDao
import com.example.data.local.dao.ScanReportDao
import com.example.data.local.dao.UserProfileDao
import com.example.data.local.entity.AcademyProgressEntity
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.QuizResultEntity
import com.example.data.local.entity.ScanReportEntity
import com.example.data.local.entity.UserProfileEntity

@Database(
    entities = [
        ScanReportEntity::class,
        QuizResultEntity::class,
        AcademyProgressEntity::class,
        ChatMessageEntity::class,
        UserProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scanReportDao(): ScanReportDao
    abstract fun quizResultDao(): QuizResultDao
    abstract fun academyProgressDao(): AcademyProgressDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rakuzz_cyber_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
