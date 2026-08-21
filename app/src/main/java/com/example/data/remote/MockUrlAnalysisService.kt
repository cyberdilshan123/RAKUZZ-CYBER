package com.example.data.remote

import com.example.util.MaliciousPatternType
import com.example.util.PatternSeverity
import com.example.util.UrlValidationResult
import com.example.util.UrlValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

data class MockScanStage(
    val stageIndex: Int,
    val totalStages: Int = 4,
    val currentStepName: String,
    val progressPercent: Float
)

data class DetailedUrlAudit(
    val scanResult: UrlScanResult,
    val category: String, // e.g. "Brand Impersonation Phishing", "Direct IP Exploitation", "Clean Corporate Domain"
    val domainAge: String,
    val sslIssuer: String,
    val dnsStatus: String,
    val detectedSignaturesCount: Int
)

class MockUrlAnalysisService {

    suspend fun analyzeUrl(
        url: String,
        onProgressUpdate: ((MockScanStage) -> Unit)? = null
    ): DetailedUrlAudit = withContext(Dispatchers.IO) {
        val validation: UrlValidationResult = UrlValidator.validate(url)

        // Simulate multi-stage security pipeline
        onProgressUpdate?.invoke(
            MockScanStage(
                stageIndex = 1,
                currentStepName = "Resolving Host DNS & WHOIS Registry...",
                progressPercent = 0.25f
            )
        )
        delay(250)

        onProgressUpdate?.invoke(
            MockScanStage(
                stageIndex = 2,
                currentStepName = "Auditing SSL/TLS Certificate Authority & Cipher...",
                progressPercent = 0.50f
            )
        )
        delay(300)

        onProgressUpdate?.invoke(
            MockScanStage(
                stageIndex = 3,
                currentStepName = "Evaluating Brand Typosquat & Homograph Distance...",
                progressPercent = 0.75f
            )
        )
        delay(250)

        onProgressUpdate?.invoke(
            MockScanStage(
                stageIndex = 4,
                currentStepName = "Cross-referencing Global Threat Intelligence Feeds...",
                progressPercent = 1.0f
            )
        )
        delay(200)

        val findings = mutableListOf<String>()
        val recommendations = mutableListOf<String>()

        var riskScore = validation.instantThreatScore
        var category = "Legitimate Web Property"
        var domainAge = "Established Domain (> 3 Years)"
        var sslIssuer = if (validation.isHttps) "Let's Encrypt / Cloudflare Edge TLS" else "None (Unencrypted HTTP)"
        var dnsStatus = "Standard Authoritative DNS"

        // Analyze validation flags and build comprehensive mock report
        if (validation.flags.isEmpty()) {
            riskScore = 5
            category = "Clean & Verified Web Destination"
            findings.add("Valid HTTPS protocol with active SSL/TLS handshake.")
            findings.add("Domain hostname matches standard reputable registration patterns.")
            findings.add("No homograph, typosquatting, or credential-harvesting tokens detected.")
            recommendations.add("Target destination shows standard security signatures.")
            recommendations.add("Always maintain standard cyber hygiene when submitting personal information.")
        } else {
            for (flag in validation.flags) {
                findings.add("${flag.title}: ${flag.description}")
            }

            // Categorize threat
            val hasTyposquat = validation.flags.any { it.type == MaliciousPatternType.BRAND_TYPOSQUAT }
            val hasRawIp = validation.flags.any { it.type == MaliciousPatternType.RAW_IP_HOST }
            val hasInsecureHttp = validation.flags.any { it.type == MaliciousPatternType.INSECURE_HTTP }
            val hasAuthDelimiter = validation.flags.any { it.type == MaliciousPatternType.AUTH_DELIMITER_OBFUSCATION }
            val hasRiskyTld = validation.flags.any { it.type == MaliciousPatternType.SUSPICIOUS_TLD }

            when {
                hasAuthDelimiter -> {
                    category = "Credential Obfuscation & Open Redirect Phish"
                    domainAge = "Newly Created / Disposable (Registered < 7 days ago)"
                    dnsStatus = "Fast-flux Dynamic DNS Server"
                    sslIssuer = "Self-signed / Untrusted CA"
                    riskScore = riskScore.coerceAtLeast(85)
                }
                hasTyposquat -> {
                    category = "Brand Impersonation & Credential Harvester"
                    domainAge = "Recently Registered Domain (Registered 12 days ago)"
                    dnsStatus = "Anonymous Offshore Name Server"
                    riskScore = riskScore.coerceAtLeast(80)
                }
                hasRawIp -> {
                    category = "Raw Host IP Exploitation / Admin Portal"
                    domainAge = "Non-DNS Bare IP Host"
                    dnsStatus = "No PTR / Reverse DNS Record"
                    riskScore = riskScore.coerceAtLeast(70)
                }
                hasRiskyTld && hasInsecureHttp -> {
                    category = "High-Risk Unencrypted Disposable Domain"
                    domainAge = "Newly Registered Domain"
                    dnsStatus = "Budget Bulk TLD Registrar"
                    riskScore = riskScore.coerceAtLeast(65)
                }
                hasInsecureHttp -> {
                    category = "Insecure Plaintext Transport Risk"
                    sslIssuer = "Missing SSL Certificate"
                    riskScore = riskScore.coerceAtLeast(40)
                }
                else -> {
                    category = "Suspicious Link with Behavioral Anomalies"
                }
            }

            // Recommendations based on severity
            if (riskScore >= 60) {
                recommendations.add("DO NOT enter login credentials, passwords, or payment cards on this URL.")
                recommendations.add("Close the browser tab immediately and avoid downloading any associated files.")
                recommendations.add("If you were redirected from an email or SMS, report the message as a phishing scam.")
            } else {
                recommendations.add("Exercise caution when interacting with non-standard domains or insecure protocols.")
                recommendations.add("Verify the URL directly through bookmark or official search engine.")
            }
        }

        val riskLevel = when {
            riskScore >= 75 -> "CRITICAL"
            riskScore >= 50 -> "HIGH"
            riskScore >= 25 -> "MEDIUM"
            else -> "SAFE"
        }

        val summary = when {
            riskScore >= 75 -> "CRITICAL THREAT: High probability of phishing or brand impersonation."
            riskScore >= 50 -> "HIGH RISK: Multiple suspicious and unsafe patterns detected."
            riskScore >= 25 -> "MODERATE CAUTION: Minor structural anomalies observed."
            else -> "SAFE: No malicious indicators detected in analyzed URL."
        }

        val scanResult = UrlScanResult(
            url = validation.formattedUrl,
            riskScore = riskScore,
            riskLevel = riskLevel,
            summary = summary,
            findings = findings,
            recommendations = recommendations,
            isHttps = validation.isHttps,
            domain = validation.domain,
            hasIpHost = validation.isIpHost,
            hasExcessiveSubdomains = validation.flags.any { it.type == MaliciousPatternType.EXCESSIVE_SUBDOMAINS },
            isAiAnalyzed = false
        )

        DetailedUrlAudit(
            scanResult = scanResult,
            category = category,
            domainAge = domainAge,
            sslIssuer = sslIssuer,
            dnsStatus = dnsStatus,
            detectedSignaturesCount = validation.flags.size
        )
    }
}
