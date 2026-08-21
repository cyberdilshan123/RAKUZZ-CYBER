package com.example.ui.screens

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberOutlineButton
import com.example.ui.components.CyberTextField
import com.example.ui.components.NeonButton
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

@Composable
fun PasswordCheckerScreen(
    viewModel: CyberViewModel,
    onBack: () -> Unit
) {
    val passwordInput by viewModel.passwordInput.collectAsState()
    val analysis by viewModel.passwordAnalysis.collectAsState()
    var isPasswordVisible by remember { mutableStateOf(false) }

    val strengthColor = when (analysis.strengthLevel) {
        "Very Strong" -> CyberGreen
        "Strong" -> CyberCyan
        "Moderate" -> CyberYellow
        "Weak" -> CyberOrange
        else -> CyberRed
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberDarkBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp)
    ) {
        // Top Header
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
                        text = "Password Entropy Audit",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "100% Local • Zero Network Leakage",
                        color = CyberGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Privacy Guarantee Badge
        item {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = CyberGreen.copy(alpha = 0.12f),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberGreen.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = CyberGreen, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Zero-Knowledge Guarantee: Password analysis runs purely on-device memory. Passwords are never sent to Gemini, APIs, cloud databases, or logs.",
                        color = TextPrimary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Password Input Box
        item {
            CyberCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "TEST PASSWORD STRENGTH",
                        color = CyberCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CyberTextField(
                            value = passwordInput,
                            onValueChange = { viewModel.onPasswordChanged(it) },
                            placeholder = "Type a test password or passphrase...",
                            leadingIcon = Icons.Default.Key,
                            modifier = Modifier.weight(1f),
                            testTag = "password_checker_input"
                        )
                    }

                    if (passwordInput.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            CyberOutlineButton(
                                text = "Clear Memory",
                                onClick = { viewModel.clearPassword() },
                                icon = Icons.Default.Delete,
                                borderColor = CyberRed,
                                textColor = CyberRed
                            )
                        }
                    }
                }
            }
        }

        // Strength Meter Card
        item {
            CyberCard(
                borderColor = strengthColor.copy(alpha = 0.7f),
                glowEffect = analysis.score >= 70,
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
                                text = "DEFENSIVE STRENGTH",
                                color = TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = analysis.strengthLevel,
                                color = strengthColor,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = strengthColor.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, strengthColor.copy(alpha = 0.7f))
                        ) {
                            Text(
                                text = "${analysis.score} / 100",
                                color = strengthColor,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    LinearProgressIndicator(
                        progress = { (analysis.score / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = strengthColor,
                        trackColor = Color(0xFF142240),
                        strokeCap = StrokeCap.Round
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Metrics Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = CyberCardBgElevated,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = "Crack Time Estimate", color = TextMuted, fontSize = 10.sp)
                                Text(
                                    text = analysis.estimatedCrackTime,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = CyberCardBgElevated,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = "Mathematical Entropy", color = TextMuted, fontSize = 10.sp)
                                Text(
                                    text = "${String.format("%.1f", analysis.entropyBits)} bits",
                                    color = CyberCyan,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Criteria Checklist
        item {
            CyberCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "PASSWORD CRITERIA CHECKLIST",
                        color = CyberCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    PasswordCheckItem(label = "Length (12+ characters for resilience)", isPassed = analysis.isLengthSufficient)
                    PasswordCheckItem(label = "Uppercase letters (A-Z)", isPassed = analysis.hasUppercase)
                    PasswordCheckItem(label = "Lowercase letters (a-z)", isPassed = analysis.hasLowercase)
                    PasswordCheckItem(label = "Numerical digits (0-9)", isPassed = analysis.hasDigits)
                    PasswordCheckItem(label = "Special symbols (!@#$%^&*)", isPassed = analysis.hasSymbols)
                    PasswordCheckItem(label = "No obvious dictionary words", isPassed = !analysis.hasCommonWords)
                    PasswordCheckItem(label = "No sequential patterns (e.g. 1234, qwerty)", isPassed = !analysis.hasSequentialPatterns)
                }
            }
        }

        // Recommendations
        item {
            CyberCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "RECOMMENDATIONS",
                        color = CyberGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    analysis.suggestions.forEach { suggestion ->
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
                            Text(text = suggestion, color = TextPrimary, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PasswordCheckItem(
    label: String,
    isPassed: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isPassed) Icons.Default.CheckCircle else Icons.Default.Close,
            contentDescription = null,
            tint = if (isPassed) CyberGreen else CyberRed,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            color = if (isPassed) TextPrimary else TextSecondary,
            fontSize = 12.sp
        )
    }
}

@Composable
fun RakuzzAiScreen(
    viewModel: CyberViewModel,
    onBack: () -> Unit
) {
    val chatMessages by viewModel.chatMessages.collectAsState()
    val isAiTyping by viewModel.isAiTyping.collectAsState()
    var inputQuery by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val promptSuggestions = listOf(
        "How to spot AI voice cloning scams?",
        "Explain Zero Trust Architecture",
        "What to do if I clicked a phishing link?",
        "How to secure home Wi-Fi router?"
    )

    LaunchedEffect(chatMessages.size, isAiTyping) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberDarkBg)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CyberDarkSurface)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberCardBg)
                        .border(1.dp, CyberCardBorder, RoundedCornerShape(8.dp))
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = CyberCyan)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "RAKUZZ AI",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = CyberPurple.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "CYBER DEFENDER",
                                color = CyberPurple,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "Motto: Protect • Detect • Educate",
                        color = CyberCyan,
                        fontSize = 11.sp
                    )
                }
            }

            IconButton(onClick = { viewModel.clearChat() }) {
                Icon(Icons.Default.Delete, contentDescription = "Clear Chat", tint = TextMuted)
            }
        }

        // Chat Stream
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            if (chatMessages.isEmpty()) {
                item {
                    CyberCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(CyberPurple.copy(alpha = 0.15f))
                                    .border(1.dp, CyberPurple, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Psychology, contentDescription = null, tint = CyberPurple, modifier = Modifier.size(30.dp))
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Welcome to RAKUZZ AI Assistant",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Ask me anything about cybersecurity threats, phishing defense, malware response, or system hardening.",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            items(chatMessages) { msg ->
                val isUser = msg.role == "user"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    if (!isUser) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(CyberPurple.copy(alpha = 0.2f))
                                .border(1.dp, CyberPurple, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = CyberPurple, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Surface(
                        shape = RoundedCornerShape(
                            topStart = 14.dp,
                            topEnd = 14.dp,
                            bottomStart = if (isUser) 14.dp else 2.dp,
                            bottomEnd = if (isUser) 2.dp else 14.dp
                        ),
                        color = if (isUser) Color(0xFF0C3866) else CyberCardBg,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isUser) CyberCyan.copy(alpha = 0.5f) else CyberCardBorder
                        ),
                        modifier = Modifier.fillMaxWidth(0.85f)
                    ) {
                        Text(
                            text = msg.text,
                            color = TextPrimary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            if (isAiTyping) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = CyberCyan,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "RAKUZZ AI analyzing threat vector...",
                            color = CyberCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Quick Suggestion Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(promptSuggestions) { prompt ->
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = CyberCardBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
                    modifier = Modifier.clickable {
                        viewModel.sendChatMessage(prompt)
                    }
                ) {
                    Text(
                        text = prompt,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Input Field
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CyberDarkSurface)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CyberTextField(
                value = inputQuery,
                onValueChange = { inputQuery = it },
                placeholder = "Ask RAKUZZ AI defensive question...",
                modifier = Modifier.weight(1f),
                testTag = "rakuzz_ai_input"
            )

            Spacer(modifier = Modifier.width(10.dp))

            IconButton(
                onClick = {
                    if (inputQuery.isNotBlank()) {
                        val q = inputQuery
                        inputQuery = ""
                        viewModel.sendChatMessage(q)
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CyberCyan)
                    .testTag("send_ai_message_button")
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color(0xFF040E20))
            }
        }
    }
}
