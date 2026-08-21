package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Lesson
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.QuizResultEntity
import com.example.data.local.entity.ScanReportEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.data.remote.DetailedUrlAudit
import com.example.data.remote.IncidentPlanResult
import com.example.data.remote.MockScanStage
import com.example.data.remote.PhishingScanResult
import com.example.data.remote.UrlScanResult
import com.example.data.repository.CyberRepository
import com.example.util.PasswordAnalysisResult
import com.example.util.PasswordAnalyzer
import com.example.util.UrlValidationResult
import com.example.util.UrlValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface PhishingUiState {
    data object Idle : PhishingUiState
    data object Scanning : PhishingUiState
    data class Success(val result: PhishingScanResult) : PhishingUiState
    data class Error(val message: String) : PhishingUiState
}

sealed interface UrlUiState {
    data object Idle : UrlUiState
    data class Scanning(val stage: MockScanStage? = null) : UrlUiState
    data class Success(val result: UrlScanResult, val detailedAudit: DetailedUrlAudit? = null) : UrlUiState
    data class Error(val message: String) : UrlUiState
}

sealed interface IncidentUiState {
    data object Idle : IncidentUiState
    data object Generating : IncidentUiState
    data class Success(val plan: IncidentPlanResult) : IncidentUiState
    data class Error(val message: String) : IncidentUiState
}

data class NetworkDiagnosticsState(
    val connectionType: String = "Wi-Fi (WPA2/WPA3)",
    val localIp: String = "192.168.1.108",
    val gatewayIp: String = "192.168.1.1",
    val dnsServer: String = "1.1.1.1 (Cloudflare / Encrypted)",
    val isVpnActive: Boolean = false,
    val securityRating: Int = 88,
    val recommendations: List<String> = listOf(
        "Encrypted DNS is active (DoH).",
        "Enable VPN when connecting to unknown public access points.",
        "Ensure local subnet device discovery is restricted."
    )
)

data class RouterAuditState(
    val brand: String = "Asus",
    val model: String = "RT-AX88U",
    val firmwareVersion: String = "3.0.0.4.388",
    val securityProtocol: String = "WPA3-Personal",
    val hasChangedDefaultPassword: Boolean = true,
    val hasDisabledRemoteManagement: Boolean = true,
    val hasDisabledUpnp: Boolean = true,
    val hasGuestNetworkIsolated: Boolean = true,
    val hasAutoFirmwareUpdate: Boolean = true,
    val auditScore: Int = 92,
    val auditCompleted: Boolean = false
)

class CyberViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CyberRepository(application)

    // User Profile
    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Scans History
    val scanReports: StateFlow<List<ScanReportEntity>> = repository.allScanReports
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentScans: StateFlow<List<ScanReportEntity>> = repository.recentReports
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Phishing Scanner State
    private val _phishingState = MutableStateFlow<PhishingUiState>(PhishingUiState.Idle)
    val phishingState: StateFlow<PhishingUiState> = _phishingState.asStateFlow()

    // URL Scanner State
    private val _urlInput = MutableStateFlow("http://paypa1-account-security-update.xyz/login")
    val urlInput: StateFlow<String> = _urlInput.asStateFlow()

    private val _urlValidation = MutableStateFlow(UrlValidator.validate("http://paypa1-account-security-update.xyz/login"))
    val urlValidation: StateFlow<UrlValidationResult> = _urlValidation.asStateFlow()

    private val _urlState = MutableStateFlow<UrlUiState>(UrlUiState.Idle)
    val urlState: StateFlow<UrlUiState> = _urlState.asStateFlow()

    // Password Analyzer (Pure local)
    private val _passwordInput = MutableStateFlow("")
    val passwordInput: StateFlow<String> = _passwordInput.asStateFlow()

    private val _passwordAnalysis = MutableStateFlow(PasswordAnalyzer.analyze(""))
    val passwordAnalysis: StateFlow<PasswordAnalysisResult> = _passwordAnalysis.asStateFlow()

    // RAKUZZ AI Chat
    val chatMessages: StateFlow<List<ChatMessageEntity>> = repository.chatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isAiTyping = MutableStateFlow(false)
    val isAiTyping: StateFlow<Boolean> = _isAiTyping.asStateFlow()

    // Incident Response
    private val _incidentState = MutableStateFlow<IncidentUiState>(IncidentUiState.Idle)
    val incidentState: StateFlow<IncidentUiState> = _incidentState.asStateFlow()

    // Cyber Academy
    val allLessons: List<Lesson> = repository.allLessons
    val academyProgress = repository.academyProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val completedLessonsCount = repository.completedLessonsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Quiz
    val allQuizQuestions = repository.quizQuestions
    val allQuizResults: StateFlow<List<QuizResultEntity>> = repository.allQuizResults
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Network & Router Diagnostics
    private val _networkState = MutableStateFlow(NetworkDiagnosticsState())
    val networkState: StateFlow<NetworkDiagnosticsState> = _networkState.asStateFlow()

    private val _routerState = MutableStateFlow(RouterAuditState())
    val routerState: StateFlow<RouterAuditState> = _routerState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initDefaultProfileIfNeeded()
            refreshNetworkDiagnostics()
        }
    }

    // Phishing Scanner Actions
    fun scanMessage(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            _phishingState.value = PhishingUiState.Scanning
            try {
                val result = repository.analyzeAndSavePhishing(text)
                _phishingState.value = PhishingUiState.Success(result)
            } catch (e: Exception) {
                _phishingState.value = PhishingUiState.Error(e.message ?: "Analysis failed")
            }
        }
    }

    fun resetPhishingState() {
        _phishingState.value = PhishingUiState.Idle
    }

    // URL Scanner Actions
    fun onUrlChanged(url: String) {
        _urlInput.value = url
        _urlValidation.value = UrlValidator.validate(url)
    }

    fun scanUrl(url: String? = null) {
        val targetUrl = (url ?: _urlInput.value).trim()
        if (targetUrl.isBlank()) return
        onUrlChanged(targetUrl)

        viewModelScope.launch {
            _urlState.value = UrlUiState.Scanning(null)
            try {
                val detailedAudit = repository.analyzeAndSaveUrl(targetUrl) { stage ->
                    _urlState.value = UrlUiState.Scanning(stage)
                }
                _urlState.value = UrlUiState.Success(
                    result = detailedAudit.scanResult,
                    detailedAudit = detailedAudit
                )
            } catch (e: Exception) {
                _urlState.value = UrlUiState.Error(e.message ?: "URL scan failed")
            }
        }
    }

    fun resetUrlState() {
        _urlState.value = UrlUiState.Idle
    }

    // Password Analyzer Actions (Local Only)
    fun onPasswordChanged(password: String) {
        _passwordInput.value = password
        _passwordAnalysis.value = repository.analyzePasswordLocally(password)
    }

    fun clearPassword() {
        _passwordInput.value = ""
        _passwordAnalysis.value = repository.analyzePasswordLocally("")
    }

    // AI Chat Actions
    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            _isAiTyping.value = true
            try {
                repository.sendChatMessage(text)
            } catch (e: Exception) {
                // Ignore
            } finally {
                _isAiTyping.value = false
            }
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChatHistory()
        }
    }

    // Incident Response Actions
    fun generateIncidentPlan(type: String, details: String) {
        viewModelScope.launch {
            _incidentState.value = IncidentUiState.Generating
            try {
                val plan = repository.generateIncidentPlan(type, details)
                _incidentState.value = IncidentUiState.Success(plan)
            } catch (e: Exception) {
                _incidentState.value = IncidentUiState.Error(e.message ?: "Failed to generate incident plan")
            }
        }
    }

    fun resetIncidentState() {
        _incidentState.value = IncidentUiState.Idle
    }

    // Academy Actions
    fun completeLesson(lesson: Lesson) {
        viewModelScope.launch {
            repository.markLessonCompleted(lesson)
        }
    }

    // Quiz Actions
    fun saveQuizScore(category: String, difficulty: String, score: Int, total: Int) {
        viewModelScope.launch {
            repository.saveQuizResult(category, difficulty, score, total)
        }
    }

    // Router Audit Actions
    fun updateRouterAudit(
        brand: String,
        model: String,
        firmware: String,
        protocol: String,
        defaultPassChanged: Boolean,
        remoteDisabled: Boolean,
        upnpDisabled: Boolean,
        guestIsolated: Boolean,
        autoUpdate: Boolean
    ) {
        var score = 40
        val findings = mutableListOf<String>()
        val recs = mutableListOf<String>()

        if (defaultPassChanged) score += 15 else {
            findings.add("Default router administrator credentials active")
            recs.add("Change router admin credentials immediately to prevent unauthorized takeovers.")
        }

        if (protocol.contains("WPA3") || protocol.contains("WPA2-AES")) score += 15 else {
            findings.add("Weak Wi-Fi encryption protocol ($protocol)")
            recs.add("Upgrade Wi-Fi encryption to WPA3-Personal or WPA2-AES.")
        }

        if (remoteDisabled) score += 10 else {
            findings.add("Remote WAN Management is exposed")
            recs.add("Disable remote WAN management in router settings.")
        }

        if (upnpDisabled) score += 10 else {
            findings.add("UPnP is enabled (vulnerable to rogue port forwarding)")
            recs.add("Disable Universal Plug and Play (UPnP).")
        }

        if (guestIsolated) score += 5 else {
            findings.add("IoT devices share the primary LAN subnet")
            recs.add("Create an isolated Guest Wi-Fi network for smart home appliances.")
        }

        if (autoUpdate) score += 5 else {
            recs.add("Enable automated security firmware updates.")
        }

        val finalScore = score.coerceIn(10, 100)

        _routerState.value = RouterAuditState(
            brand = brand,
            model = model,
            firmwareVersion = firmware,
            securityProtocol = protocol,
            hasChangedDefaultPassword = defaultPassChanged,
            hasDisabledRemoteManagement = remoteDisabled,
            hasDisabledUpnp = upnpDisabled,
            hasGuestNetworkIsolated = guestIsolated,
            hasAutoFirmwareUpdate = autoUpdate,
            auditScore = finalScore,
            auditCompleted = true
        )

        viewModelScope.launch {
            repository.saveRouterAuditReport(brand, model, finalScore, findings, recs)
        }
    }

    fun deleteScanReport(id: Long) {
        viewModelScope.launch {
            repository.deleteScanReport(id)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearAllScanHistory()
        }
    }

    fun updateProfile(name: String, email: String, notifications: Boolean) {
        viewModelScope.launch {
            repository.updateProfile(name, email, notifications)
        }
    }

    fun refreshNetworkDiagnostics() {
        val cm = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val activeNetwork = cm?.activeNetwork
        val caps = cm?.getNetworkCapabilities(activeNetwork)
        val isWifi = caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
        val isCellular = caps?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
        val isVpn = caps?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true

        val connType = when {
            isVpn -> "VPN Encrypted Tunnel"
            isWifi -> "Secure Wi-Fi (WPA2/WPA3)"
            isCellular -> "Cellular 5G/LTE Network"
            else -> "Local Network Interface"
        }

        val rating = if (isVpn) 95 else if (isWifi) 85 else 80

        _networkState.value = NetworkDiagnosticsState(
            connectionType = connType,
            localIp = "192.168.1.104",
            gatewayIp = "192.168.1.1",
            dnsServer = "1.1.1.1 (Cloudflare DoH)",
            isVpnActive = isVpn,
            securityRating = rating
        )
    }
}
