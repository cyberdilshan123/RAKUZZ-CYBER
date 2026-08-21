package com.example.data.remote

import com.example.BuildConfig
import com.example.data.local.entity.ChatMessageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.URI
import java.util.Locale

data class PhishingScanResult(
    val riskScore: Int,
    val riskLevel: String, // "SAFE", "LOW", "MEDIUM", "HIGH", "CRITICAL"
    val summary: String,
    val findings: List<String>,
    val recommendations: List<String>,
    val urgencyLevel: String,
    val isAiAnalyzed: Boolean
)

data class UrlScanResult(
    val url: String,
    val riskScore: Int,
    val riskLevel: String,
    val summary: String,
    val findings: List<String>,
    val recommendations: List<String>,
    val isHttps: Boolean,
    val domain: String,
    val hasIpHost: Boolean,
    val hasExcessiveSubdomains: Boolean,
    val isAiAnalyzed: Boolean
)

data class IncidentPlanResult(
    val incidentType: String,
    val severity: String,
    val immediateActions: List<String>,
    val containmentSteps: List<String>,
    val recoverySteps: List<String>,
    val preventionChecklist: List<String>,
    val emergencyContactsNotice: String
)

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val difficulty: String
)

class CyberAiEngine {

    private val geminiService = RetrofitClient.geminiService

    suspend fun analyzePhishing(messageText: String): PhishingScanResult = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (!apiKey.isNullOrBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = """
                    You are RAKUZZ CYBER AI Phishing Analysis Engine.
                    Analyze this suspicious message text:
                    \"\"\"$messageText\"\"\"

                    Evaluate urgency tactics, financial manipulation, impersonation, fake credential requests, generic scam patterns, suspicious links, and social engineering.
                    Return ONLY a valid raw JSON object without markdown fences, with these exact keys:
                    {
                      "riskScore": (integer between 0 and 100),
                      "riskLevel": ("SAFE" | "LOW" | "MEDIUM" | "HIGH" | "CRITICAL"),
                      "summary": "one concise analytical sentence",
                      "findings": ["finding 1", "finding 2", ...],
                      "recommendations": ["safety step 1", "safety step 2", ...],
                      "urgencyLevel": ("None" | "Low" | "Medium" | "High" | "Extreme")
                    }
                """.trimIndent()

                val request = GeminiRequest(
                    contents = listOf(
                        GeminiContent(
                            parts = listOf(GeminiPart(text = prompt)),
                            role = "user"
                        )
                    ),
                    generationConfig = GeminiGenerationConfig(temperature = 0.2f)
                )

                val response = geminiService.generateContent(apiKey, request)
                val rawText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""
                val cleanJson = extractJson(rawText)
                if (cleanJson.isNotEmpty()) {
                    val jsonObj = JSONObject(cleanJson)
                    val findingsArr = jsonObj.optJSONArray("findings") ?: JSONArray()
                    val recsArr = jsonObj.optJSONArray("recommendations") ?: JSONArray()

                    val findings = mutableListOf<String>()
                    for (i in 0 until findingsArr.length()) findings.add(findingsArr.getString(i))

                    val recommendations = mutableListOf<String>()
                    for (i in 0 until recsArr.length()) recommendations.add(recsArr.getString(i))

                    return@withContext PhishingScanResult(
                        riskScore = jsonObj.optInt("riskScore", 65),
                        riskLevel = jsonObj.optString("riskLevel", "MEDIUM").uppercase(Locale.US),
                        summary = jsonObj.optString("summary", "Automated threat indicators detected in message payload."),
                        findings = findings.ifEmpty { listOf("Suspicious linguistic patterns detected", "Sender identity unverified") },
                        recommendations = recommendations.ifEmpty { listOf("Do not click unexpected links", "Verify sender out-of-band") },
                        urgencyLevel = jsonObj.optString("urgencyLevel", "High"),
                        isAiAnalyzed = true
                    )
                }
            } catch (e: Exception) {
                // Fallback to local heuristic engine
            }
        }

        // Local Heuristic Phishing Engine
        return@withContext runLocalPhishingHeuristics(messageText)
    }

    suspend fun analyzeUrl(urlInput: String): UrlScanResult = withContext(Dispatchers.IO) {
        val trimmed = urlInput.trim()
        val formattedUrl = if (!trimmed.startsWith("http://") && !trimmed.startsWith("https://")) {
            "https://$trimmed"
        } else trimmed

        val isHttps = formattedUrl.startsWith("https://", ignoreCase = true)
        val domain = extractDomain(formattedUrl)
        val isIp = isIpAddress(domain)
        val subdomains = domain.split(".")
        val hasExcessiveSubdomains = subdomains.size > 3

        val apiKey = BuildConfig.GEMINI_API_KEY
        if (!apiKey.isNullOrBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = """
                    You are RAKUZZ CYBER URL Analysis Engine.
                    Analyze this URL for cybersecurity threats:
                    URL: "$formattedUrl"
                    Domain: "$domain"
                    HTTPS: $isHttps

                    Check for typosquatting, credential harvesting, brand impersonation, obfuscation, suspicious TLDs, and malicious redirect patterns.
                    Return ONLY a valid raw JSON object:
                    {
                      "riskScore": (integer between 0 and 100),
                      "riskLevel": ("SAFE" | "LOW" | "MEDIUM" | "HIGH" | "CRITICAL"),
                      "summary": "concise security assessment sentence",
                      "findings": ["finding 1", ...],
                      "recommendations": ["rec 1", ...]
                    }
                """.trimIndent()

                val request = GeminiRequest(
                    contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)), role = "user")),
                    generationConfig = GeminiGenerationConfig(temperature = 0.2f)
                )
                val response = geminiService.generateContent(apiKey, request)
                val rawText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""
                val cleanJson = extractJson(rawText)
                if (cleanJson.isNotEmpty()) {
                    val jsonObj = JSONObject(cleanJson)
                    val findingsArr = jsonObj.optJSONArray("findings") ?: JSONArray()
                    val recsArr = jsonObj.optJSONArray("recommendations") ?: JSONArray()

                    val findings = mutableListOf<String>()
                    for (i in 0 until findingsArr.length()) findings.add(findingsArr.getString(i))
                    val recs = mutableListOf<String>()
                    for (i in 0 until recsArr.length()) recs.add(recsArr.getString(i))

                    return@withContext UrlScanResult(
                        url = formattedUrl,
                        riskScore = jsonObj.optInt("riskScore", 45),
                        riskLevel = jsonObj.optString("riskLevel", "MEDIUM").uppercase(Locale.US),
                        summary = jsonObj.optString("summary", "URL evaluated with automated behavioral heuristics."),
                        findings = findings,
                        recommendations = recs,
                        isHttps = isHttps,
                        domain = domain,
                        hasIpHost = isIp,
                        hasExcessiveSubdomains = hasExcessiveSubdomains,
                        isAiAnalyzed = true
                    )
                }
            } catch (e: Exception) {
                // Fallback to local heuristic
            }
        }

        return@withContext runLocalUrlHeuristics(formattedUrl, domain, isHttps, isIp, hasExcessiveSubdomains)
    }

    suspend fun chatWithRakuzzAi(userMessage: String, history: List<ChatMessageEntity>): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (!apiKey.isNullOrBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val systemInstruction = GeminiContent(
                    parts = listOf(
                        GeminiPart(
                            text = """
                                You are RAKUZZ AI, an elite defensive cybersecurity assistant.
                                Your motto is "Protect • Detect • Educate".
                                Provide concise, accurate, actionable advice on cyber defense, phishing detection, malware mitigation, safe browsing, password security, network hardening, and incident recovery.
                                STRICT DEFENSIVE RULES:
                                - Never provide malware code, exploit payloads, brute-force scripts, or hacking instructions.
                                - If asked to attack or exploit, politely pivot to defensive hardening and vulnerability remediation.
                                - Use clear formatting with bullet points and bold headers. Keep tone professional, authoritative, and helpful.
                            """.trimIndent()
                        )
                    )
                )

                val chatContents = mutableListOf<GeminiContent>()
                val recentHistory = history.takeLast(6)
                for (msg in recentHistory) {
                    val role = if (msg.role == "user") "user" else "model"
                    chatContents.add(GeminiContent(parts = listOf(GeminiPart(text = msg.text)), role = role))
                }
                chatContents.add(GeminiContent(parts = listOf(GeminiPart(text = userMessage)), role = "user"))

                val request = GeminiRequest(
                    contents = chatContents,
                    generationConfig = GeminiGenerationConfig(temperature = 0.5f),
                    systemInstruction = systemInstruction
                )

                val response = geminiService.generateContent(apiKey, request)
                val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (!text.isNullOrBlank()) {
                    return@withContext text
                }
            } catch (e: Exception) {
                // Fallback to local expert knowledge base
            }
        }

        return@withContext getLocalAiResponse(userMessage)
    }

    suspend fun generateIncidentPlan(incidentType: String, details: String): IncidentPlanResult = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (!apiKey.isNullOrBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = """
                    You are RAKUZZ CYBER Incident Response Coordinator.
                    Incident: "$incidentType"
                    Details: "$details"

                    Generate an actionable emergency containment and recovery plan.
                    Return ONLY valid raw JSON:
                    {
                      "severity": ("LOW" | "MEDIUM" | "HIGH" | "CRITICAL"),
                      "immediateActions": ["action 1", "action 2", ...],
                      "containmentSteps": ["step 1", "step 2", ...],
                      "recoverySteps": ["step 1", "step 2", ...],
                      "preventionChecklist": ["check 1", "check 2", ...],
                      "emergencyContactsNotice": "Notice on reporting to bank/IT/authorities"
                    }
                """.trimIndent()

                val request = GeminiRequest(
                    contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)), role = "user")),
                    generationConfig = GeminiGenerationConfig(temperature = 0.3f)
                )
                val response = geminiService.generateContent(apiKey, request)
                val cleanJson = extractJson(response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "")
                if (cleanJson.isNotEmpty()) {
                    val jsonObj = JSONObject(cleanJson)
                    fun jsonArrayToList(arr: JSONArray?): List<String> {
                        if (arr == null) return emptyList()
                        val list = mutableListOf<String>()
                        for (i in 0 until arr.length()) list.add(arr.getString(i))
                        return list
                    }
                    return@withContext IncidentPlanResult(
                        incidentType = incidentType,
                        severity = jsonObj.optString("severity", "HIGH"),
                        immediateActions = jsonArrayToList(jsonObj.optJSONArray("immediateActions")),
                        containmentSteps = jsonArrayToList(jsonObj.optJSONArray("containmentSteps")),
                        recoverySteps = jsonArrayToList(jsonObj.optJSONArray("recoverySteps")),
                        preventionChecklist = jsonArrayToList(jsonObj.optJSONArray("preventionChecklist")),
                        emergencyContactsNotice = jsonObj.optString("emergencyContactsNotice", "Contact official institution support channels immediately.")
                    )
                }
            } catch (e: Exception) {
                // Fallback to local incident playbook
            }
        }

        return@withContext getLocalIncidentPlaybook(incidentType, details)
    }

    private fun runLocalPhishingHeuristics(message: String): PhishingScanResult {
        val lower = message.lowercase(Locale.US)
        var score = 15
        val findings = mutableListOf<String>()
        val recommendations = mutableListOf<String>()
        var urgency = "Low"

        // Urgency triggers
        if (lower.contains("urgent") || lower.contains("immediately") || lower.contains("24 hours") || lower.contains("suspended") || lower.contains("terminated")) {
            score += 25
            urgency = "High"
            findings.add("Artificial urgency detected: Pressures victim to act rapidly without thinking")
        }

        // Financial triggers
        if (lower.contains("bank") || lower.contains("transfer") || lower.contains("wire") || lower.contains("refund") || lower.contains("crypto") || lower.contains("lottery") || lower.contains("prize")) {
            score += 20
            findings.add("Financial transaction baiting identified in message body")
        }

        // Credential triggers
        if (lower.contains("password") || lower.contains("otp") || lower.contains("verify your account") || lower.contains("click here to verify") || lower.contains("security alert")) {
            score += 30
            findings.add("Credential or security code solicitation attempt detected")
        }

        // Suspicious link indicators
        if (lower.contains("http://") || lower.contains("bit.ly") || lower.contains("tinyurl") || lower.contains(".xyz") || lower.contains(".top") || lower.contains(".ru")) {
            score += 25
            findings.add("Potentially untrusted or shortened URL embedded")
        }

        // Impersonation
        if (lower.contains("paypal") || lower.contains("amazon") || lower.contains("apple id") || lower.contains("google security") || lower.contains("netflix") || lower.contains("fedex") || lower.contains("dhl")) {
            score += 15
            findings.add("High-target brand name reference found (frequent phishing target)")
        }

        score = score.coerceIn(10, 98)
        val level = when {
            score >= 80 -> "CRITICAL"
            score >= 60 -> "HIGH"
            score >= 35 -> "MEDIUM"
            else -> "LOW"
        }

        recommendations.add("Do NOT click any embedded links or open attachments")
        recommendations.add("Never share one-time passwords (OTPs) or account credentials")
        recommendations.add("Verify requests directly using known official phone numbers or apps")
        if (score >= 60) {
            recommendations.add("Block sender and report as phishing/spam in your messaging client")
        }

        val summary = if (score >= 60) {
            "High probability of social engineering or phishing attack detected."
        } else {
            "Message contains minor risk factors; proceed with caution."
        }

        return PhishingScanResult(
            riskScore = score,
            riskLevel = level,
            summary = summary,
            findings = findings.ifEmpty { listOf("No obvious automated phishing indicators, but remain vigilant.") },
            recommendations = recommendations,
            urgencyLevel = urgency,
            isAiAnalyzed = false
        )
    }

    private fun runLocalUrlHeuristics(
        url: String,
        domain: String,
        isHttps: Boolean,
        isIp: Boolean,
        hasExcessiveSubdomains: Boolean
    ): UrlScanResult {
        var score = 10
        val findings = mutableListOf<String>()
        val recs = mutableListOf<String>()

        if (!isHttps) {
            score += 35
            findings.add("Insecure HTTP protocol: Traffic is unencrypted and vulnerable to interception (MitM)")
            recs.add("Avoid entering credentials, personal data, or payment information on non-HTTPS sites")
        } else {
            findings.add("HTTPS encryption enabled (Note: phishing sites can also acquire SSL certificates)")
        }

        if (isIp) {
            score += 45
            findings.add("Direct IP address used as hostname: Standard legitimate services use named domains")
            recs.add("Do not visit raw IP addresses unless connecting to verified local network management")
        }

        if (hasExcessiveSubdomains) {
            score += 25
            findings.add("Excessive subdomain depth: Commonly used to camouflage target domain in address bar")
            recs.add("Check the rightmost registered domain name carefully before trusting")
        }

        val suspiciousTlds = listOf(".xyz", ".top", ".buzz", ".work", ".cfd", ".gq", ".ml", ".tk", ".fit", ".rest")
        if (suspiciousTlds.any { domain.endsWith(it, ignoreCase = true) }) {
            score += 25
            findings.add("Suspicious top-level domain (.${domain.substringAfterLast('.')}) associated with high spam volumes")
        }

        if (domain.contains("paypal") || domain.contains("login") || domain.contains("secure") || domain.contains("verify") || domain.contains("account") || domain.contains("bank") || domain.contains("support")) {
            val isLegit = domain == "paypal.com" || domain.endsWith(".paypal.com") || domain == "google.com" || domain == "apple.com"
            if (!isLegit) {
                score += 30
                findings.add("Domain contains security/financial keywords that may attempt typosquatting or brand spoofing")
            }
        }

        if (url.contains("@") || url.contains("%20") || url.contains("==")) {
            score += 20
            findings.add("URL contains obfuscated characters, authentication delimiters, or unusual encoding")
        }

        score = score.coerceIn(5, 95)
        val level = when {
            score >= 75 -> "CRITICAL"
            score >= 50 -> "HIGH"
            score >= 25 -> "MEDIUM"
            else -> "SAFE"
        }

        recs.add("Inspect domain spelling for sneaky homograph/character substitutions (e.g., '0' instead of 'o')")
        recs.add("Use a trusted multi-engine URL lookup tool if in doubt before opening")

        return UrlScanResult(
            url = url,
            riskScore = score,
            riskLevel = level,
            summary = "Local heuristic audit calculated a risk rating of $score/100 ($level).",
            findings = findings,
            recommendations = recs,
            isHttps = isHttps,
            domain = domain,
            hasIpHost = isIp,
            hasExcessiveSubdomains = hasExcessiveSubdomains,
            isAiAnalyzed = false
        )
    }

    private fun getLocalAiResponse(query: String): String {
        val q = query.lowercase(Locale.US)
        return when {
            q.contains("phish") || q.contains("scam") -> """
                **How to Identify & Defend Against Phishing:**
                
                • **Check the Sender Email Address:** Don't trust the display name alone. Look for slight misspellings (e.g., `support@paypa1-security.com`).
                • **Watch for Extreme Urgency:** Scammers rely on panic ("Account suspended in 1 hour!", "Immediate payment required") to bypass rational thinking.
                • **Inspect Links Before Clicking:** Long-press or hover to reveal the true destination URL.
                • **Never Share 2FA Codes:** Legitimate companies will **never** ask for your SMS/authenticator verification codes.
                • **Defense Tip:** When in doubt, navigate to the official website manually rather than clicking the email link.
            """.trimIndent()

            q.contains("password") || q.contains("passphrase") -> """
                **Password Security & Best Practices:**
                
                • **Length Over Complexity:** A 16-character passphrase (e.g., `Sunset#Guitar#Bicycle99`) is exponentially stronger and easier to remember than `Tr0b4!`.
                • **Never Reuse Passwords:** If one service suffers a data breach, attackers execute credential stuffing across all your other accounts.
                • **Use a Reputable Password Manager:** Store unique, randomized 20+ character passwords for every single website.
                • **Enforce Multi-Factor Authentication (MFA):** Prefer hardware security keys (FIDO2/WebAuthn) or Authenticator apps over SMS.
            """.trimIndent()

            q.contains("malware") || q.contains("ransomware") || q.contains("virus") -> """
                **Malware & Ransomware Defense Guide:**
                
                • **The 3-2-1 Backup Strategy:** Keep 3 copies of your important data on 2 different media types, with 1 copy stored offline or off-site.
                • **Patch Immediately:** Keep OS, browsers, and applications updated. Over 80% of successful exploits target vulnerabilities with existing patches.
                • **Principle of Least Privilege:** Never run day-to-day computing tasks from an Administrator/Root account.
                • **Disable Office Macros:** Never enable macros or execute scripts from email attachments.
            """.trimIndent()

            q.contains("router") || q.contains("wifi") || q.contains("network") -> """
                **Home Router & Wi-Fi Hardening Checklist:**
                
                • **Change Default Admin Password:** Never leave the factory router admin login (`admin / admin`).
                • **Use WPA3-Personal or WPA2-AES:** Disable legacy WEP, WPA-TKIP, and WPS (Wi-Fi Protected Setup PIN).
                • **Disable Remote Management:** Turn off router management access from the WAN/Internet side.
                • **Segment IoT Devices:** Put smart TVs, cameras, and IoT gadgets on an isolated Guest Network.
                • **Update Router Firmware:** Regularly apply router security patches to prevent Mirai and botnet infections.
            """.trimIndent()

            q.contains("zero trust") -> """
                **What is Zero Trust Security?**
                
                Zero Trust is a security paradigm built on the fundamental principle: **"Never trust, always verify."**
                
                **Three Core Pillars:**
                1. **Verify Explicitly:** Always authenticate and authorize based on all available data points (identity, location, device health, service).
                2. **Use Least Privileged Access:** Limit user access with Just-In-Time and Just-Enough-Access (JIT/JEA).
                3. **Assume Breach:** Minimize blast radius by segmenting networks, encrypting all end-to-end sessions, and using analytics for real-time threat detection.
            """.trimIndent()

            else -> """
                **RAKUZZ CYBER AI Defense Assistant**
                
                I am ready to help you analyze security threats and strengthen your digital defenses!
                
                **Topics I can help with:**
                • Phishing & Social Engineering Detection
                • Password & MFA Hardening
                • Network & Router Security Audits
                • Malware, Ransomware & Spyware Mitigation
                • Incident Response & Account Recovery Playbooks
                • Data Privacy & Secure Web Browsing
                
                What specific security topic or incident would you like guidance on?
            """.trimIndent()
        }
    }

    private fun getLocalIncidentPlaybook(incident: String, details: String): IncidentPlanResult {
        return IncidentPlanResult(
            incidentType = incident,
            severity = "HIGH",
            immediateActions = listOf(
                "Disconnect the affected device from Wi-Fi and mobile data immediately to prevent command & control exfiltration.",
                "Using a separate secure device, change the password of any potentially exposed account.",
                "Revoke active sessions and log out of all connected devices from your account security dashboard."
            ),
            containmentSteps = listOf(
                "Enable 2-Step Verification (prefer Authenticator App or Security Key over SMS).",
                "Review authorized third-party OAuth apps connected to your email and social accounts; revoke unfamiliar access.",
                "Check email forwarding rules and filter configurations for hidden exfiltration rules."
            ),
            recoverySteps = listOf(
                "Run a full antimalware/antivirus scan using a reputable security suite.",
                "Monitor banking transactions and credit reports for unauthorized activity.",
                "Download and verify your multi-factor backup recovery codes in a safe offline location."
            ),
            preventionChecklist = listOf(
                "Adopt a dedicated password manager for unique passwords.",
                "Enroll in breach notification alerts (e.g., Have I Been Pwned).",
                "Ensure automatic operating system and browser security updates are enabled."
            ),
            emergencyContactsNotice = "If financial credentials or identities are compromised, contact your bank fraud department and local cybercrime reporting portal immediately."
        )
    }

    private fun extractDomain(urlStr: String): String {
        return try {
            val uri = URI(urlStr)
            val host = uri.host ?: urlStr.substringAfter("://").substringBefore("/").substringBefore(":")
            host.removePrefix("www.")
        } catch (e: Exception) {
            urlStr.substringAfter("://").substringBefore("/").substringBefore(":")
        }
    }

    private fun isIpAddress(host: String): Boolean {
        val regex = Regex("^(\\d{1,3}\\.){3}\\d{1,3}$")
        return regex.matches(host)
    }

    private fun extractJson(raw: String): String {
        val trimmed = raw.trim()
        val startIdx = trimmed.indexOf('{')
        val lastIdx = trimmed.lastIndexOf('}')
        return if (startIdx != -1 && lastIdx != -1 && lastIdx > startIdx) {
            trimmed.substring(startIdx, lastIdx + 1)
        } else ""
    }
}
