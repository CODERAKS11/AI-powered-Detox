package com.example.aidigitaldetox.data

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class AppLockRepository @Inject constructor(
    private val restrictedAppDao: RestrictedAppDao
) {
    private fun isNewDay(lastUpdated: Long): Boolean {
        val offsetMs = 12L * 60 * 60 * 1000 // 12 hours offset to make noon the boundary
        val lastUpdateCalendar = Calendar.getInstance().apply { timeInMillis = lastUpdated - offsetMs }
        val currentCalendar = Calendar.getInstance().apply { timeInMillis = System.currentTimeMillis() - offsetMs }
        
        return lastUpdateCalendar.get(Calendar.YEAR) != currentCalendar.get(Calendar.YEAR) ||
               lastUpdateCalendar.get(Calendar.DAY_OF_YEAR) != currentCalendar.get(Calendar.DAY_OF_YEAR)
    }

    val allRestrictedApps: Flow<List<RestrictedApp>> = restrictedAppDao.getAllRestrictedApps()

    suspend fun addOrUpdateRestriction(packageName: String, appName: String, limitMs: Long, currentUsageMs: Long = 0) {
        val existing = restrictedAppDao.getRestrictedApp(packageName)
        
        // Use the maximum of tracked usage or system usage to ensure we don't cheat
        val initialUsage = if (existing != null && isNewDay(existing.lastUpdated)) {
            currentUsageMs
        } else {
            kotlin.math.max(existing?.todayUsageMs ?: 0, currentUsageMs)
        }
        val isLocked = initialUsage >= limitMs
        
        val app = existing?.copy(
                dailyLimitMs = limitMs,
                todayUsageMs = initialUsage,
                isLocked = isLocked,
                lastUpdated = System.currentTimeMillis()
            )
            ?: RestrictedApp(
                packageName = packageName, 
                appName = appName, 
                dailyLimitMs = limitMs, 
                todayUsageMs = initialUsage, 
                isLocked = isLocked, 
                lastUpdated = System.currentTimeMillis(),
                extensionCount = 0,
                warningShown = false
            )
        restrictedAppDao.insertOrUpdate(app)
    }

    suspend fun removeRestriction(packageName: String) {
        restrictedAppDao.getRestrictedApp(packageName)?.let {
            restrictedAppDao.delete(it)
        }
    }

    suspend fun updateUsage(packageName: String, currentUsageMs: Long): Boolean {
        val app = restrictedAppDao.getRestrictedApp(packageName) ?: return false
        
        if (isNewDay(app.lastUpdated)) {
            restrictedAppDao.resetDailyStats(packageName, currentUsageMs, System.currentTimeMillis())
            val isLocked = currentUsageMs >= app.dailyLimitMs
            if (isLocked) {
                restrictedAppDao.updateUsageAndLock(packageName, currentUsageMs, true, System.currentTimeMillis())
            }
            return isLocked
        }

        val isLocked = currentUsageMs >= app.dailyLimitMs
        restrictedAppDao.updateUsageAndLock(packageName, currentUsageMs, isLocked, System.currentTimeMillis())
        return isLocked
    }

    suspend fun isAppLocked(packageName: String): Boolean {
        val app = getRestrictedApp(packageName) ?: return false
        return app.isLocked
    }

    suspend fun extendLimit(packageName: String, additionalTimeMs: Long) {
        val app = getRestrictedApp(packageName) ?: return
        val newLimit = app.dailyLimitMs + additionalTimeMs
        restrictedAppDao.insertOrUpdate(app.copy(dailyLimitMs = newLimit))
        
        // Also unlock if it was locked, because we just extended the limit
        if (app.isLocked) {
             restrictedAppDao.updateUsageAndLock(packageName, app.todayUsageMs, false, System.currentTimeMillis())
        }
    }

    suspend fun addUsage(packageName: String, timeMs: Long): Boolean {
        val app = getRestrictedApp(packageName) ?: return false
        val newUsage = app.todayUsageMs + timeMs
        val isLocked = newUsage >= app.dailyLimitMs
        
        restrictedAppDao.updateUsageAndLock(packageName, newUsage, isLocked, System.currentTimeMillis())
        return isLocked
    }

    suspend fun getRestrictedApp(packageName: String): RestrictedApp? {
        val app = restrictedAppDao.getRestrictedApp(packageName)
        if (app != null && isNewDay(app.lastUpdated)) {
            restrictedAppDao.resetDailyStats(packageName, 0, System.currentTimeMillis())
            return restrictedAppDao.getRestrictedApp(packageName)
        }
        return app
    }

    suspend fun incrementExtensionCount(packageName: String) {
        restrictedAppDao.incrementExtensionCount(packageName)
    }

    suspend fun setWarningShown(packageName: String) {
        restrictedAppDao.setWarningShown(packageName)
    }
}
