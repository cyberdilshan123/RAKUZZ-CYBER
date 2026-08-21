package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lan
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.VpnLock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.data.remote.IncidentPlanResult
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberOutlineButton
import com.example.ui.components.CyberTextField
import com.example.ui.components.NeonButton
import com.example.ui.components.RiskBadge
import com.example.ui.components.ScanningIndicator
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
import com.example.ui.viewmodel.IncidentUiState

@Composable
fun IncidentResponseScreen(
    viewModel: CyberViewModel,
    onBack: () -> Unit
) {
    val incidentState by viewModel.incidentState.collectAsState()
    var incidentType by remember { mutableStateOf("Clicked Suspicious Phishing Link") }
    var incidentDetails by remember { mutableStateOf("I opened a link from an unknown SMS pretending to be my bank and entered my phone number before closing the tab.") }

    val presetIncidents = listOf(
        "Clicked Phishing Link" to "I clicked an email link claiming my Netflix account was suspended and submitted my email.",
        "Compromised Password" to "I accidentally entered my main master password on an unverified login form.",
        "Unrecognized Device Login" to "I received an alert that my account was accessed from an unknown location in another country.",
        "Suspected SIM Swap" to "My cellular phone lost network signal completely and I cannot receive SMS verification codes."
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
                        text = "Incident Response Coordinator",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Emergency Containment & Mitigation",
                        color = CyberRed,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Quick Presets
        item {
            Column {
                Text(
                    text = "EMERGENCY SCENARIOS",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(presetIncidents) { (type, desc) ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CyberCardBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
                            modifier = Modifier.clickable {
                                incidentType = type
                                incidentDetails = desc
                                viewModel.resetIncidentState()
                            }
                        ) {
                            Text(
                                text = type,
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

        // Incident Input
        item {
            CyberCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "DESCRIBE THE SECURITY EVENT",
                        color = CyberCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    CyberTextField(
                        value = incidentType,
                        onValueChange = { incidentType = it },
                        placeholder = "Incident Type (e.g., Malware popup)",
                        label = "Incident Summary",
                        testTag = "incident_type_input"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    CyberTextField(
                        value = incidentDetails,
                        onValueChange = { incidentDetails = it },
                        placeholder = "What happened? What information was entered or downloaded?",
                        label = "Incident Details",
                        isSingleLine = false,
                        maxLines = 4,
                        modifier = Modifier.height(100.dp),
                        testTag = "incident_details_input"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    NeonButton(
                        text = "Generate Emergency Containment Plan",
                        onClick = { viewModel.generateIncidentPlan(incidentType, incidentDetails) },
                        icon = Icons.Default.Warning,
                        isLoading = incidentState is IncidentUiState.Generating,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "generate_incident_plan_button"
                    )
                }
            }
        }

        if (incidentState is IncidentUiState.Generating) {
            item {
                ScanningIndicator(text = "Synthesizing Emergency Containment & Recovery Strategy...")
            }
        }

        if (incidentState is IncidentUiState.Success) {
            val plan = (incidentState as IncidentUiState.Success).plan
            item {
                IncidentPlanDisplayCard(plan = plan)
            }
        }
    }
}

@Composable
fun IncidentPlanDisplayCard(plan: IncidentPlanResult) {
    CyberCard(
        borderColor = CyberRed.copy(alpha = 0.8f),
        glowEffect = true,
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
                        text = "EMERGENCY PLAYBOOK",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = plan.incidentType,
                        color = CyberRed,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                RiskBadge(riskLevel = plan.severity)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Step 1: Immediate Containment
            Text(
                text = "1. IMMEDIATE CONTAINMENT ACTIONS (DO THIS NOW)",
                color = CyberRed,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            plan.immediateActions.forEach { action ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Default.PriorityHigh, contentDescription = null, tint = CyberRed, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = action, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Step 2: Containment
            Text(
                text = "2. ACCOUNT & CREDENTIAL CONTAINMENT",
                color = CyberOrange,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            plan.containmentSteps.forEach { step ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(text = "•", color = CyberOrange, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
                    Text(text = step, color = TextSecondary, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Step 3: Recovery
            Text(
                text = "3. SYSTEM RECOVERY & INTEGRITY VERIFICATION",
                color = CyberGreen,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            plan.recoverySteps.forEach { step ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CyberGreen, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = step, color = TextPrimary, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF24151B),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberRed.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = CyberRed, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = plan.emergencyContactsNotice,
                        color = TextPrimary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun NetworkSecurityScreen(
    viewModel: CyberViewModel,
    onBack: () -> Unit
) {
    val netState by viewModel.networkState.collectAsState()

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
                        text = "Network Security Diagnostics",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Connection Telemetry & Encryption",
                        color = CyberCyan,
                        fontSize = 11.sp
                    )
                }
            }
        }

        item {
            CyberCard(glowEffect = true, modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "NETWORK INTEGRITY RATING",
                                color = TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "${netState.securityRating} / 100",
                                color = CyberCyan,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CyberGreen.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberGreen)
                        ) {
                            Text(
                                text = "SECURE PROTOCOL",
                                color = CyberGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    NetworkInfoRow(label = "Active Interface", value = netState.connectionType, icon = Icons.Default.Wifi)
                    NetworkInfoRow(label = "Private Local IP", value = netState.localIp, icon = Icons.Default.Lan)
                    NetworkInfoRow(label = "Default Gateway", value = netState.gatewayIp, icon = Icons.Default.Router)
                    NetworkInfoRow(label = "Encrypted DNS Provider", value = netState.dnsServer, icon = Icons.Default.Lock)
                    NetworkInfoRow(label = "VPN Encrypted Tunnel", value = if (netState.isVpnActive) "Active (Encrypted)" else "Inactive", icon = Icons.Default.VpnLock)
                }
            }
        }

        item {
            CyberCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "NETWORK DEFENSE RECOMMENDATIONS",
                        color = CyberCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    netState.recommendations.forEach { rec ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CyberGreen, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = rec, color = TextPrimary, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    NeonButton(
                        text = "Refresh Network Diagnostics",
                        onClick = { viewModel.refreshNetworkDiagnostics() },
                        icon = Icons.Default.Refresh,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun NetworkInfoRow(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, color = TextSecondary, fontSize = 12.sp)
        }
        Text(text = value, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    }
    HorizontalDivider(color = CyberCardBorder.copy(alpha = 0.5f))
}

@Composable
fun RouterAuditScreen(
    viewModel: CyberViewModel,
    onBack: () -> Unit
) {
    val routerState by viewModel.routerState.collectAsState()

    var brand by remember { mutableStateOf(routerState.brand) }
    var model by remember { mutableStateOf(routerState.model) }
    var firmware by remember { mutableStateOf(routerState.firmwareVersion) }
    var protocol by remember { mutableStateOf(routerState.securityProtocol) }

    var passChanged by remember { mutableStateOf(routerState.hasChangedDefaultPassword) }
    var remoteDisabled by remember { mutableStateOf(routerState.hasDisabledRemoteManagement) }
    var upnpDisabled by remember { mutableStateOf(routerState.hasDisabledUpnp) }
    var guestIsolated by remember { mutableStateOf(routerState.hasGuestNetworkIsolated) }
    var autoUpdate by remember { mutableStateOf(routerState.hasAutoFirmwareUpdate) }

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
                        text = "Router Security Audit",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Wi-Fi & Gateway Hardening",
                        color = CyberCyan,
                        fontSize = 11.sp
                    )
                }
            }
        }

        item {
            CyberCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "ROUTER DETAILS",
                        color = CyberCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    CyberTextField(value = brand, onValueChange = { brand = it }, placeholder = "Manufacturer (e.g. Asus, TP-Link)", label = "Router Brand")
                    Spacer(modifier = Modifier.height(8.dp))
                    CyberTextField(value = model, onValueChange = { model = it }, placeholder = "Model (e.g. Archer AX50)", label = "Model Number")
                    Spacer(modifier = Modifier.height(8.dp))
                    CyberTextField(value = protocol, onValueChange = { protocol = it }, placeholder = "WPA3-Personal or WPA2-AES", label = "Wi-Fi Encryption")
                }
            }
        }

        item {
            CyberCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "HARDENING AUDIT CHECKLIST",
                        color = CyberCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    AuditCheckItem(
                        title = "Default Admin Password Changed",
                        subtitle = "Factory logins ('admin/admin') are easily exploited by Mirai botnets",
                        checked = passChanged,
                        onCheckedChange = { passChanged = it }
                    )

                    AuditCheckItem(
                        title = "Remote WAN Management Disabled",
                        subtitle = "Prevents outside internet access to router configuration console",
                        checked = remoteDisabled,
                        onCheckedChange = { remoteDisabled = it }
                    )

                    AuditCheckItem(
                        title = "UPnP (Universal Plug and Play) Disabled",
                        subtitle = "Prevents malware inside local LAN from silently punching firewall holes",
                        checked = upnpDisabled,
                        onCheckedChange = { upnpDisabled = it }
                    )

                    AuditCheckItem(
                        title = "Guest Wi-Fi Network for IoT Devices",
                        subtitle = "Isolates smart TVs, cameras, and IoT gadgets from personal computers",
                        checked = guestIsolated,
                        onCheckedChange = { guestIsolated = it }
                    )

                    AuditCheckItem(
                        title = "Automatic Security Firmware Updates Enabled",
                        subtitle = "Ensures zero-day router exploits are patched automatically",
                        checked = autoUpdate,
                        onCheckedChange = { autoUpdate = it }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    NeonButton(
                        text = "Calculate Router Audit Score",
                        onClick = {
                            viewModel.updateRouterAudit(
                                brand, model, firmware, protocol,
                                passChanged, remoteDisabled, upnpDisabled, guestIsolated, autoUpdate
                            )
                        },
                        icon = Icons.Default.Router,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        if (routerState.auditCompleted) {
            item {
                CyberCard(
                    borderColor = if (routerState.auditScore >= 80) CyberGreen else CyberYellow,
                    glowEffect = true,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "AUDIT RESULT", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    text = "${routerState.brand} ${routerState.model}",
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (routerState.auditScore >= 80) CyberGreen.copy(alpha = 0.15f) else CyberYellow.copy(alpha = 0.15f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (routerState.auditScore >= 80) CyberGreen else CyberYellow)
                            ) {
                                Text(
                                    text = "${routerState.auditScore} / 100",
                                    color = if (routerState.auditScore >= 80) CyberGreen else CyberYellow,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (routerState.auditScore >= 80) "Router posture meets industry hardening standards." else "Action required: Several high-risk vulnerabilities are exposed in your router settings.",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AuditCheckItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onCheckedChange(!checked) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = CyberCyan,
                uncheckedColor = TextMuted,
                checkmarkColor = Color(0xFF040E20)
            )
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(text = subtitle, color = TextMuted, fontSize = 11.sp)
        }
    }
}
