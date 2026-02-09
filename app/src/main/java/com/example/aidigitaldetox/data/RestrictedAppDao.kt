package com.example.aidigitaldetox.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RestrictedAppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(app: RestrictedApp)

    @Delete
    suspend fun delete(app: RestrictedApp)

    @Query("SELECT * FROM restricted_apps WHERE packageName = :packageName")
    suspend fun getRestrictedApp(packageName: String): RestrictedApp?

    @Query("SELECT * FROM restricted_apps")
    fun getAllRestrictedApps(): Flow<List<RestrictedApp>>

    @Query("UPDATE restricted_apps SET todayUsageMs = :usage, isLocked = :isLocked, lastUpdated = :lastUpdated WHERE packageName = :packageName")
    suspend fun updateUsageAndLock(packageName: String, usage: Long, isLocked: Boolean, lastUpdated: Long)
}
