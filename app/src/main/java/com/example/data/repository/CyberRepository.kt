package com.example.data.repository

import android.content.Context
import com.example.data.AcademyData
import com.example.data.CyberQuizQuestion
import com.example.data.Lesson
import com.example.data.local.AppDatabase
import com.example.data.local.entity.AcademyProgressEntity
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.QuizResultEntity
import com.example.data.local.entity.ScanReportEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.data.remote.CyberAiEngine
import com.example.data.remote.IncidentPlanResult
import com.example.data.remote.PhishingScanResult
import com.example.data.remote.UrlScanResult
import com.example.util.PasswordAnalysisResult
import com.example.util.PasswordAnalyzer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class CyberRepository(context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val scanDao = db.scanReportDao()
    private val quizDao = db.quizResultDao()
    private val academyDao = db.academyProgressDao()
    private val chatDao = db.chatMessageDao()
    private val profileDao = db.userProfileDao()

    private val aiEngine = CyberAiEngine()
    private val mockUrlService = com.example.data.remote.MockUrlAnalysisService()

    // Scans
    val allScanReports: Flow<List<ScanReportEntity>> = scanDao.getAllScanReports()
    val recentReports: Flow<List<ScanReportEntity>> = scanDao.getRecentReports(5)

    suspend fun analyzeAndSavePhishing(messageText: String): PhishingScanResult {
        val result = aiEngine.analyzePhishing(messageText)
        val report = ScanReportEntity(
            type = "PHISHING",
            targetInput = if (messageText.length > 80) messageText.take(80) + "..." else messageText,
            riskScore = result.riskScore,
            riskLevel = result.riskLevel,
            summary = result.summary,
            findingsJson = result.findings.joinToString("|||"),
            recommendationsJson = result.recommendations.joinToString("|||"),
            timestamp = System.currentTimeMillis()
        )
        scanDao.insertReport(report)
        recalculateAndSaveSecurityScore()
        return result
    }

    suspend fun analyzeAndSaveUrl(
        url: String,
        onProgressUpdate: ((com.example.data.remote.MockScanStage) -> Unit)? = null
    ): com.example.data.remote.DetailedUrlAudit {
        val detailedAudit = mockUrlService.analyzeUrl(url, onProgressUpdate)
        val result = detailedAudit.scanResult
        val report = ScanReportEntity(
            type = "URL",
            targetInput = result.url,
            riskScore = result.riskScore,
            riskLevel = result.riskLevel,
            summary = result.summary,
            findingsJson = result.findings.joinToString("|||"),
            recommendationsJson = result.recommendations.joinToString("|||"),
            timestamp = System.currentTimeMillis()
        )
        scanDao.insertReport(report)
        recalculateAndSaveSecurityScore()
        return detailedAudit
    }

    suspend fun saveRouterAuditReport(brand: String, model: String, riskScore: Int, findings: List<String>, recs: List<String>) {
        val report = ScanReportEntity(
            type = "ROUTER_AUDIT",
            targetInput = "$brand $model",
            riskScore = riskScore,
            riskLevel = if (riskScore > 60) "HIGH" else if (riskScore > 30) "MEDIUM" else "SAFE",
            summary = "Manual Router Security Audit Completed.",
            findingsJson = findings.joinToString("|||"),
            recommendationsJson = recs.joinToString("|||"),
            timestamp = System.currentTimeMillis()
        )
        scanDao.insertReport(report)
        recalculateAndSaveSecurityScore()
    }

    suspend fun deleteScanReport(id: Long) {
        scanDao.deleteReportById(id)
    }

    suspend fun clearAllScanHistory() {
        scanDao.clearAllReports()
    }

    // Password Analyzer (Local Only)
    fun analyzePasswordLocally(password: String): PasswordAnalysisResult {
        return PasswordAnalyzer.analyze(password)
    }

    // Incident Response
    suspend fun generateIncidentPlan(incidentType: String, details: String): IncidentPlanResult {
        val result = aiEngine.generateIncidentPlan(incidentType, details)
        val report = ScanReportEntity(
            type = "INCIDENT",
            targetInput = incidentType,
            riskScore = if (result.severity == "CRITICAL") 85 else 60,
            riskLevel = result.severity,
            summary = "Emergency Incident Response Plan generated.",
            findingsJson = result.immediateActions.joinToString("|||"),
            recommendationsJson = result.containmentSteps.joinToString("|||"),
            timestamp = System.currentTimeMillis()
        )
        scanDao.insertReport(report)
        return result
    }

    // RAKUZZ AI Chat
    val chatMessages: Flow<List<ChatMessageEntity>> = chatDao.getMessagesForConversation()

    suspend fun sendChatMessage(userText: String): String {
        // Save user message
        val userMsg = ChatMessageEntity(role = "user", text = userText)
        chatDao.insertMessage(userMsg)

        val history = chatDao.getMessagesForConversation().firstOrNull() ?: emptyList()
        val aiReplyText = aiEngine.chatWithRakuzzAi(userText, history)

        // Save AI reply
        val modelMsg = ChatMessageEntity(role = "model", text = aiReplyText)
        chatDao.insertMessage(modelMsg)
        return aiReplyText
    }

    suspend fun clearChatHistory() {
        chatDao.clearChat()
    }

    // Academy
    val allLessons: List<Lesson> = AcademyData.lessons
    val academyProgress: Flow<List<AcademyProgressEntity>> = academyDao.getAllProgress()
    val completedLessonsCount: Flow<Int> = academyDao.getCompletedCount()

    fun getLessonById(id: String): Lesson? = allLessons.find { it.id == id }

    suspend fun markLessonCompleted(lesson: Lesson) {
        val progress = AcademyProgressEntity(
            lessonId = lesson.id,
            title = lesson.title,
            category = lesson.category,
            progressPercent = 100,
            isCompleted = true,
            completedAt = System.currentTimeMillis()
        )
        academyDao.saveProgress(progress)
        recalculateAndSaveSecurityScore()
    }

    // Quiz
    val quizQuestions: List<CyberQuizQuestion> = AcademyData.quizBank
    val allQuizResults: Flow<List<QuizResultEntity>> = quizDao.getAllQuizResults()

    suspend fun saveQuizResult(category: String, difficulty: String, score: Int, total: Int) {
        val percent = if (total > 0) (score * 100) / total else 0
        val entity = QuizResultEntity(
            category = category,
            difficulty = difficulty,
            score = score,
            totalQuestions = total,
            percentage = percent,
            timestamp = System.currentTimeMillis()
        )
        quizDao.insertQuizResult(entity)
        recalculateAndSaveSecurityScore()
    }

    // User Profile & Security Awareness Score
    val userProfile: Flow<UserProfileEntity?> = profileDao.getUserProfile()

    suspend fun initDefaultProfileIfNeeded() {
        val current = profileDao.getUserProfile().firstOrNull()
        if (current == null) {
            profileDao.saveUserProfile(UserProfileEntity())
        }
    }

    suspend fun updateProfile(name: String, email: String, notifications: Boolean) {
        val current = profileDao.getUserProfile().firstOrNull() ?: UserProfileEntity()
        profileDao.saveUserProfile(
            current.copy(
                name = name,
                email = email,
                notificationsEnabled = notifications
            )
        )
    }

    suspend fun recalculateAndSaveSecurityScore() {
        val completedCount = academyDao.getCompletedCount().firstOrNull() ?: 0
        val totalLessons = allLessons.size.coerceAtLeast(1)
        val lessonScoreComponent = (completedCount.toFloat() / totalLessons * 35).toInt()

        val avgQuiz = quizDao.getAverageScore().firstOrNull() ?: 70.0
        val quizScoreComponent = (avgQuiz * 0.35).toInt()

        val recentScans = scanDao.getRecentReports(5).firstOrNull() ?: emptyList()
        val scanCountBonus = (recentScans.size * 3).coerceAtMost(15)

        val baseScore = 15
        val totalScore = (baseScore + lessonScoreComponent + quizScoreComponent + scanCountBonus).coerceIn(40, 100)

        val current = profileDao.getUserProfile().firstOrNull() ?: UserProfileEntity()
        profileDao.saveUserProfile(current.copy(securityScore = totalScore))
    }
}
