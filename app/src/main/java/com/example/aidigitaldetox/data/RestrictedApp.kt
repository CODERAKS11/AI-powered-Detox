
package com.example.aidigitaldetox.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restricted_apps")
data class RestrictedApp(
    @PrimaryKey val packageName: String,
    val appName: String,
    val dailyLimitMs: Long,
    val todayUsageMs: Long,
    val isLocked: Boolean,
    val lastUpdated: Long,
    val extensionCount: Int = 0,
    val warningShown: Boolean = false
)
