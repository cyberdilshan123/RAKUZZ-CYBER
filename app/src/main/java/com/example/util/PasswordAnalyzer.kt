package com.example.util

import kotlin.math.log2
import kotlin.math.pow

data class PasswordAnalysisResult(
    val score: Int, // 0 to 100
    val strengthLevel: String, // "Very Weak", "Weak", "Moderate", "Strong", "Very Strong"
    val entropyBits: Double,
    val estimatedCrackTime: String,
    val hasUppercase: Boolean,
    val hasLowercase: Boolean,
    val hasDigits: Boolean,
    val hasSymbols: Boolean,
    val isLengthSufficient: Boolean, // >= 12
    val hasSequentialPatterns: Boolean,
    val hasRepeatedPatterns: Boolean,
    val hasCommonWords: Boolean,
    val suggestions: List<String>
)

object PasswordAnalyzer {

    private val COMMON_PASSWORDS = setOf(
        "password", "123456", "123456789", "qwerty", "12345678", "111111", "12345",
        "1234567", "dragon", "welcome", "login", "admin", "secret", "football",
        "master", "monkey", "iloveyou", "princess", "sunshine", "shadow", "superman",
        "batman", "trustno1", "letmein", "starwars", "pass123", "cyber123"
    )

    private val SEQUENTIAL_STRINGS = listOf(
        "abcdefghijklmnopqrstuvwxyz",
        "01234567890",
        "qwertyuiop",
        "asdfghjkl",
        "zxcvbnm"
    )

    fun analyze(password: String): PasswordAnalysisResult {
        if (password.isEmpty()) {
            return PasswordAnalysisResult(
                score = 0,
                strengthLevel = "Very Weak",
                entropyBits = 0.0,
                estimatedCrackTime = "Instant",
                hasUppercase = false,
                hasLowercase = false,
                hasDigits = false,
                hasSymbols = false,
                isLengthSufficient = false,
                hasSequentialPatterns = false,
                hasRepeatedPatterns = false,
                hasCommonWords = false,
                suggestions = listOf("Enter a password to evaluate its defensive strength.")
            )
        }

        val len = password.length
        val hasUpper = password.any { it.isUpperCase() }
        val hasLower = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSymbol = password.any { !it.isLetterOrDigit() }

        var poolSize = 0
        if (hasLower) poolSize += 26
        if (hasUpper) poolSize += 26
        if (hasDigit) poolSize += 10
        if (hasSymbol) poolSize += 33

        val entropy = if (poolSize > 0) len * log2(poolSize.toDouble()) else 0.0

        val lowerPass = password.lowercase()
        val hasCommon = COMMON_PASSWORDS.any { lowerPass.contains(it) }
        val hasSequential = checkSequential(lowerPass)
        val hasRepeated = checkRepeated(password)

        var calculatedScore = 0

        // Length component
        when {
            len >= 16 -> calculatedScore += 35
            len >= 12 -> calculatedScore += 25
            len >= 8 -> calculatedScore += 15
            else -> calculatedScore += 5
        }

        // Diversity component
        var diversityCount = 0
        if (hasLower) diversityCount++
        if (hasUpper) diversityCount++
        if (hasDigit) diversityCount++
        if (hasSymbol) diversityCount++

        calculatedScore += diversityCount * 10 // Max 40

        // Entropy bonus
        if (entropy > 60) calculatedScore += 15
        if (entropy > 80) calculatedScore += 10

        // Penalties
        if (hasCommon) calculatedScore -= 30
        if (hasSequential) calculatedScore -= 15
        if (hasRepeated) calculatedScore -= 10
        if (len < 8) calculatedScore -= 20

        val finalScore = calculatedScore.coerceIn(5, 100)

        val strengthLevel = when {
            finalScore >= 85 -> "Very Strong"
            finalScore >= 70 -> "Strong"
            finalScore >= 50 -> "Moderate"
            finalScore >= 30 -> "Weak"
            else -> "Very Weak"
        }

        val crackTime = estimateCrackTime(entropy, hasCommon, len)

        val suggestions = mutableListOf<String>()
        if (len < 12) suggestions.add("Increase length to at least 12–16 characters (or use a 4-word passphrase).")
        if (!hasUpper) suggestions.add("Include uppercase letters (A-Z).")
        if (!hasLower) suggestions.add("Include lowercase letters (a-z).")
        if (!hasDigit) suggestions.add("Include numerical digits (0-9).")
        if (!hasSymbol) suggestions.add("Include special characters (!, @, #, $, etc.).")
        if (hasCommon) suggestions.add("Avoid common dictionary words and well-known dictionary terms.")
        if (hasSequential) suggestions.add("Remove sequential letter or keypad progressions (e.g., '1234', 'qwerty').")
        if (hasRepeated) suggestions.add("Reduce repetitive character clusters (e.g., 'aaaa', '1111').")

        if (suggestions.isEmpty()) {
            suggestions.add("Excellent password structure! Store it safely in an encrypted password manager.")
            suggestions.add("Ensure you enable Multi-Factor Authentication (MFA) on this account.")
        }

        return PasswordAnalysisResult(
            score = finalScore,
            strengthLevel = strengthLevel,
            entropyBits = entropy,
            estimatedCrackTime = crackTime,
            hasUppercase = hasUpper,
            hasLowercase = hasLower,
            hasDigits = hasDigit,
            hasSymbols = hasSymbol,
            isLengthSufficient = len >= 12,
            hasSequentialPatterns = hasSequential,
            hasRepeatedPatterns = hasRepeated,
            hasCommonWords = hasCommon,
            suggestions = suggestions
        )
    }

    private fun checkSequential(pass: String): Boolean {
        for (seq in SEQUENTIAL_STRINGS) {
            for (i in 0..pass.length - 3) {
                val sub = pass.substring(i, i + 3)
                if (seq.contains(sub) || seq.reversed().contains(sub)) {
                    return true
                }
            }
        }
        return false
    }

    private fun checkRepeated(pass: String): Boolean {
        for (i in 0..pass.length - 3) {
            if (pass[i] == pass[i + 1] && pass[i] == pass[i + 2]) {
                return true
            }
        }
        return false
    }

    private fun estimateCrackTime(entropy: Double, isCommon: Boolean, length: Int): String {
        if (isCommon || length < 6) return "Instant (< 1 millisecond)"
        return when {
            entropy < 28 -> "< 1 second"
            entropy < 36 -> "A few minutes"
            entropy < 45 -> "Several hours"
            entropy < 55 -> "A few months"
            entropy < 65 -> "10 to 50 years"
            entropy < 80 -> "Thousands of years"
            else -> "Millions of centuries"
        }
    }
}
