package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.CyberCardBgElevated
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberOrange
import com.example.ui.theme.CyberRed
import com.example.ui.theme.CyberYellow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.MaliciousPatternFlag
import com.example.util.PatternSeverity
import com.example.util.UrlValidationResult

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ValidatedUrlInputField(
    value: String,
    onValueChange: (String) -> Unit,
    validationResult: UrlValidationResult,
    onScanTriggered: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "validated_url_input"
) {
    val borderColor by animateColorAsState(
        targetValue = when {
            value.isEmpty() -> CyberCardBorder
            !validationResult.isValidFormat -> CyberRed
            validationResult.instantThreatScore >= 60 -> CyberRed
            validationResult.instantThreatScore >= 30 -> CyberOrange
            validationResult.instantThreatScore >= 15 -> CyberYellow
            else -> CyberGreen
        },
        label = "borderAnim"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        // Status & Protocol Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TARGET URL / HOST VALIDATION",
                color = CyberCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            if (value.isNotEmpty() && validationResult.isValidFormat) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = borderColor.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, borderColor.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(borderColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (validationResult.flags.isEmpty()) "SYNTAX CLEAN" else "${validationResult.flags.size} PATTERNS FLAGGED",
                            color = borderColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Input Field with Dynamic Border and Icons
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = "e.g., https://paypal-security-auth.xyz/login",
                    color = TextMuted,
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                if (value.isNotEmpty() && validationResult.isValidFormat) {
                    Icon(
                        imageVector = if (validationResult.isHttps) Icons.Default.Lock else Icons.Default.LockOpen,
                        contentDescription = if (validationResult.isHttps) "HTTPS" else "HTTP Insecure",
                        tint = if (validationResult.isHttps) CyberGreen else CyberRed,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "URL",
                        tint = CyberCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            trailingIcon = {
                if (value.isNotEmpty()) {
                    IconButton(
                        onClick = { onValueChange("") },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear URL",
                            tint = TextMuted
                        )
                    }
                }
            },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onScanTriggered() }
            ),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CyberCardBgElevated,
                unfocusedContainerColor = CyberCardBg,
                focusedBorderColor = borderColor,
                unfocusedBorderColor = borderColor.copy(alpha = 0.7f),
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = CyberCyan
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag)
        )

        // Error message if format is invalid
        if (!validationResult.isValidFormat && validationResult.errorMessage != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, start = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ErrorOutline,
                    contentDescription = null,
                    tint = CyberRed,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = validationResult.errorMessage,
                    color = CyberRed,
                    fontSize = 11.sp
                )
            }
        }

        // Live Malicious Pattern Badges
        AnimatedVisibility(
            visible = value.isNotEmpty() && validationResult.flags.isNotEmpty(),
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Text(
                    text = "DETECTED MALICIOUS PATTERNS (PRE-FLIGHT AUDIT):",
                    color = TextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    validationResult.flags.forEach { flag ->
                        PatternFlagChip(flag = flag)
                    }
                }
            }
        }
    }
}

@Composable
fun PatternFlagChip(
    flag: MaliciousPatternFlag,
    modifier: Modifier = Modifier
) {
    val (chipColor, chipBg) = when (flag.severity) {
        PatternSeverity.CRITICAL -> CyberRed to CyberRed.copy(alpha = 0.15f)
        PatternSeverity.DANGER -> CyberOrange to CyberOrange.copy(alpha = 0.15f)
        PatternSeverity.WARNING -> CyberYellow to CyberYellow.copy(alpha = 0.15f)
        PatternSeverity.INFO -> CyberCyan to CyberCyan.copy(alpha = 0.12f)
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = chipBg,
        border = BorderStroke(1.dp, chipColor.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (flag.severity) {
                    PatternSeverity.CRITICAL, PatternSeverity.DANGER -> Icons.Default.Warning
                    PatternSeverity.WARNING -> Icons.Default.Warning
                    PatternSeverity.INFO -> Icons.Default.Info
                },
                contentDescription = null,
                tint = chipColor,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "${flag.tag}: ${flag.title}",
                color = chipColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
