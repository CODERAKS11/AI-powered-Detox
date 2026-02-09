package com.example.aidigitaldetox.di

import android.content.Context
import androidx.room.Room
import com.example.aidigitaldetox.data.AppDatabase
import com.example.aidigitaldetox.data.UsageDao
import com.example.aidigitaldetox.data.RestrictedAppDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "detox_database"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideUsageDao(database: AppDatabase): UsageDao {
        return database.usageDao()
    }

    @Provides
    fun provideRestrictedAppDao(database: AppDatabase): RestrictedAppDao {
        return database.restrictedAppDao()
    }
}
