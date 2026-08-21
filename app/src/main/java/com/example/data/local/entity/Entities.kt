package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_reports")
data class ScanReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String, // "PHISHING", "URL", "ROUTER_AUDIT", "INCIDENT"
    val targetInput: String,
    val riskScore: Int,
    val riskLevel: String, // "SAFE", "LOW", "MEDIUM", "HIGH", "CRITICAL"
    val summary: String,
    val findingsJson: String, // Comma-separated or serialized points
    val recommendationsJson: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String,
    val difficulty: String,
    val score: Int,
    val totalQuestions: Int,
    val percentage: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "academy_progress")
data class AcademyProgressEntity(
    @PrimaryKey
    val lessonId: String,
    val title: String,
    val category: String,
    val progressPercent: Int,
    val isCompleted: Boolean,
    val completedAt: Long? = null
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val conversationId: String = "default",
    val role: String, // "user", "model", "system"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val uid: String = "rakuzz_user_1",
    val name: String = "Security Specialist",
    val email: String = "specialist@rakuzz.cyber",
    val role: String = "Cyber Defender",
    val securityScore: Int = 85,
    val streakDays: Int = 4,
    val notificationsEnabled: Boolean = true,
    val darkThemeEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
