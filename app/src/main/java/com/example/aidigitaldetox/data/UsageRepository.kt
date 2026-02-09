package com.example.aidigitaldetox.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UsageRepository @Inject constructor(
    private val usageDao: UsageDao
) {
    fun getUsageForDate(date: String): Flow<List<UsageLog>> = usageDao.getUsageForDate(date)

    suspend fun logUsage(packageName: String, duration: Long) {
        val log = UsageLog(
            packageName = packageName,
            appName = packageName,
            date = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date()),
            durationInMillis = duration,
            openCount = 1
        )
        usageDao.insertUsageLog(log)
    }

    suspend fun logSession(packageName: String, startTime: Long, endTime: Long) {
        val session = AppSession(
            packageName = packageName,
            startTime = startTime,
            endTime = endTime,
            duration = endTime - startTime
        )
        usageDao.insertAppSession(session)
    }

    suspend fun logChallenge(challengeType: String, difficulty: String, success: Boolean, timeTaken: Long) {
        val log = ChallengeLog(
            timestamp = System.currentTimeMillis(),
            challengeType = challengeType,
            difficulty = difficulty,
            success = success,
            timeTaken = timeTaken
        )
        usageDao.insertChallengeLog(log)
    }

    // In-memory blocklist and snooze map
    private val blockedPackages = setOf("com.instagram.android", "com.facebook.katana", "com.google.android.youtube")
    private val temporaryUnlocks = java.util.concurrent.ConcurrentHashMap<String, Long>()

    fun isPackageBlocked(packageName: String): Boolean {
        if (!blockedPackages.contains(packageName)) return false
        
        val snoozeUntil = temporaryUnlocks[packageName]
        if (snoozeUntil != null) {
            if (System.currentTimeMillis() < snoozeUntil) {
                return false // Temporarily allowed
            } else {
                temporaryUnlocks.remove(packageName) // Expired
            }
        }
        return true
    }

    fun allowEmergencyAccess(packageName: String, durationMs: Long) {
        temporaryUnlocks[packageName] = System.currentTimeMillis() + durationMs
    }

    suspend fun logEmergencyUnlock(packageName: String) {
        val log = EmergencyLog(
            timestamp = System.currentTimeMillis(),
            packageName = packageName
        )
        usageDao.insertEmergencyLog(log)
    }
}
