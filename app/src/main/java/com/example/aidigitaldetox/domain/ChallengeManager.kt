package com.example.aidigitaldetox.domain

import com.example.aidigitaldetox.ml.PredictionResult
import com.example.aidigitaldetox.ml.RelapseRisk
import javax.inject.Inject
import kotlin.random.Random

class ChallengeManager @Inject constructor() {

    fun generateChallenge(extensionCount: Int): ChallengeConfig {
        // Phase 1 (First Limit): Visual Memory x3
        if (extensionCount == 0) {
            return createConfig(ChallengeType.MEMORY, ChallengeDifficulty.EASY, 3)
        }

        // Phase 2 (1st Extension): Math x3
        if (extensionCount == 1) {
             return createConfig(ChallengeType.MATH, ChallengeDifficulty.EASY, 3) 
        }

        // Phase 3 (2nd Extension): Walk 20 Steps
        if (extensionCount == 2) {
             return createConfig(ChallengeType.STEP, ChallengeDifficulty.MEDIUM, 2) // 2 * 10 = 20 steps
        }

        // Phase 4 (3rd+ Extension): 20 Squats
        if (extensionCount >= 3) {
             // For 4th, 5th, etc., we can keep it at 20 or scale slightly. 
             // User asked for "Phase 4 of 20 squats", implying a plateau or specific resistance.
             // Let's scale slightly: 20, 25, 30...
             val reps = 20 + (extensionCount - 3) * 5
             return createConfig(ChallengeType.SQUAT, ChallengeDifficulty.HARD, reps)
        }
        
        // ML Fallback (Shouldn't be reached)
        return createConfig(ChallengeType.MEMORY, ChallengeDifficulty.EASY, 3)
    }
    
    // Adjusted helper to accept overrides
    private fun createConfig(type: ChallengeType, difficulty: ChallengeDifficulty, overrideAmount: Int? = null): ChallengeConfig {
        return when (type) {
            ChallengeType.MATH -> {
                val count = overrideAmount ?: when(difficulty) {
                    ChallengeDifficulty.EASY -> 3
                    ChallengeDifficulty.MEDIUM -> 5
                    ChallengeDifficulty.HARD -> 10
                }
                ChallengeConfig(type, difficulty, "Solve $count math problems", count)
            }
            ChallengeType.MEMORY -> {
                val levels = overrideAmount ?: when(difficulty) {
                    ChallengeDifficulty.EASY -> 3
                    ChallengeDifficulty.MEDIUM -> 5
                    ChallengeDifficulty.HARD -> 7
                }
                ChallengeConfig(type, difficulty, "Complete $levels memory sequences", levels)
            }
            ChallengeType.STEP -> {
                val steps = overrideAmount?.times(10) ?: when(difficulty) { // Scale step count differently
                    ChallengeDifficulty.EASY -> 20
                    ChallengeDifficulty.MEDIUM -> 50
                    ChallengeDifficulty.HARD -> 100
                }
                ChallengeConfig(type, difficulty, "Walk $steps steps", steps)
            }
            ChallengeType.SQUAT -> {
                 val reps = overrideAmount ?: when(difficulty) {
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
