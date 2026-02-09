package com.example.aidigitaldetox.ml

import com.example.aidigitaldetox.data.UsageDao
import kotlinx.coroutines.flow.firstOrNull
import java.util.Calendar
import javax.inject.Inject

class FeatureExtractor @Inject constructor(
    private val usageDao: UsageDao
) {

    suspend fun extractFeatures(): FloatArray = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        // 1. Time of Day (Normalized 0-1)
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val timeOfDay = hour / 24f

        // 2. Day of Week (Normalized 0-1)
        val dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) - 1) / 7f

        // 3. Previous Session Duration (Normalized against 1 hour)
        val now = System.currentTimeMillis()
        val oneDayAgo = now - (24 * 60 * 60 * 1000)
        
        // Get sessions from last 24 hours to find previous one
        val recentSessions = usageDao.getSessionsSince(oneDayAgo).firstOrNull() ?: emptyList()
        val lastSession = recentSessions.asSequence().sortedByDescending { it.endTime }.firstOrNull()
        val prevSessionDuration = (lastSession?.duration ?: 0L) / (60 * 60 * 1000f) // Normalized hours

        // 4. Total Usage Today (Normalized against 12 hours)
        val todayStr = "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DAY_OF_MONTH)}" 
        val totalUsage = usageDao.getTotalUsageForDate(todayStr) ?: 0L
        val totalUsageNorm = totalUsage / (12 * 60 * 60 * 1000f)

        // 5. Unlock Attempts / Open Count (Normalized against 100)
        val usageLogs = usageDao.getUsageForDate(todayStr).firstOrNull() ?: emptyList()
        val totalOpens = usageLogs.sumOf { it.openCount }
        val opensNorm = totalOpens / 100f

        // 6. Reopen Gap (Normalized 60m)
        val lastSessionEndTime = recentSessions.asSequence().sortedByDescending { it.endTime }.firstOrNull()?.endTime ?: 0L
        val gapMs = if (lastSessionEndTime > 0) now - lastSessionEndTime else 60 * 60 * 1000L
        val reopenGapNorm = (gapMs / (60 * 60 * 1000f)).coerceIn(0f, 1f)

        // 7. Challenge Success Rate
        val challengeLogs = usageDao.getChallengeHistory().firstOrNull() ?: emptyList()
        val successRate = if (challengeLogs.isNotEmpty()) {
            challengeLogs.count { it.success }.toFloat() / challengeLogs.size
        } else {
            1.0f 
        }
        
        // 8. Emergency Count (Normalized 10)
        val profile = usageDao.getRecentHistory().firstOrNull()?.firstOrNull()
        val emergencyCount = profile?.emergencyUnlockCount ?: 0
        val emergencyNorm = emergencyCount / 10f

        // Return Float Tensor [1, 8]
        return@withContext floatArrayOf(
            timeOfDay.coerceIn(0f, 1f),
            dayOfWeek.coerceIn(0f, 1f),
            prevSessionDuration.coerceIn(0f, 1f),
            totalUsageNorm.coerceIn(0f, 1f),
            opensNorm.coerceIn(0f, 1f),
            emergencyNorm.coerceIn(0f, 1f),
            reopenGapNorm,     // Index 6
            successRate        // Index 7
        )
    }
}
