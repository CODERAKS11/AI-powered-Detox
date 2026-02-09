package com.example.aidigitaldetox.di

import com.example.aidigitaldetox.data.AddictionRiskRepository
import com.example.aidigitaldetox.data.UsageRepository
import com.example.aidigitaldetox.domain.ChallengeManager
import com.example.aidigitaldetox.data.AppLockRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface OverlayEntryPoint {
    fun getAddictionRiskRepository(): AddictionRiskRepository
    fun getUsageRepository(): UsageRepository
    fun getChallengeManager(): ChallengeManager
    fun getAppLockRepository(): AppLockRepository
    fun getEmergencyUnlockManager(): com.example.aidigitaldetox.domain.EmergencyUnlockManager
}
