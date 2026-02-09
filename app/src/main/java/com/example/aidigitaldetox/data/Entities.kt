package com.example.aidigitaldetox.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usage_logs")
data class UsageLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val packageName: String,
    val appName: String,
    val date: String, // YYYY-MM-DD
    val durationInMillis: Long,
    val openCount: Int
)

@Entity(tableName = "app_sessions")
data class AppSession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val packageName: String,
    val startTime: Long,
    val endTime: Long,
    val duration: Long
)

@Entity(tableName = "challenge_logs")
data class ChallengeLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val challengeType: String, // COGNITIVE, PHYSICAL
    val difficulty: String, // EASY, MEDIUM, HARD
    val success: Boolean,
    val timeTaken: Long
)

@Entity(tableName = "emergency_logs")
data class EmergencyLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val packageName: String
)

@Entity(tableName = "behavior_profiles")
data class BehaviorProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val totalUsageTime: Long,
    val emergencyUnlockCount: Int,
    val challengesCompleted: Int,
    val challengesFailed: Int,
    val averageDifficulty: Int, // 1-10
    val addictionScore: Float // 0-100
)
