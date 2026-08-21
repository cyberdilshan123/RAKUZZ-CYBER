package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberOutlineButton
import com.example.ui.components.CyberTextField
import com.example.ui.components.NeonButton
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberDarkBg
import com.example.ui.theme.CyberDarkSurface
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.6f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "splashScale"
    )
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "splashAlpha"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2200)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        CyberDarkBg,
                        Color(0xFF0A122B),
                        CyberDarkBg
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .scale(scale)
                .padding(32.dp)
        ) {
            // Logo Image
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFF0C1736))
                    .border(2.dp, CyberCyan.copy(alpha = alpha), RoundedCornerShape(28.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rakuzz_cyber_logo),
                    contentDescription = "RAKUZZ CYBER Logo",
                    modifier = Modifier
                        .size(110.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "RAKUZZ CYBER",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CyberCyan.copy(alpha = 0.12f),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f))
            ) {
                Text(
                    text = "Protect • Detect • Educate",
                    color = CyberCyan,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            CircularProgressIndicator(
                color = CyberCyan,
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = "Initializing Threat Intelligence Matrix...",
                color = TextMuted,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Login, 1: Register, 2: Forgot
    var email by remember { mutableStateOf("defender@rakuzz.cyber") }
    var password by remember { mutableStateOf("Rakuzz#Secure2026!") }
    var fullName by remember { mutableStateOf("Security Specialist") }
    var confirmPassword by remember { mutableStateOf("Rakuzz#Secure2026!") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberDarkBg)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0D1C3D))
                        .border(1.dp, CyberCyan, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.rakuzz_cyber_logo),
                        contentDescription = null,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "RAKUZZ CYBER",
                        color = TextPrimary,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Protect • Detect • Educate",
                        color = CyberCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            CyberCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    // Auth Tabs
                    TabRow(
                        selectedTabIndex = selectedTab.coerceAtMost(1),
                        containerColor = Color.Transparent,
                        contentColor = CyberCyan,
                        indicator = { tabPositions ->
                            if (selectedTab < tabPositions.size) {
                                TabRowDefaults.SecondaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab.coerceAtMost(1)]),
                                    color = CyberCyan
                                )
                            }
                        },
                        divider = { HorizontalDivider(color = CyberCardBorder) }
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0; errorMessage = null; successMessage = null },
                            text = { Text("Log In", fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1; errorMessage = null; successMessage = null },
                            text = { Text("Register", fontWeight = FontWeight.Bold) }
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    if (errorMessage != null) {
                        Surface(
                            color = CyberRed.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberRed),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Text(
                                text = errorMessage!!,
                                color = CyberRed,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    if (successMessage != null) {
                        Surface(
                            color = CyberGreen.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberGreen),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Text(
                                text = successMessage!!,
                                color = CyberGreen,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    if (selectedTab == 1) {
                        // Register Fields
                        CyberTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            placeholder = "Full Name",
                            label = "Full Name",
                            leadingIcon = Icons.Default.Person,
                            testTag = "register_name_input"
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    CyberTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "defender@company.com",
                        label = "Email Address",
                        leadingIcon = Icons.Default.Email,
                        testTag = "auth_email_input"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (selectedTab != 2) {
                        CyberTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = "••••••••••••",
                            label = "Password",
                            leadingIcon = Icons.Default.Lock,
                            testTag = "auth_password_input"
                        )
                    }

                    if (selectedTab == 1) {
                        Spacer(modifier = Modifier.height(10.dp))
                        CyberTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            placeholder = "••••••••••••",
                            label = "Confirm Password",
                            leadingIcon = Icons.Default.Lock,
                            testTag = "auth_confirm_password_input"
                        )
                    }

                    if (selectedTab == 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { selectedTab = 2; errorMessage = null }) {
                                Text("Forgot Password?", color = CyberCyan, fontSize = 12.sp)
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    NeonButton(
                        text = when (selectedTab) {
                            1 -> "Create Defender Account"
                            2 -> "Send Reset Link"
                            else -> "Authorize & Log In"
                        },
                        onClick = {
                            if (email.isBlank() || (!email.contains("@"))) {
                                errorMessage = "Please enter a valid email address."
                                return@NeonButton
                            }
                            if (selectedTab == 1 && password != confirmPassword) {
                                errorMessage = "Passwords do not match."
                                return@NeonButton
                            }
                            if (selectedTab == 2) {
                                successMessage = "Password reset instructions sent to $email."
                                return@NeonButton
                            }

                            isLoading = true
                            onAuthSuccess()
                        },
                        isLoading = isLoading,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "auth_submit_button"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CyberOutlineButton(
                        text = "Continue with Google Identity",
                        onClick = { onAuthSuccess() },
                        icon = Icons.Default.Shield,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "google_auth_button"
                    )
                }
            }
        }
    }
}
