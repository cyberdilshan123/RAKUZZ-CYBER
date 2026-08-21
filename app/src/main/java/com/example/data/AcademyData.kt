package com.example.data

data class Lesson(
    val id: String,
    val title: String,
    val category: String, // "Beginner", "Intermediate", "Advanced"
    val readTimeMinutes: Int,
    val overview: String,
    val keyPoints: List<String>,
    val fullContent: String,
    val realWorldExample: String,
    val defenseChecklist: List<String>,
    val quizQuestion: String,
    val quizOptions: List<String>,
    val correctOptionIndex: Int,
    val quizExplanation: String
)

data class CyberQuizQuestion(
    val id: String,
    val category: String,
    val difficulty: String, // "Beginner", "Intermediate", "Advanced"
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

object AcademyData {

    val lessons: List<Lesson> = listOf(
        Lesson(
            id = "lesson_phishing_101",
            title = "What is Phishing & Social Engineering?",
            category = "Beginner",
            readTimeMinutes = 4,
            overview = "Understand how cyber adversaries manipulate human psychology to steal credentials and financial assets.",
            keyPoints = listOf(
                "Phishing exploits human trust, urgency, and fear rather than software vulnerabilities.",
                "Spear phishing targets specific individuals with tailored personal data.",
                "Smishing (SMS) and Vishing (Voice) are mobile-focused social engineering variants."
            ),
            fullContent = """
                Phishing remains the #1 initial access vector for cyber attacks worldwide. Rather than breaking through firewall cryptography, attackers convince authorized users to voluntarily surrender credentials or execute malicious payloads.

                Key Attack Vectors:
                1. Mass Phishing: Generic lures sent to millions (e.g., fake package delivery, tax refund notices).
                2. Spear Phishing: Highly researched messages targeting specific executives or employees using OSINT (Open Source Intelligence).
                3. Business Email Compromise (BEC): Attackers impersonate company executives to authorize fraudulent wire transfers.
                4. QR Code Phishing (Quishing): Malicious QR codes in emails or physical spaces redirecting to credential harvesting portals.

                Red Flag Indicators:
                • Artificial urgency ("Account terminating in 24 hours!")
                • Mismatched domain names (e.g., `service@paypa1-update.com`)
                • Inconsistent greetings and unexpected attachments (.exe, .scr, .iso, .zip)
                • Direct requests for one-time verification passcodes or secret keys.
            """.trimIndent(),
            realWorldExample = "A major tech company employee received an SMS claiming their IT portal login had expired. The link directed to a spoofed single-sign-on (SSO) landing page, bypassing SMS 2FA with real-time reverse proxying.",
            defenseChecklist = listOf(
                "Inspect the full sender address domain, not just the display name.",
                "Never click links in unprompted security or financial alerts.",
                "Navigate directly to the official app or website URL in a fresh browser tab.",
                "Report suspicious communications to your organization's security team."
            ),
            quizQuestion = "Which element in an email is the strongest indicator of a phishing attempt?",
            quizOptions = listOf(
                "The email was sent during business hours",
                "Extreme urgency demanding immediate password reset via an unverified link",
                "The email uses proper corporate logos",
                "The email is addressed from a registered domain"
            ),
            correctOptionIndex = 1,
            quizExplanation = "Urgency tactics combined with third-party links are classic social engineering hallmarks designed to prevent victims from verifying claims."
        ),
        Lesson(
            id = "lesson_password_fortress",
            title = "Password Security & Multi-Factor Authentication",
            category = "Beginner",
            readTimeMinutes = 5,
            overview = "Master password entropy, passphrases, and why hardware keys surpass SMS-based authentication.",
            keyPoints = listOf(
                "Length creates exponential entropy compared to simple character substitution.",
                "Credential stuffing automates breached password attempts across thousands of websites.",
                "FIDO2 / Passkeys provide cryptographically bound, phishing-resistant authentication."
            ),
            fullContent = """
                Traditional passwords have failed the modern threat landscape. Attackers utilize GPU clusters calculating billions of hashes per second to brute-force short passwords.

                Entropy and Passphrases:
                A 16-character passphrase composed of random words (e.g., `orbit-cactus-frozen-violin`) provides over 70 bits of entropy, taking centuries to crack even on supercomputers.

                MFA Hierarchy (Weakest to Strongest):
                1. SMS / Voice Call: Vulnerable to SIM-swapping, SS7 interception, and social engineering.
                2. Authenticator Apps (TOTP): Generates 6-digit rolling codes; safe from SIM-swapping but vulnerable to real-time phishing proxies.
                3. Push Notification with Number Matching: Mitigates MFA fatigue prompt bombing.
                4. Hardware Security Keys / Passkeys (FIDO2 / WebAuthn): Cryptographic challenge-response bound to the exact origin domain. Immune to remote phishing!
            """.trimIndent(),
            realWorldExample = "In 2022, attackers compromised thousands of employee accounts by executing MFA bombing (sending 50+ push notifications at 3:00 AM) until the exhausted victim clicked 'Approve'.",
            defenseChecklist = listOf(
                "Never reuse passwords across different online services.",
                "Adopt a dedicated password manager to generate 20+ character random strings.",
                "Upgrade from SMS verification to TOTP Authenticator apps or Passkeys.",
                "Store offline emergency recovery codes in a secure, fireproof location."
            ),
            quizQuestion = "Why are FIDO2 Passkeys superior to 6-digit SMS verification codes?",
            quizOptions = listOf(
                "They use shorter passwords",
                "They are cryptographically bound to the website domain, making them phishing-immune",
                "They require an active cellular phone plan",
                "They never require biometric authentication"
            ),
            correctOptionIndex = 1,
            quizExplanation = "Passkeys use public-key cryptography tied to the domain in the browser, preventing users from accidentally submitting credentials to spoofed sites."
        ),
        Lesson(
            id = "lesson_malware_anatomy",
            title = "Malware, Spyware & Trojan Anatomy",
            category = "Intermediate",
            readTimeMinutes = 6,
            overview = "Explore the operational lifecycles of modern malicious software, infostealers, and remote access trojans (RATs).",
            keyPoints = listOf(
                "Infostealers target browser sqlite cookies, saved passwords, and crypto wallets.",
                "Living-off-the-land binaries (LOLBins) abuse legitimate OS tools to evade detection.",
                "Code signing certificate theft allows malware to pose as verified software."
            ),
            fullContent = """
                Modern malware is modular, stealthy, and highly specialized. Cybercrime syndicates operate under Malware-as-a-Service (MaaS) business models.

                Common Malware Classifications:
                • Infostealers (e.g., RedLine, Lumma): Scrapes browser local storage, session cookies (bypassing MFA), Telegram sessions, and crypto wallet extensions.
                • Remote Access Trojans (RATs): Provides continuous hidden desktop streaming, keylogging, microphone/camera eavesdropping, and file exfiltration.
                • Rootkits & Bootkits: Hooks into the kernel or UEFI firmware, rendering the malware invisible to standard user-space antivirus tools.
                • Droppers & Loaders: Small encrypted payloads that establish a foothold and download heavy secondary malware stages.

                Defense in Depth:
                Antivirus alone is insufficient against zero-day polymorphic binaries. Modern endpoints employ Endpoint Detection & Response (EDR) to monitor behavioral anomalies in memory and process creation trees.
            """.trimIndent(),
            realWorldExample = "A game mod downloaded from an unauthorized forum contained an obfuscated infostealer that extracted active Discord and Google session cookies, hijacking accounts without triggering password alerts.",
            defenseChecklist = listOf(
                "Download software exclusively from verified official vendor repositories.",
                "Keep operating system, drivers, and browser software updated.",
                "Avoid cracked software, keygens, and pirated media.",
                "Monitor background startup items and active running services."
            ),
            quizQuestion = "How do Infostealers bypass Multi-Factor Authentication (MFA)?",
            quizOptions = listOf(
                "By cracking the encryption of the telecom provider",
                "By extracting active session tokens and browser authentication cookies from disk",
                "By guessing the user's secret security questions",
                "By disabling the router's DNS cache"
            ),
            correctOptionIndex = 1,
            quizExplanation = "Session cookies authenticate active logged-in sessions. If an infostealer copies valid session cookies to the attacker's machine, the attacker can impersonate the user without entering credentials or MFA."
        ),
        Lesson(
            id = "lesson_ransomware_defense",
            title = "Ransomware Operations & Disaster Recovery",
            category = "Intermediate",
            readTimeMinutes = 6,
            overview = "Deconstruct double-extortion ransomware campaigns and implement the resilient 3-2-1 backup paradigm.",
            keyPoints = listOf(
                "Modern ransomware performs double extortion: encrypting data and threatening public leaks.",
                "Attackers spend days or weeks inside networks before triggering the encryption detonator.",
                "Immutable offline backups are the only guaranteed recovery path against ransomware."
            ),
            fullContent = """
                Ransomware has evolved from automated lockscreen scripts into sophisticated human-operated campaigns.

                The Attack Lifecycle:
                1. Initial Access: Phishing, compromised VPN credentials, or unpatched public servers.
                2. Lateral Movement: Harvesting credentials using tools like Mimikatz and traversing Active Directory.
                3. Data Exfiltration: Stealing gigabytes of sensitive intellectual property and customer records.
                4. Backup Destruction: Locating and deleting shadow copies, network shares, and online backup repositories.
                5. Mass Encryption & Extortion: Deploying AES-256 / RSA-4096 cryptographic locks across all servers simultaneously.

                The 3-2-1-1-0 Backup Rule:
                • 3 copies of important data
                • 2 different storage media (e.g., SSD + Cloud)
                • 1 copy stored off-site
                • 1 copy stored offline or immutable (WORM - Write Once, Read Many)
                • 0 errors verified through regular backup restoration drills!
            """.trimIndent(),
            realWorldExample = "A regional healthcare provider was crippled when ransomware encrypted its patient records. Because they maintained isolated offline tape backups, they restored operations within 48 hours without paying extortion demands.",
            defenseChecklist = listOf(
                "Maintain verified offline, air-gapped, or immutable cloud backups.",
                "Disable SMBv1 and unnecessary remote management protocols (RDP) exposed to the Internet.",
                "Implement network segmentation to restrict lateral movement between departments.",
                "Regularly test data restoration procedures under simulated disaster conditions."
            ),
            quizQuestion = "In the 3-2-1 backup strategy, what is the primary purpose of the '1' offline copy?",
            quizOptions = listOf(
                "To reduce cloud storage bills",
                "To prevent automated ransomware from reaching and encrypting the backup over the network",
                "To increase file compression ratios",
                "To speed up daily sync operations"
            ),
            correctOptionIndex = 1,
            quizExplanation = "Ransomware actively scans connected network drives and cloud mounts. An offline, air-gapped backup cannot be modified by network-based malware."
        ),
        Lesson(
            id = "lesson_wifi_network_hardening",
            title = "Wi-Fi & Home Network Security",
            category = "Advanced",
            readTimeMinutes = 7,
            overview = "Secure wireless access points against deauthentication, rogue AP evil twins, and IoT device pivots.",
            keyPoints = listOf(
                "WPA3 introduces Protected Management Frames (PMF) and SAE to resist dictionary attacks.",
                "Guest network isolation prevents compromised IoT devices from intercepting personal traffic.",
                "DNS-over-HTTPS (DoH) protects DNS queries from local ISP snooping and spoofing."
            ),
            fullContent = """
                Your home Wi-Fi is the digital gateway to every phone, laptop, smart TV, and camera in your household.

                Wireless Vulnerabilities:
                • Rogue APs / Evil Twins: Attackers clone a public hotspot SSID to perform Man-in-the-Middle (MitM) packet inspection.
                • Deauthentication Attacks: Attackers flood 802.11 deauth frames to kick clients off Wi-Fi and capture WPA2 four-way handshakes.
                • Vulnerable IoT Pivot Points: Cheap smart bulbs or cameras with hardcoded passwords provide attackers a bridge into the internal subnet.

                Router Hardening Protocol:
                1. Change Default Router Admin Credentials: Use a 20-character passphrase.
                2. Enforce WPA3-SAE or WPA2-AES: Disable WEP and WPA-TKIP completely.
                3. Disable WPS (Wi-Fi Protected Setup): The 8-digit PIN can be brute-forced in hours.
                4. Enable Guest Network Isolation: Place smart home devices on a dedicated 2.4GHz VLAN with intra-BSS isolation enabled.
                5. Disable Remote WAN Management: The router administration page should only be reachable from a wired LAN connection.
            """.trimIndent(),
            realWorldExample = "Attackers discovered an unpatched vulnerability in a smart home fish tank thermometer, used it to penetrate the internal network, and extracted a high-roller customer database.",
            defenseChecklist = listOf(
                "Verify your Wi-Fi is configured for WPA2-AES or WPA3-Personal encryption.",
                "Turn off UPnP (Universal Plug and Play) and WPS on your router.",
                "Isolate smart home and IoT gadgets on a separate Guest Wi-Fi SSID.",
                "Configure encrypted DNS (Cloudflare 1.1.1.1 or Quad9 9.9.9.9 with DoH)."
            ),
            quizQuestion = "Why should UPnP (Universal Plug and Play) be disabled on consumer home routers?",
            quizOptions = listOf(
                "It slows down internet speeds by 50%",
                "It allows malware on local devices to automatically open external firewall ports without authentication",
                "It prevents devices from connecting to Wi-Fi",
                "It disables Wi-Fi encryption"
            ),
            correctOptionIndex = 1,
            quizExplanation = "UPnP allows any software inside your network to forward ports on the router automatically, exposing internal devices directly to external Internet scans."
        ),
        Lesson(
            id = "lesson_zero_trust",
            title = "Zero Trust Architecture & Threat Intelligence",
            category = "Advanced",
            readTimeMinutes = 7,
            overview = "Explore modern enterprise defense architecture, microsegmentation, and indicator of compromise (IoC) analysis.",
            keyPoints = listOf(
                "Traditional castle-and-moat perimeter security is obsolete in cloud-first environments.",
                "Zero Trust assumes breach and continuously evaluates identity, posture, and context.",
                "Threat Intelligence (MITRE ATT&CK) categorizes adversary tactics, techniques, and procedures (TTPs)."
            ),
            fullContent = """
                The paradigm shift from perimeter defense to Zero Trust Architecture (ZTA) acknowledges that internal networks cannot be inherently trusted.

                Core Tenets of Zero Trust (NIST SP 800-207):
                • All data sources and computing services are considered resources.
                • All communication is secured regardless of network location.
                • Access to individual enterprise resources is granted on a per-session basis.
                • Dynamic Policy Enforcement: Access requests are evaluated against user identity, device health state, geographical location, and behavioral risk scores.
                • Continuous Telemetry & Logging: Collects robust logs to detect active lateral movement and credential abuse in real time.

                Threat Intelligence & MITRE ATT&CK:
                Security operations centers (SOCs) leverage frameworks like MITRE ATT&CK to track adversarial behaviors across stages: Reconnaissance, Initial Access, Execution, Persistence, Privilege Escalation, Defense Evasion, Credential Access, Discovery, Lateral Movement, Collection, Command and Control, Exfiltration, and Impact.
            """.trimIndent(),
            realWorldExample = "A multinational enterprise suffered a credential leak on a developer workstation. Because the company enforced Zero Trust microsegmentation and device health verification, the attacker was unable to pivot into production databases.",
            defenseChecklist = listOf(
                "Enforce strict least-privilege access across all digital accounts and systems.",
                "Segment sensitive subnets and databases behind dedicated authentication gateways.",
                "Implement continuous endpoint health validation.",
                "Ingest threat feeds to proactively block known malicious IPs, hashes, and domains."
            ),
            quizQuestion = "Which statement best summarizes the core principle of Zero Trust Architecture?",
            quizOptions = listOf(
                "Trust all devices connected to the internal corporate intranet",
                "Never trust, always verify every access request regardless of origin",
                "Rely strictly on perimeter firewalls to block external attacks",
                "Allow unrestricted access once a user logs in with a password"
            ),
            correctOptionIndex = 1,
            quizExplanation = "Zero Trust eliminates implicit trust based on network location, requiring continuous verification and explicit authorization for every transaction."
        )
    )

    val quizBank: List<CyberQuizQuestion> = listOf(
        CyberQuizQuestion(
            id = "q1",
            category = "Phishing & Social Engineering",
            difficulty = "Beginner",
            question = "You receive an SMS stating: 'URGENT: Your bank account is locked. Click https://mybank-security-auth.top to unlock.' What is the safest action?",
            options = listOf(
                "Click the link immediately to prevent account closure",
                "Reply with your login details to confirm your identity",
                "Delete the SMS, block the sender, and verify through your official banking app",
                "Forward the message to your friends to see if they got it"
            ),
            correctIndex = 2,
            explanation = "Never click links in unexpected security alerts. Access your account through the official mobile banking app or known URL."
        ),
        CyberQuizQuestion(
            id = "q2",
            category = "Password & Identity",
            difficulty = "Beginner",
            question = "Which of the following passwords possesses the highest mathematical entropy and resistance against brute-force attacks?",
            options = listOf(
                "P@ssw0rd123!",
                "Admin2024",
                "quantum#flute#crimson#orbit92",
                "1234567890abcdef"
            ),
            correctIndex = 2,
            explanation = "A 4-word random passphrase with symbols and numbers creates over 75 bits of entropy, vastly exceeding short complex passwords."
        ),
        CyberQuizQuestion(
            id = "q3",
            category = "Malware & Ransomware",
            difficulty = "Intermediate",
            question = "What is the primary risk of downloading pirated software or cracked video games?",
            options = listOf(
                "The game might run at lower graphics settings",
                "Keygens and cracks frequently bundle infostealers and hidden cryptominers",
                "It uses more battery power than regular software",
                "The software will expire after 30 days"
            ),
            correctIndex = 1,
            explanation = "Cracked software distribution networks are primary delivery mechanisms for infostealers that harvest browser session tokens and passwords."
        ),
        CyberQuizQuestion(
            id = "q4",
            category = "Network & Wi-Fi",
            difficulty = "Intermediate",
            question = "When connecting to a free public Wi-Fi network at an airport or coffee shop, what is the best defensive measure?",
            options = listOf(
                "Disable all phone notifications",
                "Use a trusted VPN or cellular data, and avoid sensitive banking tasks",
                "Enable file sharing so other users can see your device",
                "Accept all untrusted SSL certificate warnings"
            ),
            correctIndex = 1,
            explanation = "Public Wi-Fi networks are vulnerable to Man-in-the-Middle (MitM) attacks and rogue access points. A VPN encrypts all packet payloads."
        ),
        CyberQuizQuestion(
            id = "q5",
            category = "Advanced Defense",
            difficulty = "Advanced",
            question = "What does the principle of 'Least Privilege' mandate in access control?",
            options = listOf(
                "Grant users administrator privileges by default so they aren't blocked from working",
                "Provide users only the minimum permissions necessary to perform their required tasks",
                "Disable password requirements for internal employees",
                "Rotate cryptographic keys once every 10 years"
            ),
            correctIndex = 1,
            explanation = "Least privilege ensures that if an individual user account is compromised, the attacker's blast radius and damage potential are strictly contained."
        ),
        CyberQuizQuestion(
            id = "q6",
            category = "Web Security",
            difficulty = "Advanced",
            question = "What is a 'Typosquatting' attack in domain security?",
            options = listOf(
                "Attacking physical keyboards with electromagnetic sensors",
                "Registering misspelled variants of popular brand domains to trick users into visiting phishing sites",
                "Cracking Wi-Fi WPA2 handshakes",
                "Flooding DNS servers with UDP packet floods"
            ),
            correctIndex = 1,
            explanation = "Typosquatting targets users who make typographical errors when typing URLs (e.g., `goolge.com` or `paypa1.com`)."
        )
    )
}
