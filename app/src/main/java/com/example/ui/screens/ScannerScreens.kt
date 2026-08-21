package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.DetailedUrlAudit
import com.example.data.remote.PhishingScanResult
import com.example.data.remote.UrlScanResult
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberOutlineButton
import com.example.ui.components.CyberTextField
import com.example.ui.components.NeonButton
import com.example.ui.components.RiskBadge
import com.example.ui.components.ScanningIndicator
import com.example.ui.components.ValidatedUrlInputField
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.CyberCardBgElevated
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberDarkBg
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberOrange
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.CyberRed
import com.example.ui.theme.CyberYellow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.CyberViewModel
import com.example.ui.viewmodel.PhishingUiState
import com.example.ui.viewmodel.UrlUiState

@Composable
fun PhishingScannerScreen(
    viewModel: CyberViewModel,
    onBack: () -> Unit
) {
    val phishingState by viewModel.phishingState.collectAsState()
    var inputMessage by remember {
        mutableStateOf(
            "URGENT: Your bank account has been suspended due to suspicious activity. Click here immediately to verify your identity and restore access: http://secure-bank-login-portal.top/auth"
        )
    }

    val sampleMessages = listOf(
        "Urgent Bank Suspension" to "URGENT: Your account has been suspended! Verify your credentials within 24h at http://bank-secure-auth.xyz to avoid permanent closure.",
        "Fake Delivery SMS" to "FedEx: Your package #US99281 could not be delivered due to an unpaid customs fee of $2.49. Update info at http://fedx-pkg-track.top",
        "Prize / Lottery Scam" to "Congratulations! You won $50,000 in our international crypto lottery. Reply with your full name and OTP to claim your funds.",
        "Clean Normal Message" to "Hey Alex, are we still meeting for lunch tomorrow at 1:00 PM? Let me know if the time works for you."
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberDarkBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(CyberCardBg)
                        .border(1.dp, CyberCardBorder, RoundedCornerShape(10.dp))
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = CyberCyan)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "AI Phishing Detector",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Message, SMS & Email Threat Analysis",
                        color = CyberCyan,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Quick Preset Samples
        item {
            Column {
                Text(
                    text = "QUICK TEST SAMPLES",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(sampleMessages) { (title, msg) ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CyberCardBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
                            modifier = Modifier.clickable {
                                inputMessage = msg
                                viewModel.resetPhishingState()
                            }
                        ) {
                            Text(
                                text = title,
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Input Card
        item {
            CyberCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "PASTE SUSPICIOUS TEXT",
                        color = CyberCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    CyberTextField(
                        value = inputMessage,
                        onValueChange = { inputMessage = it },
                        placeholder = "Paste email, SMS, or chat message body here...",
                        isSingleLine = false,
                        maxLines = 6,
                        modifier = Modifier.height(130.dp),
                        testTag = "phishing_message_input"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        NeonButton(
                            text = "Analyze Message",
                            onClick = { viewModel.scanMessage(inputMessage) },
                            icon = Icons.Default.Security,
                            isLoading = phishingState is PhishingUiState.Scanning,
                            modifier = Modifier.weight(1f),
                            testTag = "scan_phishing_button"
                        )

                        if (phishingState !is PhishingUiState.Idle) {
                            CyberOutlineButton(
                                text = "Reset",
                                onClick = { viewModel.resetPhishingState() },
                                icon = Icons.Default.Refresh,
                                modifier = Modifier.width(100.dp)
                            )
                        }
                    }
                }
            }
        }

        // Scanning Indicator
        if (phishingState is PhishingUiState.Scanning) {
            item {
                ScanningIndicator(text = "Evaluating Linguistic & Urgency Threat Signatures...")
            }
        }

        // Results Card
        if (phishingState is PhishingUiState.Success) {
            val result = (phishingState as PhishingUiState.Success).result
            item {
                PhishingResultCard(result = result)
            }
        }
    }
}

@Composable
fun PhishingResultCard(result: PhishingScanResult) {
    val (scoreColor, scoreBorder) = when {
        result.riskScore >= 75 -> CyberRed to CyberRed.copy(alpha = 0.8f)
        result.riskScore >= 50 -> CyberOrange to CyberOrange.copy(alpha = 0.8f)
        result.riskScore >= 25 -> CyberYellow to CyberYellow.copy(alpha = 0.8f)
        else -> CyberGreen to CyberGreen.copy(alpha = 0.8f)
    }

    CyberCard(
        borderColor = scoreBorder,
        glowEffect = result.riskScore >= 60,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "THREAT ASSESSMENT REPORT",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Calculated Risk: ${result.riskScore} / 100",
                        color = scoreColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                RiskBadge(riskLevel = result.riskLevel)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = CyberCardBgElevated,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (result.riskScore >= 50) Icons.Default.Warning else Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = scoreColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = result.summary,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "DETECTED THREAT INDICATORS",
                color = CyberCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            result.findings.forEach { finding ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(text = "•", color = scoreColor, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
                    Text(text = finding, color = TextSecondary, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "RECOMMENDED DEFENSIVE ACTIONS",
                color = CyberGreen,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            result.recommendations.forEach { rec ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = CyberGreen,
                        modifier = Modifier
                            .size(14.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = rec, color = TextPrimary, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (result.isAiAnalyzed) CyberPurple.copy(alpha = 0.2f) else CyberBlue.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = if (result.isAiAnalyzed) "Gemini AI Model Analyzed" else "Local Heuristic Engine",
                        color = if (result.isAiAnalyzed) CyberPurple else CyberBlue,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Text(
                    text = "Urgency Level: ${result.urgencyLevel}",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun UrlScannerScreen(
    viewModel: CyberViewModel,
    onBack: () -> Unit
) {
    val urlState by viewModel.urlState.collectAsState()
    val inputUrl by viewModel.urlInput.collectAsState()
    val validationResult by viewModel.urlValidation.collectAsState()

    val sampleUrls = listOf(
        "Typosquat PayPal" to "http://paypa1-account-security-update.xyz/login",
        "Raw IP Host" to "http://192.168.1.1/cgi-bin/admin.html",
        "Obfuscated @ URL" to "https://google.com@auth-recovery-node.top/login",
        "Subdomain Stacking" to "https://login.appleid.com.verify-billing.suspicious-domain.cfd",
        "Suspicious Crypto TLD" to "http://binance-claim-airdrop.buzz/token",
        "Legitimate HTTPS" to "https://www.google.com"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberDarkBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(CyberCardBg)
                        .border(1.dp, CyberCardBorder, RoundedCornerShape(10.dp))
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = CyberCyan)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "URL & Phishing Scanner",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Real-Time Malicious Pattern Validation & Deep Mock Audit",
                        color = CyberCyan,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Preset Samples
        item {
            Column {
                Text(
                    text = "SAMPLE THREAT VECTORS",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(sampleUrls) { (title, url) ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CyberCardBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
                            modifier = Modifier.clickable {
                                viewModel.onUrlChanged(url)
                                viewModel.resetUrlState()
                            }
                        ) {
                            Text(
                                text = title,
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Input Card with Live Pattern Validation
        item {
            CyberCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    ValidatedUrlInputField(
                        value = inputUrl,
                        onValueChange = { viewModel.onUrlChanged(it) },
                        validationResult = validationResult,
                        onScanTriggered = { viewModel.scanUrl() },
                        testTag = "url_scanner_input"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        NeonButton(
                            text = "Run Threat Analysis",
                            onClick = { viewModel.scanUrl() },
                            icon = Icons.Default.Language,
                            isLoading = urlState is UrlUiState.Scanning,
                            modifier = Modifier.weight(1f),
                            testTag = "scan_url_button"
                        )

                        if (urlState !is UrlUiState.Idle) {
                            CyberOutlineButton(
                                text = "Reset",
                                onClick = { viewModel.resetUrlState() },
                                icon = Icons.Default.Refresh,
                                modifier = Modifier.width(100.dp)
                            )
                        }
                    }
                }
            }
        }

        // Scanning State with Multi-Stage Progress
        if (urlState is UrlUiState.Scanning) {
            val scanningState = urlState as UrlUiState.Scanning
            val stepText = scanningState.stage?.currentStepName ?: "Correlating Multi-Engine Phishing & Reputation Signatures..."
            item {
                CyberCard(
                    borderColor = CyberCyan.copy(alpha = 0.6f),
                    glowEffect = true,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    ) {
                        ScanningIndicator(text = stepText)

                        if (scanningState.stage != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            androidx.compose.material3.LinearProgressIndicator(
                                progress = { scanningState.stage.progressPercent },
                                modifier = Modifier
                                    .fillMaxWidth(0.85f)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = CyberCyan,
                                trackColor = CyberCardBorder
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Stage ${scanningState.stage.stageIndex} of ${scanningState.stage.totalStages}",
                                color = TextMuted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Results
        if (urlState is UrlUiState.Success) {
            val successState = urlState as UrlUiState.Success
            item {
                UrlResultCard(
                    result = successState.result,
                    detailedAudit = successState.detailedAudit
                )
            }
        }
    }
}

@Composable
fun UrlResultCard(
    result: UrlScanResult,
    detailedAudit: DetailedUrlAudit? = null
) {
    val (scoreColor, scoreBorder) = when {
        result.riskScore >= 75 -> CyberRed to CyberRed.copy(alpha = 0.8f)
        result.riskScore >= 50 -> CyberOrange to CyberOrange.copy(alpha = 0.8f)
        result.riskScore >= 25 -> CyberYellow to CyberYellow.copy(alpha = 0.8f)
        else -> CyberGreen to CyberGreen.copy(alpha = 0.8f)
    }

    CyberCard(
        borderColor = scoreBorder,
        glowEffect = result.riskScore >= 50,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Header Row: Score & Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "MOCK THREAT AUDIT REPORT",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "${result.riskScore} / 100",
                        color = scoreColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                RiskBadge(riskLevel = result.riskLevel)
            }

            // Threat Category Classification Banner
            if (detailedAudit != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = scoreColor.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, scoreColor.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (result.riskScore >= 50) Icons.Default.Warning else Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = scoreColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = detailedAudit.category,
                            color = scoreColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Protocol & Host Grid (Bento mini-cards)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = CyberCardBgElevated,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (result.isHttps) Icons.Default.Lock else Icons.Default.LockOpen,
                            contentDescription = null,
                            tint = if (result.isHttps) CyberGreen else CyberRed,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "PROTOCOL",
                                color = TextMuted,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (result.isHttps) "HTTPS Encrypted" else "Insecure HTTP",
                                color = if (result.isHttps) CyberGreen else CyberRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = CyberCardBgElevated,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "CANONICAL HOST",
                                color = TextMuted,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = result.domain,
                                color = TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // Detailed Metadata Grid if available
            if (detailedAudit != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = CyberCardBgElevated,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "DOMAIN AGE", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(text = detailedAudit.domainAge, color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = CyberCardBgElevated,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "SSL / TLS CERTIFICATE", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(text = detailedAudit.sslIssuer, color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Detected Findings & Threat Signatures
            Text(
                text = "THREAT SIGNATURES & ANOMALIES",
                color = CyberCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            result.findings.forEach { finding ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(text = "•", color = scoreColor, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
                    Text(text = finding, color = TextSecondary, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Defensive Recommendations
            Text(
                text = "DEFENSIVE MITIGATION ACTIONS",
                color = CyberGreen,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            result.recommendations.forEach { rec ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = CyberGreen,
                        modifier = Modifier
                            .size(14.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = rec, color = TextPrimary, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF161E36),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Mock analysis service evaluates lexical patterns, typosquat distance, TLD reputation, and transport encryption.",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
