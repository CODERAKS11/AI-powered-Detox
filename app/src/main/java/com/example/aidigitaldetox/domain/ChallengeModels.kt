package com.example.aidigitaldetox.domain

enum class ChallengeType {
    MATH, MEMORY, PATTERN, STEP, SQUAT, HOLD_STILL
}

enum class ChallengeDifficulty {
    EASY, MEDIUM, HARD
}

data class ChallengeConfig(
    val type: ChallengeType,
    val difficulty: ChallengeDifficulty,
    val description: String,
    val targetCount: Int, // e.g. 5 problems, 20 steps
    val timeLimitSeconds: Int = 0 // 0 = no limit
)
