package com.example.aidigitaldetox.data

import com.example.aidigitaldetox.domain.AddictionClassifier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MlRepository @Inject constructor(
    private val usageDao: UsageDao,
    private val classifier: AddictionClassifier
) {

    suspend fun analyzeBehaviorAndGetDifficulty(): Int {
        val recentHistory = usageDao.getRecentHistory().first()
        
        // simple simulation if no history
        if (recentHistory.isEmpty()) return 3

        val latest = recentHistory.first()
        // In reality, we'd pass a vector of historical data to the classifier
        val score = classifier.predictAddictionScore(
            latest.totalUsageTime,
            0, // We need to track actual open count
            latest.emergencyUnlockCount
        )
        return classifier.recommendDifficulty(score)
    }
}
