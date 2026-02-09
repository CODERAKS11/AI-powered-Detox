package com.example.aidigitaldetox.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.aidigitaldetox.util.Converters

@Database(
    entities = [
        UsageLog::class,
        BehaviorProfile::class,
        AppSession::class,
        ChallengeLog::class,
        EmergencyLog::class,
        RestrictedApp::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usageDao(): UsageDao

    abstract fun restrictedAppDao(): RestrictedAppDao
}