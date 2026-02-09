package com.example.aidigitaldetox.domain

import android.content.Context
import android.content.SharedPreferences
import java.util.Calendar
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext

class EmergencyUnlockManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("emergency_unlock_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_UNLOCK_COUNT = "unlock_count"
        private const val KEY_LAST_RESET_DAY = "last_reset_day"
        private const val MAX_UNLOCKS = 3
    }

    fun getRemainingUnlocks(): Int {
        checkAndResetDaily()
        val count = prefs.getInt(KEY_UNLOCK_COUNT, 0)
        return (MAX_UNLOCKS - count).coerceAtLeast(0)
    }

    fun useEmergencyUnlock(): Boolean {
        checkAndResetDaily()
        val count = prefs.getInt(KEY_UNLOCK_COUNT, 0)
        if (count < MAX_UNLOCKS) {
            prefs.edit().putInt(KEY_UNLOCK_COUNT, count + 1).apply()
            return true
        }
        return false
    }

    private fun checkAndResetDaily() {
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val lastDay = prefs.getInt(KEY_LAST_RESET_DAY, -1)

        if (currentDay != lastDay) {
            prefs.edit()
                .putInt(KEY_LAST_RESET_DAY, currentDay)
                .putInt(KEY_UNLOCK_COUNT, 0)
                .apply()
        }
    }
}
