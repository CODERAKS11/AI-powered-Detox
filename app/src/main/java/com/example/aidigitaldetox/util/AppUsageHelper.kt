package com.example.aidigitaldetox.util

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject

data class AppUsageInfo(
    val packageName: String,
    val appName: String,
    val usageTimeMillis: Long,
    val lastTimeUsed: Long,
    val category: String
)

class AppUsageHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val usageStatsManager =
        context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    /**
     * Returns today's usage stats for all apps.
     * No feature removed — improved correctness.
     */
    fun getTodayUsageStats(): List<AppUsageInfo> {

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()

        // Use queryEvents for precise calculation.
        // queryAndAggregateUsageStats can be inaccurate on some devices due to bucket overlap.
        val events = usageStatsManager.queryEvents(startTime, endTime)
        val usageMap = mutableMapOf<String, Long>()
        val lastEventMap = mutableMapOf<String, Long>()

        val event = android.app.usage.UsageEvents.Event()
        while (events.hasNextEvent()) {
            events.getNextEvent(event)
            val pkg = event.packageName
            
            if (event.eventType == android.app.usage.UsageEvents.Event.MOVE_TO_FOREGROUND) {
                lastEventMap[pkg] = event.timeStamp
            } else if (event.eventType == android.app.usage.UsageEvents.Event.MOVE_TO_BACKGROUND) {
                val start = lastEventMap[pkg]
                if (start != null) {
                    val duration = event.timeStamp - start
                    if (duration > 0) {
                        usageMap[pkg] = (usageMap[pkg] ?: 0L) + duration
                    }
                    lastEventMap.remove(pkg) // Clear start time
                }
            }
        }
        
        // Handle apps currently in foreground (no background event yet)
        lastEventMap.forEach { (pkg, start) ->
            val duration = endTime - start
            if (duration > 0) {
                 usageMap[pkg] = (usageMap[pkg] ?: 0L) + duration
            }
        }

        // Convert Map back to list of partial UsageStats-like objects for compatibility
        return usageMap.entries
            .filter { it.value > 0 }
            .filter { (pkg, _) ->
                // Filter 1: Check if it's a launchable app (excludes most system services)
                context.packageManager.getLaunchIntentForPackage(pkg) != null 
            }
            .filter { (pkg, _) ->
                // Filter 2: Explicit exclude list
                val systemPackages = setOf(
                    "com.android.systemui",
                    "com.google.android.apps.nexuslauncher",
                    "com.sec.android.app.launcher",
                    "com.miui.home", 
                    "com.huawei.android.launcher",
                    "com.android.settings" 
                )
                !systemPackages.contains(pkg)
            }
            .mapNotNull { (pkg, duration) ->
                val appName = getAppName(pkg)
                if (appName.isNotEmpty()) {
                    AppUsageInfo(
                        packageName = pkg,
                        appName = appName,
                        usageTimeMillis = duration,
                        lastTimeUsed = endTime, // Approximate
                        category = getAppCategory(pkg)
                    )
                } else null
            }
            .sortedByDescending { it.usageTimeMillis }
    }
    
    

    private fun getAppName(packageName: String): String {
        return try {
            val appInfo = context.packageManager.getApplicationInfo(packageName, 0)
            context.packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            ""
        }
    }

    private fun getAppCategory(packageName: String): String {
        return try {
            val appInfo = context.packageManager.getApplicationInfo(packageName, 0)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                ApplicationInfo.getCategoryTitle(context, appInfo.category)?.toString() ?: "Unknown"
            } else {
                "Unknown"
            }
        } catch (e: PackageManager.NameNotFoundException) {
            "Unknown"
        }
    }

    /**
     * Returns a list of all installed apps on the device, regardless of usage.
     */
    fun getAllInstalledApps(): List<AppUsageInfo> {
        val pm = context.packageManager
        val installedApps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        
        // Get limits/usage to show real data
        val usageStats = getTodayUsageStats().associateBy { it.packageName }
        
        return installedApps
            .filter { pm.getLaunchIntentForPackage(it.packageName) != null } // Only show apps that can be launched by the user
            .map { appInfo ->
                val appName = pm.getApplicationLabel(appInfo).toString()
                val existingStat = usageStats[appInfo.packageName]
                
                AppUsageInfo(
                    packageName = appInfo.packageName,
                    appName = appName,
                    usageTimeMillis = existingStat?.usageTimeMillis ?: 0,
                    lastTimeUsed = existingStat?.lastTimeUsed ?: 0,
                    category = getAppCategory(appInfo.packageName)
                )
            }
            .sortedBy { it.appName }
    }
}
