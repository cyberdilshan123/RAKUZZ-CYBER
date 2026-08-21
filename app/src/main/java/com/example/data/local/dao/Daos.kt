package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.AcademyProgressEntity
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.QuizResultEntity
import com.example.data.local.entity.ScanReportEntity
import com.example.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanReportDao {
    @Query("SELECT * FROM scan_reports ORDER BY timestamp DESC")
    fun getAllScanReports(): Flow<List<ScanReportEntity>>

    @Query("SELECT * FROM scan_reports WHERE type = :type ORDER BY timestamp DESC")
    fun getReportsByType(type: String): Flow<List<ScanReportEntity>>

    @Query("SELECT * FROM scan_reports ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentReports(limit: Int = 5): Flow<List<ScanReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ScanReportEntity): Long

    @Query("DELETE FROM scan_reports WHERE id = :id")
    suspend fun deleteReportById(id: Long)

    @Query("DELETE FROM scan_reports")
    suspend fun clearAllReports()
}

@Dao
interface QuizResultDao {
    @Query("SELECT * FROM quiz_results ORDER BY timestamp DESC")
    fun getAllQuizResults(): Flow<List<QuizResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(result: QuizResultEntity): Long

    @Query("SELECT AVG(percentage) FROM quiz_results")
    fun getAverageScore(): Flow<Double?>
}

@Dao
interface AcademyProgressDao {
    @Query("SELECT * FROM academy_progress")
    fun getAllProgress(): Flow<List<AcademyProgressEntity>>

    @Query("SELECT * FROM academy_progress WHERE lessonId = :lessonId")
    suspend fun getProgressForLesson(lessonId: String): AcademyProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: AcademyProgressEntity)

    @Query("SELECT COUNT(*) FROM academy_progress WHERE isCompleted = 1")
    fun getCompletedCount(): Flow<Int>
}

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages WHERE conversationId = :convId ORDER BY timestamp ASC")
    fun getMessagesForConversation(convId: String = "default"): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity): Long

    @Query("DELETE FROM chat_messages WHERE conversationId = :convId")
    suspend fun clearChat(convId: String = "default")
}

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET securityScore = :score WHERE uid = :uid")
    suspend fun updateSecurityScore(uid: String, score: Int)
}
