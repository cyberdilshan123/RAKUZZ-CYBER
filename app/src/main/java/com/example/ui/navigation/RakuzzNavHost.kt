package com.example.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.Lesson
import com.example.ui.screens.AcademyScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.IncidentResponseScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.LessonDetailScreen
import com.example.ui.screens.NetworkSecurityScreen
import com.example.ui.screens.PasswordCheckerScreen
import com.example.ui.screens.PhishingScannerScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.RouterAuditScreen
import com.example.ui.screens.SettingsPrivacyScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.CyberCardBg
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
import kotlinx.coroutines.launch

enum class ScreenRoute {
    SPLASH,
    AUTH,
    DASHBOARD,
    PHISHING_SCANNER,
    URL_SCANNER,
    PASSWORD_CHECKER,
    RAKUZZ_AI,
    ACADEMY,
    LESSON_DETAIL,
    QUIZ,
    LEADERBOARD,
    INCIDENT_RESPONSE,
    NETWORK_SECURITY,
    ROUTER_AUDIT,
    PROFILE,
    ADMIN_DASHBOARD,
    SETTINGS_PRIVACY
}

data class NavItem(
    val title: String,
    val route: ScreenRoute,
    val icon: ImageVector
)

@Composable
fun RakuzzApp() {
    val viewModel: CyberViewModel = viewModel()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    var currentScreen by remember { mutableStateOf(ScreenRoute.SPLASH) }
    var activeLesson by remember { mutableStateOf<Lesson?>(null) }

    val bottomNavItems = listOf(
        NavItem("Home", ScreenRoute.DASHBOARD, Icons.Default.Home),
        NavItem("Scanner", ScreenRoute.PHISHING_SCANNER, Icons.Default.Security),
        NavItem("Academy", ScreenRoute.ACADEMY, Icons.Default.School),
        NavItem("RAKUZZ AI", ScreenRoute.RAKUZZ_AI, Icons.Default.Psychology),
        NavItem("Profile", ScreenRoute.PROFILE, Icons.Default.Person)
    )

    when (currentScreen) {
        ScreenRoute.SPLASH -> {
            SplashScreen(onSplashFinished = { currentScreen = ScreenRoute.AUTH })
        }
        ScreenRoute.AUTH -> {
            AuthScreen(onAuthSuccess = { currentScreen = ScreenRoute.DASHBOARD })
        }
        else -> {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        drawerContainerColor = CyberDarkSurface,
                        modifier = Modifier.width(300.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(16.dp)
                        ) {
                            // Drawer Header
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF0F264A))
                                        .border(1.dp, CyberCyan, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Security, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(text = "RAKUZZ CYBER", color = TextPrimary, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                    Text(text = "DEFENSIVE OPERATIONS", color = CyberCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            HorizontalDivider(color = CyberCardBorder, modifier = Modifier.padding(vertical = 8.dp))

                            // Drawer Items
                            DrawerItemRow("Dashboard", Icons.Default.Home, CyberCyan, currentScreen == ScreenRoute.DASHBOARD) {
                                currentScreen = ScreenRoute.DASHBOARD
                                coroutineScope.launch { drawerState.close() }
                            }
                            DrawerItemRow("AI Phishing Detector", Icons.Default.Security, CyberCyan, currentScreen == ScreenRoute.PHISHING_SCANNER) {
                                currentScreen = ScreenRoute.PHISHING_SCANNER
                                coroutineScope.launch { drawerState.close() }
                            }
                            DrawerItemRow("URL & Link Scanner", Icons.Default.Language, CyberBlue, currentScreen == ScreenRoute.URL_SCANNER) {
                                currentScreen = ScreenRoute.URL_SCANNER
                                coroutineScope.launch { drawerState.close() }
                            }
                            DrawerItemRow("Password Entropy Audit", Icons.Default.Key, CyberGreen, currentScreen == ScreenRoute.PASSWORD_CHECKER) {
                                currentScreen = ScreenRoute.PASSWORD_CHECKER
                                coroutineScope.launch { drawerState.close() }
                            }
                            DrawerItemRow("RAKUZZ AI Assistant", Icons.Default.Psychology, CyberPurple, currentScreen == ScreenRoute.RAKUZZ_AI) {
                                currentScreen = ScreenRoute.RAKUZZ_AI
                                coroutineScope.launch { drawerState.close() }
                            }
                            DrawerItemRow("Cyber Academy", Icons.Default.School, CyberYellow, currentScreen == ScreenRoute.ACADEMY) {
                                currentScreen = ScreenRoute.ACADEMY
                                coroutineScope.launch { drawerState.close() }
                            }
                            DrawerItemRow("Cyber Quiz & Ranks", Icons.Default.Quiz, CyberOrange, currentScreen == ScreenRoute.QUIZ) {
                                currentScreen = ScreenRoute.QUIZ
                                coroutineScope.launch { drawerState.close() }
                            }
                            DrawerItemRow("Incident Response", Icons.Default.Warning, CyberRed, currentScreen == ScreenRoute.INCIDENT_RESPONSE) {
                                currentScreen = ScreenRoute.INCIDENT_RESPONSE
                                coroutineScope.launch { drawerState.close() }
                            }
                            DrawerItemRow("Router Security Audit", Icons.Default.Router, CyberCyan, currentScreen == ScreenRoute.ROUTER_AUDIT) {
                                currentScreen = ScreenRoute.ROUTER_AUDIT
                                coroutineScope.launch { drawerState.close() }
                            }
                            DrawerItemRow("Network Diagnostics", Icons.Default.Wifi, CyberBlue, currentScreen == ScreenRoute.NETWORK_SECURITY) {
                                currentScreen = ScreenRoute.NETWORK_SECURITY
                                coroutineScope.launch { drawerState.close() }
                            }
                            DrawerItemRow("SOC Telemetry & Admin", Icons.Default.AdminPanelSettings, CyberPurple, currentScreen == ScreenRoute.ADMIN_DASHBOARD) {
                                currentScreen = ScreenRoute.ADMIN_DASHBOARD
                                coroutineScope.launch { drawerState.close() }
                            }
                            DrawerItemRow("Privacy & Disclosures", Icons.Default.PrivacyTip, CyberGreen, currentScreen == ScreenRoute.SETTINGS_PRIVACY) {
                                currentScreen = ScreenRoute.SETTINGS_PRIVACY
                                coroutineScope.launch { drawerState.close() }
                            }
                        }
                    }
                }
            ) {
                Scaffold(
                    bottomBar = {
                        val showBottomBar = currentScreen in listOf(
                            ScreenRoute.DASHBOARD,
                            ScreenRoute.PHISHING_SCANNER,
                            ScreenRoute.ACADEMY,
                            ScreenRoute.RAKUZZ_AI,
                            ScreenRoute.PROFILE
                        )

                        if (showBottomBar) {
                            NavigationBar(
                                containerColor = CyberDarkSurface,
                                tonalElevation = 8.dp,
                                modifier = Modifier
                                    .border(androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder))
                                    .height(68.dp)
                            ) {
                                bottomNavItems.forEach { item ->
                                    val isSelected = currentScreen == item.route
                                    NavigationBarItem(
                                        selected = isSelected,
                                        onClick = { currentScreen = item.route },
                                        icon = {
                                            Icon(
                                                item.icon,
                                                contentDescription = item.title,
                                                tint = if (isSelected) CyberCyan else TextMuted,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = item.title,
                                                color = if (isSelected) CyberCyan else TextMuted,
                                                fontSize = 10.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            indicatorColor = Color(0xFF0C2448)
                                        ),
                                        modifier = Modifier.testTag("nav_${item.title.lowercase().replace(" ", "_")}")
                                    )
                                }
                            }
                        }
                    },
                    containerColor = CyberDarkBg
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentScreen) {
                            ScreenRoute.DASHBOARD -> DashboardScreen(
                                viewModel = viewModel,
                                onNavigateToPhishing = { currentScreen = ScreenRoute.PHISHING_SCANNER },
                                onNavigateToUrlScan = { currentScreen = ScreenRoute.URL_SCANNER },
                                onNavigateToPassword = { currentScreen = ScreenRoute.PASSWORD_CHECKER },
                                onNavigateToAiChat = { currentScreen = ScreenRoute.RAKUZZ_AI },
                                onNavigateToAcademy = { currentScreen = ScreenRoute.ACADEMY },
                                onNavigateToQuiz = { currentScreen = ScreenRoute.QUIZ },
                                onNavigateToIncident = { currentScreen = ScreenRoute.INCIDENT_RESPONSE },
                                onNavigateToRouterAudit = { currentScreen = ScreenRoute.ROUTER_AUDIT },
                                onNavigateToNetwork = { currentScreen = ScreenRoute.NETWORK_SECURITY },
                                onOpenDrawer = { coroutineScope.launch { drawerState.open() } }
                            )
                            ScreenRoute.PHISHING_SCANNER -> PhishingScannerScreen(
                                viewModel = viewModel,
                                onBack = { currentScreen = ScreenRoute.DASHBOARD }
                            )
                            ScreenRoute.URL_SCANNER -> com.example.ui.screens.UrlScannerScreen(
                                viewModel = viewModel,
                                onBack = { currentScreen = ScreenRoute.DASHBOARD }
                            )
                            ScreenRoute.PASSWORD_CHECKER -> PasswordCheckerScreen(
                                viewModel = viewModel,
                                onBack = { currentScreen = ScreenRoute.DASHBOARD }
                            )
                            ScreenRoute.RAKUZZ_AI -> com.example.ui.screens.RakuzzAiScreen(
                                viewModel = viewModel,
                                onBack = { currentScreen = ScreenRoute.DASHBOARD }
                            )
                            ScreenRoute.ACADEMY -> AcademyScreen(
                                viewModel = viewModel,
                                onSelectLesson = { lesson ->
                                    activeLesson = lesson
                                    currentScreen = ScreenRoute.LESSON_DETAIL
                                },
                                onBack = { currentScreen = ScreenRoute.DASHBOARD }
                            )
                            ScreenRoute.LESSON_DETAIL -> activeLesson?.let { lesson ->
                                LessonDetailScreen(
                                    lesson = lesson,
                                    viewModel = viewModel,
                                    onBack = { currentScreen = ScreenRoute.ACADEMY }
                                )
                            } ?: run { currentScreen = ScreenRoute.ACADEMY }
                            ScreenRoute.QUIZ -> QuizScreen(
                                viewModel = viewModel,
                                onNavigateToLeaderboard = { currentScreen = ScreenRoute.LEADERBOARD },
                                onBack = { currentScreen = ScreenRoute.DASHBOARD }
                            )
                            ScreenRoute.LEADERBOARD -> LeaderboardScreen(
                                viewModel = viewModel,
                                onBack = { currentScreen = ScreenRoute.QUIZ }
                            )
                            ScreenRoute.INCIDENT_RESPONSE -> IncidentResponseScreen(
                                viewModel = viewModel,
                                onBack = { currentScreen = ScreenRoute.DASHBOARD }
                            )
                            ScreenRoute.NETWORK_SECURITY -> NetworkSecurityScreen(
                                viewModel = viewModel,
                                onBack = { currentScreen = ScreenRoute.DASHBOARD }
                            )
                            ScreenRoute.ROUTER_AUDIT -> RouterAuditScreen(
                                viewModel = viewModel,
                                onBack = { currentScreen = ScreenRoute.DASHBOARD }
                            )
                            ScreenRoute.PROFILE -> ProfileScreen(
                                viewModel = viewModel,
                                onNavigateToAdmin = { currentScreen = ScreenRoute.ADMIN_DASHBOARD },
                                onNavigateToPrivacy = { currentScreen = ScreenRoute.SETTINGS_PRIVACY },
                                onLogout = { currentScreen = ScreenRoute.AUTH },
                                onBack = { currentScreen = ScreenRoute.DASHBOARD }
                            )
                            ScreenRoute.ADMIN_DASHBOARD -> AdminDashboardScreen(
                                viewModel = viewModel,
                                onBack = { currentScreen = ScreenRoute.PROFILE }
                            )
                            ScreenRoute.SETTINGS_PRIVACY -> SettingsPrivacyScreen(
                                onBack = { currentScreen = ScreenRoute.PROFILE }
                            )
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerItemRow(
    label: String,
    icon: ImageVector,
    accentColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) accentColor.copy(alpha = 0.15f) else Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isSelected) accentColor else TextSecondary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                color = if (isSelected) TextPrimary else TextSecondary,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 13.sp
            )
        }
    }
}
