package com.example.aidigitaldetox.data

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class AppLockRepository @Inject constructor(
    private val restrictedAppDao: RestrictedAppDao
) {
    val allRestrictedApps: Flow<List<RestrictedApp>> = restrictedAppDao.getAllRestrictedApps()

    suspend fun addOrUpdateRestriction(packageName: String, appName: String, limitMs: Long, currentUsageMs: Long = 0) {
        val existing = restrictedAppDao.getRestrictedApp(packageName)
        
        // Use the maximum of tracked usage or system usage to ensure we don't cheat
        val initialUsage = kotlin.math.max(existing?.todayUsageMs ?: 0, currentUsageMs)
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
                lastUpdated = System.currentTimeMillis()
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
        
        val isLocked = currentUsageMs >= app.dailyLimitMs
        restrictedAppDao.updateUsageAndLock(packageName, currentUsageMs, isLocked, System.currentTimeMillis())
        return isLocked
    }

    suspend fun isAppLocked(packageName: String): Boolean {
        val app = restrictedAppDao.getRestrictedApp(packageName) ?: return false
        return app.isLocked
    }

    suspend fun extendLimit(packageName: String, additionalTimeMs: Long) {
        val app = restrictedAppDao.getRestrictedApp(packageName) ?: return
        val newLimit = app.dailyLimitMs + additionalTimeMs
        restrictedAppDao.insertOrUpdate(app.copy(dailyLimitMs = newLimit))
        
        // Also unlock if it was locked, because we just extended the limit
        if (app.isLocked) {
             restrictedAppDao.updateUsageAndLock(packageName, app.todayUsageMs, false, System.currentTimeMillis())
        }
    }

    suspend fun addUsage(packageName: String, timeMs: Long): Boolean {
        val app = restrictedAppDao.getRestrictedApp(packageName) ?: return false
        val newUsage = app.todayUsageMs + timeMs
        val isLocked = newUsage >= app.dailyLimitMs
        
        restrictedAppDao.updateUsageAndLock(packageName, newUsage, isLocked, System.currentTimeMillis())
        return isLocked
    }

    suspend fun getRestrictedApp(packageName: String): RestrictedApp? {
        return restrictedAppDao.getRestrictedApp(packageName)
    }
}
