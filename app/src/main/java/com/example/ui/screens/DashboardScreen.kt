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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.CyberCard
import com.example.ui.components.RiskBadge
import com.example.ui.components.SecurityScoreGauge
import com.example.ui.components.ThreatAlertBanner
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.CyberCardBgElevated
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberDarkBg
import com.example.ui.theme.CyberDarkSurface
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
fun DashboardScreen(
    viewModel: CyberViewModel,
    onNavigateToPhishing: () -> Unit,
    onNavigateToUrlScan: () -> Unit,
    onNavigateToPassword: () -> Unit,
    onNavigateToAiChat: () -> Unit,
    onNavigateToAcademy: () -> Unit,
    onNavigateToQuiz: () -> Unit,
    onNavigateToIncident: () -> Unit,
    onNavigateToRouterAudit: () -> Unit,
    onNavigateToNetwork: () -> Unit,
    onOpenDrawer: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val recentScans by viewModel.recentScans.collectAsState()
    val completedLessons by viewModel.completedLessonsCount.collectAsState()
    val totalLessons = viewModel.allLessons.size
    val quizResults by viewModel.allQuizResults.collectAsState()

    val securityScore = userProfile?.securityScore ?: 85

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberDarkBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
    ) {
        // Top Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onOpenDrawer,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(CyberCardBg)
                            .border(1.dp, CyberCardBorder, RoundedCornerShape(10.dp))
                            .testTag("dashboard_menu_button")
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = CyberCyan)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "RAKUZZ CYBER",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "DEFENSIVE OPERATIONS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberCyan,
                            letterSpacing = 1.5.sp
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = CyberCyan.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(CyberGreen)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SYSTEM SECURE",
                            color = CyberGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }

        // Hero Banner Visual Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, CyberCyan.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CyberCardBg)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.cyber_hero_banner),
                        contentDescription = "Cyber Security Operations Center",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        CyberDarkBg.copy(alpha = 0.92f),
                                        Color(0xCC08132B),
                                        Color(0x6608132B)
                                    )
                                )
                            )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = CyberCyan.copy(alpha = 0.2f),
                                modifier = Modifier.padding(bottom = 4.dp)
                            ) {
                                Text(
                                    text = "AI DEFENSE ENGINE ACTIVE",
                                    color = CyberCyan,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = "Protect • Detect • Educate",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Next-Gen AI Analysis for Phishing, URLs & Password Auditing",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }

        // Animated Security Score Gauge
        item {
            CyberCard(
                glowEffect = true,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SecurityScoreGauge(
                        score = securityScore,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(CyberCardBgElevated)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$completedLessons/$totalLessons", color = CyberCyan, fontWeight = FontWeight.Black, fontSize = 15.sp)
                            Text(text = "Lessons", color = TextMuted, fontSize = 11.sp)
                        }
                        Box(modifier = Modifier.width(1.dp).height(28.dp).background(CyberCardBorder))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "${quizResults.size}", color = CyberGreen, fontWeight = FontWeight.Black, fontSize = 15.sp)
                            Text(text = "Quizzes", color = TextMuted, fontSize = 11.sp)
                        }
                        Box(modifier = Modifier.width(1.dp).height(28.dp).background(CyberCardBorder))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "${recentScans.size}", color = CyberYellow, fontWeight = FontWeight.Black, fontSize = 15.sp)
                            Text(text = "Scans", color = TextMuted, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Threat Alert Banner
        item {
            ThreatAlertBanner(
                title = "Zero-Day Mobile OTP Phishing Advisory",
                description = "Adversaries are actively deploying reverse-proxy login portals spoofing popular banks and messengers. Verify all incoming SMS requests independently.",
                severity = "HIGH",
                onClick = onNavigateToPhishing
            )
        }

        // Quick Actions Grid
        item {
            Text(
                text = "SECURITY CAPABILITIES",
                color = CyberCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = "Phishing Detector",
                        subtitle = "Analyze messages & SMS",
                        icon = Icons.Default.Security,
                        accentColor = CyberCyan,
                        modifier = Modifier.weight(1f),
                        testTag = "action_phishing_scan",
                        onClick = onNavigateToPhishing
                    )
                    QuickActionCard(
                        title = "URL Scanner",
                        subtitle = "Domain & link safety",
                        icon = Icons.Default.Language,
                        accentColor = CyberBlue,
                        modifier = Modifier.weight(1f),
                        testTag = "action_url_scan",
                        onClick = onNavigateToUrlScan
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = "Password Audit",
                        subtitle = "100% local entropy check",
                        icon = Icons.Default.Key,
                        accentColor = CyberGreen,
                        modifier = Modifier.weight(1f),
                        testTag = "action_password_check",
                        onClick = onNavigateToPassword
                    )
                    QuickActionCard(
                        title = "RAKUZZ AI",
                        subtitle = "Cybersecurity Assistant",
                        icon = Icons.Default.Psychology,
                        accentColor = CyberPurple,
                        modifier = Modifier.weight(1f),
                        testTag = "action_rakuzz_ai",
                        onClick = onNavigateToAiChat
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = "Cyber Academy",
                        subtitle = "Learn defense tactics",
                        icon = Icons.Default.School,
                        accentColor = CyberYellow,
                        modifier = Modifier.weight(1f),
                        testTag = "action_academy",
                        onClick = onNavigateToAcademy
                    )
                    QuickActionCard(
                        title = "Cyber Quiz",
                        subtitle = "Test your knowledge",
                        icon = Icons.Default.Quiz,
                        accentColor = CyberOrange,
                        modifier = Modifier.weight(1f),
                        testTag = "action_quiz",
                        onClick = onNavigateToQuiz
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = "Incident Help",
                        subtitle = "Breach & attack recovery",
                        icon = Icons.Default.Warning,
                        accentColor = CyberRed,
                        modifier = Modifier.weight(1f),
                        testTag = "action_incident_response",
                        onClick = onNavigateToIncident
                    )
                    QuickActionCard(
                        title = "Router Audit",
                        subtitle = "Wi-Fi firmware & settings",
                        icon = Icons.Default.Router,
                        accentColor = CyberCyan,
                        modifier = Modifier.weight(1f),
                        testTag = "action_router_audit",
                        onClick = onNavigateToRouterAudit
                    )
                }
            }
        }

        // Recent Threat Scans
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RECENT SCANS & REPORTS",
                    color = CyberCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        if (recentScans.isEmpty()) {
            item {
                CyberCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No Scans Recorded Yet",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Scan a suspicious text or URL above to generate your first threat report.",
                            color = TextMuted,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        } else {
            items(recentScans) { scan ->
                CyberCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = CyberCardBgElevated
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    when (scan.type) {
                                        "PHISHING" -> CyberCyan.copy(alpha = 0.15f)
                                        "URL" -> CyberBlue.copy(alpha = 0.15f)
                                        "ROUTER_AUDIT" -> CyberGreen.copy(alpha = 0.15f)
                                        else -> CyberRed.copy(alpha = 0.15f)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (scan.type) {
                                    "PHISHING" -> Icons.Default.Security
                                    "URL" -> Icons.Default.Language
                                    "ROUTER_AUDIT" -> Icons.Default.Router
                                    else -> Icons.Default.Warning
                                },
                                contentDescription = null,
                                tint = when (scan.type) {
                                    "PHISHING" -> CyberCyan
                                    "URL" -> CyberBlue
                                    "ROUTER_AUDIT" -> CyberGreen
                                    else -> CyberRed
                                },
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = scan.type.replace("_", " "),
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                RiskBadge(riskLevel = scan.riskLevel)
                            }
                            Text(
                                text = scan.targetInput,
                                color = TextSecondary,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault()).format(Date(scan.timestamp)),
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        }

                        IconButton(
                            onClick = { viewModel.deleteScanReport(scan.id) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextMuted, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "quick_action_card"
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, CyberCardBorder, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CyberCardBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(accentColor.copy(alpha = 0.15f))
                        .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
                }
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.6f))
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                color = TextMuted,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
