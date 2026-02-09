package com.example.aidigitaldetox.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsageDao {
    @Query("SELECT * FROM usage_logs WHERE date = :date")
    fun getUsageForDate(date: String): Flow<List<UsageLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsageLog(log: UsageLog)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppSession(session: AppSession)

    @Query("SELECT * FROM app_sessions WHERE startTime >= :startTime ORDER BY startTime DESC")
    fun getSessionsSince(startTime: Long): Flow<List<AppSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallengeLog(log: ChallengeLog)

    @Query("SELECT * FROM challenge_logs ORDER BY timestamp DESC")
    fun getChallengeHistory(): Flow<List<ChallengeLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergencyLog(log: EmergencyLog)

    @Query("SELECT * FROM behavior_profiles ORDER BY id DESC LIMIT 30")
    fun getRecentHistory(): Flow<List<BehaviorProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBehaviorProfile(profile: BehaviorProfile)
    
    @Query("SELECT SUM(durationInMillis) FROM usage_logs WHERE date = :date")
    suspend fun getTotalUsageForDate(date: String): Long?
}
