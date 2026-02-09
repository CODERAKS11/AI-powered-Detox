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

        val stats: List<UsageStats> =
            usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                startTime,
                endTime
            ) ?: emptyList()

        return stats
            .filter { it.totalTimeInForeground > 0 }
            .mapNotNull {
                val appName = getAppName(it.packageName)
                if (appName.isNotEmpty()) {
                    AppUsageInfo(
                        packageName = it.packageName,
                        appName = appName,
                        usageTimeMillis = it.totalTimeInForeground,
                        lastTimeUsed = it.lastTimeUsed,
                        category = getAppCategory(it.packageName)
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
