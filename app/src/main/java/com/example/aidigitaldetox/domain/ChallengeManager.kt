package com.example.aidigitaldetox.domain

import com.example.aidigitaldetox.ml.PredictionResult
import com.example.aidigitaldetox.ml.RelapseRisk
import javax.inject.Inject
import kotlin.random.Random

class ChallengeManager @Inject constructor() {

    fun generateChallenge(prediction: PredictionResult): ChallengeConfig {
        val difficulty = when(prediction.recommendedDifficulty) {
            3 -> ChallengeDifficulty.HARD
            2 -> ChallengeDifficulty.MEDIUM
            else -> ChallengeDifficulty.EASY
        }

        // Emergency/High Risk -> Cognitive Only
        val isEmergency = prediction.relapseRisk == RelapseRisk.HIGH
        
        val type = if (isEmergency) {
            // Force cognitive
            listOf(ChallengeType.MATH, ChallengeType.MEMORY).random()
        } else {
            // Mixed
            ChallengeType.values().random()
        }

        return createConfig(type, difficulty)
    }

    private fun createConfig(type: ChallengeType, difficulty: ChallengeDifficulty): ChallengeConfig {
        return when (type) {
            ChallengeType.MATH -> {
                val count = when(difficulty) {
                    ChallengeDifficulty.EASY -> 3
                    ChallengeDifficulty.MEDIUM -> 5
                    ChallengeDifficulty.HARD -> 10
                }
                ChallengeConfig(type, difficulty, "Solve $count math problems", count)
            }
            ChallengeType.MEMORY -> {
                val levels = when(difficulty) {
                    ChallengeDifficulty.EASY -> 3
                    ChallengeDifficulty.MEDIUM -> 5
                    ChallengeDifficulty.HARD -> 7
                }
                ChallengeConfig(type, difficulty, "Complete $levels memory sequences", levels)
            }
            ChallengeType.STEP -> {
                val steps = when(difficulty) {
                    ChallengeDifficulty.EASY -> 20
                    ChallengeDifficulty.MEDIUM -> 50
                    ChallengeDifficulty.HARD -> 100
                }
                ChallengeConfig(type, difficulty, "Walk $steps steps", steps)
            }
            ChallengeType.SQUAT -> {
                 val reps = when(difficulty) {
                    ChallengeDifficulty.EASY -> 5
                    ChallengeDifficulty.MEDIUM -> 10
                    ChallengeDifficulty.HARD -> 20
                }
                ChallengeConfig(type, difficulty, "Do $reps Squats", reps)
            }
            // Fallbacks for other types to match basic logic
            else -> ChallengeConfig(type, difficulty, "Complete task", 1)
        }
    }
}
