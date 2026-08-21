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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AcademyData
import com.example.data.CyberQuizQuestion
import com.example.data.Lesson
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberOutlineButton
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
fun AcademyScreen(
    viewModel: CyberViewModel,
    onSelectLesson: (Lesson) -> Unit,
    onBack: () -> Unit
) {
    val allLessons = viewModel.allLessons
    val progressList by viewModel.academyProgress.collectAsState()
    val completedCount by viewModel.completedLessonsCount.collectAsState()
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf("All", "Beginner", "Intermediate", "Advanced")
    val filteredLessons = if (selectedCategory == "All") allLessons else allLessons.filter { it.category == selectedCategory }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberDarkBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
    ) {
        // Top Bar
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
                        text = "Cyber Security Academy",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Threat Defense Syllabus",
                        color = CyberCyan,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Academy Progress Card
        item {
            CyberCard(modifier = Modifier.fillMaxWidth(), glowEffect = true) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "ACADEMY COMPLETION",
                                color = TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "$completedCount of ${allLessons.size} Modules Mastered",
                                color = CyberCyan,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CyberCyan.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = "${if (allLessons.isNotEmpty()) (completedCount * 100) / allLessons.size else 0}%",
                                color = CyberCyan,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { if (allLessons.isNotEmpty()) completedCount.toFloat() / allLessons.size else 0f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = CyberCyan,
                        trackColor = Color(0xFF142240),
                        strokeCap = StrokeCap.Round
                    )
                }
            }
        }

        // Category Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) CyberCyan else CyberCardBg,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) CyberCyan else CyberCardBorder
                        ),
                        modifier = Modifier.clickable { selectedCategory = cat }
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) Color(0xFF040E20) else TextSecondary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Lessons List
        items(filteredLessons) { lesson ->
            val isCompleted = progressList.any { it.lessonId == lesson.id && it.isCompleted }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(
                        1.dp,
                        if (isCompleted) CyberGreen.copy(alpha = 0.5f) else CyberCardBorder,
                        RoundedCornerShape(14.dp)
                    )
                    .clickable { onSelectLesson(lesson) }
                    .testTag("lesson_card_${lesson.id}"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CyberCardBg)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = when (lesson.category) {
                                "Beginner" -> CyberGreen.copy(alpha = 0.15f)
                                "Intermediate" -> CyberYellow.copy(alpha = 0.15f)
                                else -> CyberPurple.copy(alpha = 0.15f)
                            }
                        ) {
                            Text(
                                text = lesson.category.uppercase(),
                                color = when (lesson.category) {
                                    "Beginner" -> CyberGreen
                                    "Intermediate" -> CyberYellow
                                    else -> CyberPurple
                                },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Timer, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "${lesson.readTimeMinutes} min read", color = TextMuted, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = lesson.title,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = lesson.overview,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isCompleted) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CyberGreen, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Completed & Certified", color = CyberGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Text(text = "Not Completed", color = TextMuted, fontSize = 11.sp)
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CyberCyan.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "Start Module →",
                                color = CyberCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LessonDetailScreen(
    lesson: Lesson,
    viewModel: CyberViewModel,
    onBack: () -> Unit
) {
    val progressList by viewModel.academyProgress.collectAsState()
    val isCompleted = progressList.any { it.lessonId == lesson.id && it.isCompleted }

    var selectedOption by remember { mutableIntStateOf(-1) }
    var hasSubmittedQuiz by remember { mutableStateOf(false) }

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
                        text = lesson.category,
                        color = CyberCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = lesson.title,
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Key Takeaways
        item {
            CyberCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "CORE DEFENSIVE PRINCIPLES",
                        color = CyberCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    lesson.keyPoints.forEach { point ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(text = "•", color = CyberCyan, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
                            Text(text = point, color = TextPrimary, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Main Reading Content
        item {
            CyberCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "DETAILED SYLLABUS",
                        color = CyberCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = lesson.fullContent,
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // Real-world Attack Case Study
        item {
            CyberCard(
                borderColor = CyberOrange.copy(alpha = 0.5f),
                backgroundColor = Color(0xFF171B33),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "REAL-WORLD INCIDENT CASE STUDY",
                        color = CyberOrange,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Text(
                        text = lesson.realWorldExample,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Defense Checklist
        item {
            CyberCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "DEFENSIVE ACTION CHECKLIST",
                        color = CyberGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    lesson.defenseChecklist.forEach { item ->
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
                            Text(text = item, color = TextPrimary, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Interactive Knowledge Check
        item {
            CyberCard(
                borderColor = if (isCompleted) CyberGreen else CyberCyan,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "KNOWLEDGE CERTIFICATION CHECK",
                            color = CyberCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        if (isCompleted) {
                            Text(
                                text = "VERIFIED ✓",
                                color = CyberGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = lesson.quizQuestion,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    lesson.quizOptions.forEachIndexed { index, option ->
                        val isSelected = selectedOption == index
                        val isCorrect = index == lesson.correctOptionIndex

                        val optionBg = when {
                            hasSubmittedQuiz && isCorrect -> CyberGreen.copy(alpha = 0.2f)
                            hasSubmittedQuiz && isSelected && !isCorrect -> CyberRed.copy(alpha = 0.2f)
                            isSelected -> CyberCyan.copy(alpha = 0.15f)
                            else -> CyberCardBgElevated
                        }

                        val optionBorder = when {
                            hasSubmittedQuiz && isCorrect -> CyberGreen
                            hasSubmittedQuiz && isSelected && !isCorrect -> CyberRed
                            isSelected -> CyberCyan
                            else -> CyberCardBorder
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = optionBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, optionBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    if (!hasSubmittedQuiz) {
                                        selectedOption = index
                                    }
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${('A' + index)}. ",
                                    color = if (isSelected) CyberCyan else TextSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = option,
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (!hasSubmittedQuiz) {
                        NeonButton(
                            text = "Submit & Verify Knowledge",
                            onClick = {
                                if (selectedOption != -1) {
                                    hasSubmittedQuiz = true
                                    if (selectedOption == lesson.correctOptionIndex) {
                                        viewModel.completeLesson(lesson)
                                    }
                                }
                            },
                            enabled = selectedOption != -1,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Column {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (selectedOption == lesson.correctOptionIndex) CyberGreen.copy(alpha = 0.15f) else CyberRed.copy(alpha = 0.15f),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (selectedOption == lesson.correctOptionIndex) CyberGreen else CyberRed
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = if (selectedOption == lesson.correctOptionIndex) "Correct! Module certification unlocked." else "Incorrect answer. Review explanation below:",
                                    color = if (selectedOption == lesson.correctOptionIndex) CyberGreen else CyberRed,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = lesson.quizExplanation,
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuizScreen(
    viewModel: CyberViewModel,
    onNavigateToLeaderboard: () -> Unit,
    onBack: () -> Unit
) {
    val quizQuestions = viewModel.allQuizQuestions
    var selectedDifficulty by remember { mutableStateOf("All") }
    val filteredQuestions = if (selectedDifficulty == "All") quizQuestions else quizQuestions.filter { it.difficulty == selectedDifficulty }

    var currentQuestionIdx by remember { mutableIntStateOf(0) }
    var selectedAnswerIdx by remember { mutableIntStateOf(-1) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }
    var scoreCount by remember { mutableIntStateOf(0) }
    var isQuizCompleted by remember { mutableStateOf(false) }

    val currentQ = filteredQuestions.getOrNull(currentQuestionIdx)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberDarkBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                            text = "Cyber Defense Quiz",
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Interactive Threat Testing",
                            color = CyberOrange,
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(
                    onClick = onNavigateToLeaderboard,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(CyberCardBg)
                        .border(1.dp, CyberCardBorder, RoundedCornerShape(10.dp))
                ) {
                    Icon(Icons.Default.Leaderboard, contentDescription = "Leaderboard", tint = CyberYellow)
                }
            }
        }

        if (!isQuizCompleted && currentQ != null) {
            // Progress
            item {
                CyberCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "QUESTION ${currentQuestionIdx + 1} OF ${filteredQuestions.size}",
                                color = CyberCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Score: $scoreCount",
                                color = CyberGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { (currentQuestionIdx + 1).toFloat() / filteredQuestions.size },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = CyberCyan,
                            trackColor = Color(0xFF142240)
                        )
                    }
                }
            }

            // Question Card
            item {
                CyberCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = CyberOrange.copy(alpha = 0.15f),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text(
                                text = "${currentQ.category} • ${currentQ.difficulty}",
                                color = CyberOrange,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Text(
                            text = currentQ.question,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        currentQ.options.forEachIndexed { index, option ->
                            val isSelected = selectedAnswerIdx == index
                            val isCorrect = index == currentQ.correctIndex

                            val optionBg = when {
                                isAnswerSubmitted && isCorrect -> CyberGreen.copy(alpha = 0.2f)
                                isAnswerSubmitted && isSelected && !isCorrect -> CyberRed.copy(alpha = 0.2f)
                                isSelected -> CyberCyan.copy(alpha = 0.15f)
                                else -> CyberCardBgElevated
                            }

                            val optionBorder = when {
                                isAnswerSubmitted && isCorrect -> CyberGreen
                                isAnswerSubmitted && isSelected && !isCorrect -> CyberRed
                                isSelected -> CyberCyan
                                else -> CyberCardBorder
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = optionBg,
                                border = androidx.compose.foundation.BorderStroke(1.dp, optionBorder),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        if (!isAnswerSubmitted) {
                                            selectedAnswerIdx = index
                                        }
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${('A' + index)}. ",
                                        color = if (isSelected) CyberCyan else TextSecondary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = option,
                                        color = TextPrimary,
                                        fontSize = 12.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        if (!isAnswerSubmitted) {
                            NeonButton(
                                text = "Confirm Answer",
                                onClick = {
                                    if (selectedAnswerIdx != -1) {
                                        isAnswerSubmitted = true
                                        if (selectedAnswerIdx == currentQ.correctIndex) {
                                            scoreCount++
                                        }
                                    }
                                },
                                enabled = selectedAnswerIdx != -1,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Column {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (selectedAnswerIdx == currentQ.correctIndex) CyberGreen.copy(alpha = 0.15f) else CyberRed.copy(alpha = 0.15f),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (selectedAnswerIdx == currentQ.correctIndex) CyberGreen else CyberRed
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = if (selectedAnswerIdx == currentQ.correctIndex) "Correct! +10 Defense Points" else "Incorrect",
                                        color = if (selectedAnswerIdx == currentQ.correctIndex) CyberGreen else CyberRed,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = currentQ.explanation,
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                NeonButton(
                                    text = if (currentQuestionIdx + 1 < filteredQuestions.size) "Next Question →" else "Finish & Record Score",
                                    onClick = {
                                        if (currentQuestionIdx + 1 < filteredQuestions.size) {
                                            currentQuestionIdx++
                                            selectedAnswerIdx = -1
                                            isAnswerSubmitted = false
                                        } else {
                                            isQuizCompleted = true
                                            viewModel.saveQuizScore("Cyber Defense", selectedDifficulty, scoreCount, filteredQuestions.size)
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Quiz Complete Card
            item {
                CyberCard(glowEffect = true, modifier = Modifier.fillMaxWidth()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(CyberGreen.copy(alpha = 0.2f))
                                .border(2.dp, CyberGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = CyberGreen, modifier = Modifier.size(36.dp))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Quiz Challenge Completed!",
                            color = TextPrimary,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )

                        Text(
                            text = "You scored $scoreCount out of ${filteredQuestions.size}",
                            color = CyberCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        NeonButton(
                            text = "Retake Quiz",
                            onClick = {
                                currentQuestionIdx = 0
                                selectedAnswerIdx = -1
                                isAnswerSubmitted = false
                                scoreCount = 0
                                isQuizCompleted = false
                            },
                            icon = Icons.Default.Refresh,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        CyberOutlineButton(
                            text = "View Leaderboard",
                            onClick = onNavigateToLeaderboard,
                            icon = Icons.Default.Leaderboard,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderboardScreen(
    viewModel: CyberViewModel,
    onBack: () -> Unit
) {
    val results by viewModel.allQuizResults.collectAsState()

    val leaderboardRanks = listOf(
        Triple("Sentinel_01", 100, "Elite Specialist"),
        Triple("CyberShadow", 95, "Senior Defender"),
        Triple("You (Defender)", 90, "Security Specialist"),
        Triple("HexZero", 85, "Cyber Analyst"),
        Triple("ZeroTrustGuy", 80, "Practitioner")
    )

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
                        text = "Defender Leaderboard",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Global Anonymous Security Posture Rankings",
                        color = CyberYellow,
                        fontSize = 11.sp
                    )
                }
            }
        }

        item {
            CyberCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "GLOBAL DEFENDER RANKS",
                        color = CyberYellow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    leaderboardRanks.forEachIndexed { index, (name, score, rank) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (name.contains("You")) CyberCyan.copy(alpha = 0.12f) else Color.Transparent)
                                .padding(vertical = 8.dp, horizontal = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "#${index + 1}",
                                    color = if (index == 0) CyberYellow else TextMuted,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp,
                                    modifier = Modifier.width(28.dp)
                                )
                                Column {
                                    Text(
                                        text = name,
                                        color = if (name.contains("You")) CyberCyan else TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(text = rank, color = TextMuted, fontSize = 10.sp)
                                }
                            }

                            Text(
                                text = "$score pts",
                                color = if (name.contains("You")) CyberCyan else CyberGreen,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                        if (index < leaderboardRanks.size - 1) {
                            HorizontalDivider(color = CyberCardBorder)
                        }
                    }
                }
            }
        }
    }
}
