package com.example.util

import java.net.URI
import java.util.Locale

enum class PatternSeverity {
    INFO,
    WARNING,
    DANGER,
    CRITICAL
}

enum class MaliciousPatternType {
    INSECURE_HTTP,
    RAW_IP_HOST,
    BRAND_TYPOSQUAT,
    SUSPICIOUS_TLD,
    EXCESSIVE_SUBDOMAINS,
    AUTH_DELIMITER_OBFUSCATION,
    OBFUSCATED_ENCODING,
    SUSPICIOUS_KEYWORD_PATH,
    NON_STANDARD_PORT,
    EXTREME_LENGTH,
    SYNTAX_ERROR
}

data class MaliciousPatternFlag(
    val type: MaliciousPatternType,
    val severity: PatternSeverity,
    val title: String,
    val description: String,
    val tag: String
)

data class UrlValidationResult(
    val rawInput: String,
    val formattedUrl: String,
    val isValidFormat: Boolean,
    val errorMessage: String? = null,
    val scheme: String = "https",
    val domain: String = "",
    val path: String = "/",
    val port: Int? = null,
    val isHttps: Boolean = true,
    val isIpHost: Boolean = false,
    val flags: List<MaliciousPatternFlag> = emptyList(),
    val instantThreatScore: Int = 0, // 0 - 100
    val instantThreatLevel: String = "CLEAN" // "CLEAN", "LOW RISK", "SUSPICIOUS", "HIGH RISK", "CRITICAL"
)

object UrlValidator {

    private val SUSPICIOUS_TLDS = setOf(
        "xyz", "top", "buzz", "work", "cfd", "gq", "ml", "tk", "fit",
        "rest", "click", "link", "download", "racing", "surf", "stream",
        "loan", "cam", "icu", "monster", "quest", "zip", "mov", "country",
        "kim", "gdn", "mom", "bid", "men", "party", "review", "trade"
    )

    private val TARGET_BRANDS = listOf(
        "paypal" to listOf("paypa1", "p4ypal", "paypai", "pay-pal", "paypal-auth", "paypal-security", "paypal-update"),
        "google" to listOf("g00gle", "go0gle", "g0ogle", "google-security", "google-verify", "google-auth"),
        "apple" to listOf("app1e", "apple-id", "appleid-verify", "icloud-verify", "apple-support"),
        "amazon" to listOf("amaz0n", "amazn", "amazon-security", "amazon-update", "amazon-prime-gift"),
        "netflix" to listOf("netfl1x", "netflx", "netflix-verify", "netflix-update", "netflix-billing"),
        "microsoft" to listOf("micros0ft", "micr0soft", "ms-verify", "office365-login", "microsoft-support"),
        "facebook" to listOf("faceb00k", "fb-security", "meta-verify", "instagram-verify"),
        "bank" to listOf("chase-secure", "wellsfarg0", "bofa-verify", "citibank-auth", "hsbc-online", "bank-login"),
        "crypto" to listOf("binance-claim", "metamask-verify", "coinbase-auth", "trustwallet-airdrop", "crypto-giveaway", "ledger-update")
    )

    private val SUSPICIOUS_PATH_KEYWORDS = listOf(
        "login", "signin", "sign-in", "log-in", "verify", "verification",
        "account", "update", "banking", "secure", "authenticate", "auth",
        "password", "passcode", "otp", "token", "wallet", "claim",
        "confirm", "unlock", "restore", "suspended", "session"
    )

    private val IPV4_REGEX = Regex("^(\\d{1,3}\\.){3}\\d{1,3}$")

    fun validate(input: String): UrlValidationResult {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) {
            return UrlValidationResult(
                rawInput = "",
                formattedUrl = "",
                isValidFormat = false,
                errorMessage = "URL input is empty"
            )
        }

        // Basic space / formatting check
        if (trimmed.contains(" ") && !trimmed.contains("://")) {
            return UrlValidationResult(
                rawInput = trimmed,
                formattedUrl = trimmed,
                isValidFormat = false,
                errorMessage = "URL cannot contain spaces",
                flags = listOf(
                    MaliciousPatternFlag(
                        type = MaliciousPatternType.SYNTAX_ERROR,
                        severity = PatternSeverity.WARNING,
                        title = "Invalid URL Syntax",
                        description = "Target address contains unencoded spaces.",
                        tag = "SYNTAX"
                    )
                ),
                instantThreatScore = 30,
                instantThreatLevel = "LOW RISK"
            )
        }

        val formattedUrl = if (!trimmed.startsWith("http://", ignoreCase = true) && !trimmed.startsWith("https://", ignoreCase = true)) {
            "https://$trimmed"
        } else {
            trimmed
        }

        val isHttps = formattedUrl.startsWith("https://", ignoreCase = true)
        val scheme = if (isHttps) "https" else "http"

        val flags = mutableListOf<MaliciousPatternFlag>()
        var calculatedScore = 5

        // Check 1: Insecure HTTP Protocol
        if (!isHttps) {
            calculatedScore += 25
            flags.add(
                MaliciousPatternFlag(
                    type = MaliciousPatternType.INSECURE_HTTP,
                    severity = PatternSeverity.WARNING,
                    title = "Insecure Plaintext HTTP",
                    description = "Connection lacks SSL/TLS encryption. Traffic is vulnerable to Man-in-the-Middle (MitM) eavesdropping.",
                    tag = "UNENCRYPTED"
                )
            )
        }

        // Check 2: Obfuscated user-info delimiter (@ symbol in URL)
        if (formattedUrl.contains("@")) {
            calculatedScore += 35
            flags.add(
                MaliciousPatternFlag(
                    type = MaliciousPatternType.AUTH_DELIMITER_OBFUSCATION,
                    severity = PatternSeverity.CRITICAL,
                    title = "Credential Obfuscation (@ Delimiter)",
                    description = "Contains '@' symbol before host. Browsers ignore text before '@' and route directly to the attacker server.",
                    tag = "OBFUSCATION"
                )
            )
        }

        // Parse Host & Path
        var domain = ""
        var path = "/"
        var port: Int? = null

        try {
            val uri = URI(formattedUrl.replace(" ", "%20"))
            domain = uri.host ?: extractDomainManually(formattedUrl)
            path = uri.path ?: "/"
            if (uri.port != -1) {
                port = uri.port
            }
        } catch (e: Exception) {
            domain = extractDomainManually(formattedUrl)
        }

        domain = domain.lowercase(Locale.US).removePrefix("www.")

        // Check 3: Raw IP Address Host
        val isIp = IPV4_REGEX.matches(domain) || domain.matches(Regex("^\\[?[0-9a-fA-F:]+\\]?$"))
        if (isIp) {
            calculatedScore += 30
            flags.add(
                MaliciousPatternFlag(
                    type = MaliciousPatternType.RAW_IP_HOST,
                    severity = PatternSeverity.DANGER,
                    title = "Direct IP Address Host",
                    description = "URL bypasses standard DNS domain names, connecting directly to a numerical IP address ($domain).",
                    tag = "RAW IP"
                )
            )
        }

        // Check 4: Suspicious TLD
        val tld = domain.substringAfterLast('.', "")
        if (tld.isNotEmpty() && SUSPICIOUS_TLDS.contains(tld)) {
            calculatedScore += 25
            flags.add(
                MaliciousPatternFlag(
                    type = MaliciousPatternType.SUSPICIOUS_TLD,
                    severity = PatternSeverity.WARNING,
                    title = "High-Abuse Top Level Domain (.$tld)",
                    description = "Top-level domain '.$tld' has a statistically elevated association with malware distribution and disposable phishing campaigns.",
                    tag = "RISKY TLD"
                )
            )
        }

        // Check 5: Brand Typosquatting / Impersonation
        var detectedBrandSquat = false
        for ((brand, typos) in TARGET_BRANDS) {
            if (domain.contains(brand)) {
                // If it contains the legitimate brand name but is not the official root domain
                val isOfficial = isOfficialDomain(domain, brand)
                if (!isOfficial) {
                    calculatedScore += 30
                    flags.add(
                        MaliciousPatternFlag(
                            type = MaliciousPatternType.BRAND_TYPOSQUAT,
                            severity = PatternSeverity.CRITICAL,
                            title = "Brand Impersonation ($brand)",
                            description = "Domain incorporates '$brand' name on an unauthorized third-party host ($domain).",
                            tag = "SPOOFING"
                        )
                    )
                    detectedBrandSquat = true
                    break
                }
            } else {
                // Check for typo variations (e.g., paypa1, amaz0n)
                for (typo in typos) {
                    if (domain.contains(typo)) {
                        calculatedScore += 35
                        flags.add(
                            MaliciousPatternFlag(
                                type = MaliciousPatternType.BRAND_TYPOSQUAT,
                                severity = PatternSeverity.CRITICAL,
                                title = "Typosquatting Detected ($typo for $brand)",
                                description = "Character substitution visually mimicking official '$brand' website.",
                                tag = "TYPOSQUAT"
                            )
                        )
                        detectedBrandSquat = true
                        break
                    }
                }
                if (detectedBrandSquat) break
            }
        }

        // Check 6: Excessive Subdomains / Camouflage Stacking
        val dotCount = domain.count { it == '.' }
        if (dotCount >= 3) {
            calculatedScore += 20
            flags.add(
                MaliciousPatternFlag(
                    type = MaliciousPatternType.EXCESSIVE_SUBDOMAINS,
                    severity = PatternSeverity.WARNING,
                    title = "Subdomain Camouflage Stacking",
                    description = "Domain has $dotCount sub-levels. Attackers often prefix reputable names to push malicious roots offscreen.",
                    tag = "SUBDOMAINS"
                )
            )
        }

        // Check 7: Suspicious Keywords in Path/Parameters
        val lowerFull = formattedUrl.lowercase(Locale.US)
        val matchedKeywords = SUSPICIOUS_PATH_KEYWORDS.filter { keyword ->
            lowerFull.contains("/$keyword") || lowerFull.contains("-$keyword") || lowerFull.contains("=$keyword")
        }
        if (matchedKeywords.size >= 2) {
            calculatedScore += 15
            flags.add(
                MaliciousPatternFlag(
                    type = MaliciousPatternType.SUSPICIOUS_KEYWORD_PATH,
                    severity = PatternSeverity.WARNING,
                    title = "High-Risk Action Keywords (${matchedKeywords.take(2).joinToString(", ")})",
                    description = "Path references sensitive authentication or recovery workflows.",
                    tag = "HARVESTING"
                )
            )
        }

        // Check 8: Non-standard web ports
        if (port != null && port != 80 && port != 443 && port != 8080 && port != 8443) {
            calculatedScore += 15
            flags.add(
                MaliciousPatternFlag(
                    type = MaliciousPatternType.NON_STANDARD_PORT,
                    severity = PatternSeverity.INFO,
                    title = "Non-Standard Port (:$port)",
                    description = "Destination binds to uncommon network port :$port instead of standard 443/80.",
                    tag = "PORT :$port"
                )
            )
        }

        // Check 9: Obfuscated encoding or double slashes in path
        if (formattedUrl.contains("%20") || formattedUrl.contains("%2E") || formattedUrl.contains("//") && formattedUrl.indexOf("//") != formattedUrl.lastIndexOf("//")) {
            calculatedScore += 15
            flags.add(
                MaliciousPatternFlag(
                    type = MaliciousPatternType.OBFUSCATED_ENCODING,
                    severity = PatternSeverity.WARNING,
                    title = "Obfuscated Characters / Hex Escapes",
                    description = "Payload employs URL-encoding or repeated slash sequences to evade signature filters.",
                    tag = "ENCODING"
                )
            )
        }

        // Check 10: Extreme Length
        if (formattedUrl.length > 120) {
            calculatedScore += 10
            flags.add(
                MaliciousPatternFlag(
                    type = MaliciousPatternType.EXTREME_LENGTH,
                    severity = PatternSeverity.INFO,
                    title = "Excessive URL Length (${formattedUrl.length} chars)",
                    description = "Unusually long URL containing potential tracking parameters or base64 token blobs.",
                    tag = "LONG URL"
                )
            )
        }

        val finalScore = calculatedScore.coerceIn(5, 99)
        val level = when {
            finalScore >= 75 -> "CRITICAL"
            finalScore >= 50 -> "HIGH RISK"
            finalScore >= 30 -> "SUSPICIOUS"
            finalScore >= 15 -> "LOW RISK"
            else -> "CLEAN"
        }

        return UrlValidationResult(
            rawInput = trimmed,
            formattedUrl = formattedUrl,
            isValidFormat = true,
            errorMessage = null,
            scheme = scheme,
            domain = domain.ifEmpty { "unknown" },
            path = path,
            port = port,
            isHttps = isHttps,
            isIpHost = isIp,
            flags = flags,
            instantThreatScore = finalScore,
            instantThreatLevel = level
        )
    }

    private fun extractDomainManually(url: String): String {
        return try {
            val afterScheme = url.substringAfter("://", url)
            val hostPort = afterScheme.substringBefore("/").substringBefore("?").substringBefore("#")
            hostPort.substringBefore(":")
        } catch (e: Exception) {
            "unknown"
        }
    }

    private fun isOfficialDomain(domain: String, brand: String): Boolean {
        val officialRoots = mapOf(
            "paypal" to listOf("paypal.com", "paypal.me"),
            "google" to listOf("google.com", "google.co.uk", "google.de", "google.fr", "youtube.com"),
            "apple" to listOf("apple.com", "icloud.com"),
            "amazon" to listOf("amazon.com", "amazon.co.uk", "amazon.de", "aws.amazon.com"),
            "netflix" to listOf("netflix.com"),
            "microsoft" to listOf("microsoft.com", "live.com", "office.com", "azure.com"),
            "facebook" to listOf("facebook.com", "fb.com", "instagram.com", "meta.com")
        )

        val roots = officialRoots[brand] ?: return false
        return roots.any { domain == it || domain.endsWith(".$it") }
    }
}
