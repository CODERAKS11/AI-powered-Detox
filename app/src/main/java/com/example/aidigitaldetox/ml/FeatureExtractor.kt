package com.example.aidigitaldetox.ml

import com.example.aidigitaldetox.data.UsageDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

class FeatureExtractor @Inject constructor(
    private val usageDao: UsageDao
) {

    suspend fun extractFeatures(): FloatArray {
        return withContext(Dispatchers.IO) {
            // 1. Prepare Calendar and Time
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val timeOfDay = hour / 24f
            val dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) - 1) / 7f
            val now = System.currentTimeMillis()
            val oneDayAgo = now - (24 * 60 * 60 * 1000)
            val todayStr = "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DAY_OF_MONTH)}"

            // 2. Parallel Database Queries
            val recentSessionsDeferred = async { usageDao.getSessionsSince(oneDayAgo).firstOrNull() ?: emptyList() }
            val totalUsageDeferred = async { usageDao.getTotalUsageForDate(todayStr) ?: 0L }
            val usageForDateDeferred = async { usageDao.getUsageForDate(todayStr).firstOrNull() ?: emptyList() }
            val challengeHistoryDeferred = async { usageDao.getChallengeHistory().firstOrNull() ?: emptyList() }
            val recentHistoryDeferred = async { usageDao.getRecentHistory().firstOrNull() ?: emptyList() }

            // 3. Await Results
            val recentSessions = recentSessionsDeferred.await()
            val totalUsage = totalUsageDeferred.await()
            val usageLogs = usageForDateDeferred.await()
            val challengeLogs = challengeHistoryDeferred.await()
            val history = recentHistoryDeferred.await()

            // 4. Process Features (In Memory - Fast)
            // Feature 2: Previous Session Duration
            val lastSession = recentSessions.asSequence().sortedByDescending { it.endTime }.firstOrNull()
            val prevSessionDuration = (lastSession?.duration ?: 0L) / (60 * 60 * 1000f) // Normalized hours

            // Feature 3: Total Usage Today
            val totalUsageNorm = totalUsage / (12 * 60 * 60 * 1000f)

            // Feature 4: Opens Count
            val totalOpens = usageLogs.sumOf { it.openCount }
            val opensNorm = totalOpens / 100f

            // Feature 6: Reopen Gap
            val lastSessionEndTime = recentSessions.asSequence().sortedByDescending { it.endTime }.firstOrNull()?.endTime ?: 0L
            val gapMs = if (lastSessionEndTime > 0) now - lastSessionEndTime else 60 * 60 * 1000L
            val reopenGapNorm = (gapMs / (60 * 60 * 1000f)).coerceIn(0f, 1f)

            // Feature 7: Success Rate
            val successRate = if (challengeLogs.isNotEmpty()) {
                challengeLogs.count { it.success }.toFloat() / challengeLogs.size
            } else {
                1.0f
            }

            // Feature 5: Emergency Count
            val profile = history.firstOrNull()
            val emergencyCount = profile?.emergencyUnlockCount ?: 0
            val emergencyNorm = emergencyCount / 10f

            // Feature 8: Z-Score (Anomaly)
            val usageHistory = history.map { it.totalUsageTime.toFloat() }
            val zScore = if (usageHistory.isNotEmpty()) {
                val mean = usageHistory.average().toFloat()
                val variance = usageHistory.map { (it - mean) * (it - mean) }.average().toFloat()
                val stdDev = kotlin.math.sqrt(variance)
                val safeStdDev = if (stdDev < 60 * 60 * 1000f) 60 * 60 * 1000f else stdDev
                (totalUsage - mean) / safeStdDev
            } else {
                0.0f
            }

            // Return Float Tensor [1, 9]
            floatArrayOf(
                timeOfDay.coerceIn(0f, 1f),
                dayOfWeek.coerceIn(0f, 1f),
                prevSessionDuration.coerceIn(0f, 1f),
                totalUsageNorm.coerceIn(0f, 1f),
                opensNorm.coerceIn(0f, 1f),
                emergencyNorm.coerceIn(0f, 1f),
                reopenGapNorm,     // Index 6
                successRate,       // Index 7
                zScore             // Index 8
            )
        }
    }
}
