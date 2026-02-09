package com.example.aidigitaldetox.ml

enum class RelapseRisk {
    LOW, MEDIUM, HIGH
}

data class PredictionResult(
    val doomscrollProbability: Float, // 0.0 - 1.0
    val expectedSessionMinutes: Int,
    val relapseRisk: RelapseRisk,
    val recommendedDifficulty: Int    // 1=EASY, 2=MEDIUM, 3=HARD
)
