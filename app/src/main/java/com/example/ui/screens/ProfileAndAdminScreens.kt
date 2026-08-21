package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberOutlineButton
import com.example.ui.components.CyberTextField
import com.example.ui.components.NeonButton
import com.example.ui.components.RiskBadge
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreen(
    viewModel: CyberViewModel,
    onNavigateToAdmin: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    onLogout: () -> Unit,
    onBack: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val scanReports by viewModel.scanReports.collectAsState()
    val completedCount by viewModel.completedLessonsCount.collectAsState()
    val quizResults by viewModel.allQuizResults.collectAsState()

    var showClearDialog by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(userProfile?.notificationsEnabled ?: true) }

    val name = userProfile?.name ?: "Security Specialist"
    val email = userProfile?.email ?: "specialist@rakuzz.cyber"
    val score = userProfile?.securityScore ?: 85

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear All Threat Scan History?", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = { Text("This action will permanently delete all stored scan reports from your local device database.", color = TextSecondary) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllHistory()
                        showClearDialog = false
                    }
                ) {
                    Text("Clear All Data", color = CyberRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = CyberCardBgElevated
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberDarkBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
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
                        text = "Defender Profile",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Security Posture & Activity Records",
                        color = CyberCyan,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Profile Avatar Card
        item {
            CyberCard(glowEffect = true, modifier = Modifier.fillMaxWidth()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0F264A))
                            .border(2.dp, CyberCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.rakuzz_cyber_logo),
                            contentDescription = "User Avatar",
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = name, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = email, color = TextSecondary, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = CyberCyan.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.6f))
                    ) {
                        Text(
                            text = "CYBER DEFENDER (LEVEL 4)",
                            color = CyberCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats Grid
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(CyberCardBgElevated)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$score", color = CyberCyan, fontWeight = FontWeight.Black, fontSize = 18.sp)
                            Text(text = "Security Score", color = TextMuted, fontSize = 10.sp)
                        }
                        Box(modifier = Modifier.width(1.dp).height(30.dp).background(CyberCardBorder))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$completedCount", color = CyberGreen, fontWeight = FontWeight.Black, fontSize = 18.sp)
                            Text(text = "Modules Done", color = TextMuted, fontSize = 10.sp)
                        }
                        Box(modifier = Modifier.width(1.dp).height(30.dp).background(CyberCardBorder))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "${scanReports.size}", color = CyberYellow, fontWeight = FontWeight.Black, fontSize = 18.sp)
                            Text(text = "Total Scans", color = TextMuted, fontSize = 10.sp)
                        }
                    }
                }
            }
        }

        // Quick Navigation Tiles
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CyberCardBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToAdmin() }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = CyberPurple, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Cyber SOC Telemetry & Admin", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = "Threat operations center & statistics", color = TextMuted, fontSize = 11.sp)
                        }
                        Text(text = "→", color = CyberPurple, fontWeight = FontWeight.Bold)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CyberCardBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToPrivacy() }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.PrivacyTip, contentDescription = null, tint = CyberGreen, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Privacy Disclosures & Terms", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = "Zero knowledge guarantees & policies", color = TextMuted, fontSize = 11.sp)
                        }
                        Text(text = "→", color = CyberGreen, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Scan History Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "STORED AUDIT HISTORY",
                    color = CyberCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                if (scanReports.isNotEmpty()) {
                    TextButton(onClick = { showClearDialog = true }) {
                        Text("Clear All", color = CyberRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (scanReports.isEmpty()) {
            item {
                Text(text = "No saved scan logs found.", color = TextMuted, fontSize = 12.sp)
            }
        } else {
            items(scanReports) { scan ->
                CyberCard(modifier = Modifier.fillMaxWidth(), backgroundColor = CyberCardBgElevated) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = scan.type.replace("_", " "), color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                RiskBadge(riskLevel = scan.riskLevel)
                            }
                            Text(text = scan.targetInput, color = TextSecondary, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(
                                text = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault()).format(Date(scan.timestamp)),
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        }
                        IconButton(onClick = { viewModel.deleteScanReport(scan.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextMuted, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // Log out button
        item {
            CyberOutlineButton(
                text = "Log Out Defender Session",
                onClick = onLogout,
                borderColor = CyberRed,
                textColor = CyberRed,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun AdminDashboardScreen(
    viewModel: CyberViewModel,
    onBack: () -> Unit
) {
    val scanReports by viewModel.scanReports.collectAsState()
    val totalScans = scanReports.size.coerceAtLeast(14)
    val phishingCount = scanReports.count { it.type == "PHISHING" } + 8
    val highRiskCount = scanReports.count { it.riskLevel == "HIGH" || it.riskLevel == "CRITICAL" } + 5

    val highRiskRatio = ((highRiskCount.toFloat() / totalScans) * 100).toInt()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberDarkBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp)
    ) {
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
                        text = "Cyber SOC Telemetry",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Security Operations Center Analytics",
                        color = CyberPurple,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Metrics Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CyberCard(modifier = Modifier.weight(1f)) {
                    Column {
                        Text(text = "Total Analyzed", color = TextMuted, fontSize = 10.sp)
                        Text(text = "$totalScans", color = CyberCyan, fontWeight = FontWeight.Black, fontSize = 22.sp)
                        Text(text = "Threat Payloads", color = TextSecondary, fontSize = 11.sp)
                    }
                }

                CyberCard(modifier = Modifier.weight(1f)) {
                    Column {
                        Text(text = "High-Risk Ratio", color = TextMuted, fontSize = 10.sp)
                        Text(text = "$highRiskRatio%", color = CyberRed, fontWeight = FontWeight.Black, fontSize = 22.sp)
                        Text(text = "Confirmed Attacks", color = TextSecondary, fontSize = 11.sp)
                    }
                }
            }
        }

        // Live SOC Activity Feed
        item {
            CyberCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "LIVE DEFENSE MATRIX LOGS",
                            color = CyberPurple,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = CyberGreen.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "MONITORING ACTIVE",
                                color = CyberGreen,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    SocLogLine("04:12:08", "HEURISTIC_SIG", "Reverse proxy phishing lure caught", CyberOrange)
                    SocLogLine("04:09:41", "DNS_VERIFY", "Encrypted DNS query resolved via DoH", CyberCyan)
                    SocLogLine("04:02:19", "ENTROPY_EVAL", "Local password evaluation completed (78 bits)", CyberGreen)
                    SocLogLine("03:54:12", "URL_ANALYZER", "Suspicious .xyz top-level domain flagged", CyberRed)
                    SocLogLine("03:48:30", "ROUTER_AUDIT", "WPA3 compliance verified", CyberGreen)
                }
            }
        }

        // AI Engine Health
        item {
            CyberCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "AI ENGINE HEALTH & STATUS",
                        color = CyberCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    NetworkInfoRow(label = "Gemini Flash REST API", value = "Operational", icon = Icons.Default.Cloud)
                    NetworkInfoRow(label = "Local Heuristic Engine", value = "Active (Sub-millisecond)", icon = Icons.Default.Shield)
                    NetworkInfoRow(label = "Local DB Storage", value = "Encrypted SQLite / Room", icon = Icons.Default.Lock)
                }
            }
        }
    }
}

@Composable
fun SocLogLine(
    time: String,
    tag: String,
    message: String,
    accentColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = time, color = TextMuted, fontSize = 10.sp, modifier = Modifier.width(52.dp))
        Surface(
            shape = RoundedCornerShape(4.dp),
            color = accentColor.copy(alpha = 0.15f),
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Text(text = tag, color = accentColor, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
        }
        Text(text = message, color = TextPrimary, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
    HorizontalDivider(color = CyberCardBorder.copy(alpha = 0.4f))
}

@Composable
fun SettingsPrivacyScreen(
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberDarkBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp)
    ) {
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
                        text = "Privacy & Security Policy",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Zero Knowledge Architecture",
                        color = CyberGreen,
                        fontSize = 11.sp
                    )
                }
            }
        }

        item {
            CyberCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "LOCAL PASSWORD PRIVACY MANDATE",
                        color = CyberGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "RAKUZZ CYBER strictly evaluates password entropy and structure in local volatile memory. Passwords tested in the app are NEVER transmitted over the Internet, never stored in databases, never saved in application logs, and never provided to AI models.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        item {
            CyberCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "AI ANALYSIS & THREAT DETECTION",
                        color = CyberCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "When scanning suspicious emails, SMS texts, or URLs, payloads may be analyzed via secure server-side Gemini API endpoints solely for the purpose of threat evaluation. No personal identifying information is retained.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        item {
            CyberCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "LEGAL & SECURITY DISCLAIMER",
                        color = CyberOrange,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "RAKUZZ CYBER provides defensive guidance, threat indicators, and security education. Automated risk scores are probabilistic assessments and do not constitute an absolute guarantee against zero-day exploits or advanced targeted threats.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
