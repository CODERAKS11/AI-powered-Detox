package com.example.aidigitaldetox.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aidigitaldetox.data.AddictionRiskRepository
import com.example.aidigitaldetox.domain.ChallengeConfig
import com.example.aidigitaldetox.ml.PredictionResult
import com.example.aidigitaldetox.domain.ChallengeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LockScreenViewModel @Inject constructor(
    private val repository: AddictionRiskRepository,
    private val usageRepository: com.example.aidigitaldetox.data.UsageRepository,
    private val challengeManager: ChallengeManager,
    private val appLockRepository: com.example.aidigitaldetox.data.AppLockRepository,
    private val emergencyUnlockManager: com.example.aidigitaldetox.domain.EmergencyUnlockManager
) : ViewModel() {

    private val _challengeState = MutableStateFlow<ChallengeConfig?>(null)
    val challengeState: StateFlow<ChallengeConfig?> = _challengeState

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _emergencyUnlockState = MutableStateFlow(3)
    val emergencyUnlockState: StateFlow<Int> = _emergencyUnlockState

    private var challengeStartTime: Long = 0
    private var isEmergencyMode: Boolean = false
    private var targetPackageName: String = ""
    
    // Difficulty scaling: 0 = base, increments on each extension
    private var currentDifficultyLevel = 0

    fun setTargetPackage(packageName: String) {
        this.targetPackageName = packageName
        refreshEmergencyCount()
    }
    
    private fun refreshEmergencyCount() {
        _emergencyUnlockState.value = emergencyUnlockManager.getRemainingUnlocks()
    }

    fun loadChallenge() {
        if (_challengeState.value != null && !_isLoading.value) return // Prevent reload on recomposition
        
        viewModelScope.launch {
            _isLoading.value = true
            isEmergencyMode = false
            
            try {
                // 1. Fetch current escalation level (extension count) from DB
                // Use default 0 if DB fails or returns null
                val appConfig = try {
                    appLockRepository.getRestrictedApp(targetPackageName)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
                val extensionCount = appConfig?.extensionCount ?: 0

                // 2. Generate Challenge based on Phase logic (Instant, no ML)
                val config = challengeManager.generateChallenge(extensionCount)
                _challengeState.value = config
            } catch (e: Exception) {
                e.printStackTrace()
                // Fallback challenge to prevent UI stuck state
                _challengeState.value = com.example.aidigitaldetox.domain.ChallengeConfig(
                    com.example.aidigitaldetox.domain.ChallengeType.MATH,
                    com.example.aidigitaldetox.domain.ChallengeDifficulty.EASY,
                    "Solve 1 problem (Fallback)",
                    1
                )
            } finally {
                challengeStartTime = System.currentTimeMillis()
                _isLoading.value = false
            }
        }
    }

    fun triggerEmergencyUnlock() {
        if (emergencyUnlockManager.useEmergencyUnlock()) {
             viewModelScope.launch {
                // Emergency grant: 2 minutes
                extendLimit(2)
                refreshEmergencyCount()
             }
        }
    }

    fun onChallengeComplete() {
        val config = _challengeState.value ?: return
        val timeTaken = System.currentTimeMillis() - challengeStartTime
        
        viewModelScope.launch {
            usageRepository.logChallenge(
                challengeType = config.type.name,
                difficulty = config.difficulty.name,
                success = true,
                timeTaken = timeTaken
            )
            
            // Reward: Extension
            val rewardMs = 30 * 1000L // 30 seconds
            extendLimit(rewardMs) 
            
            // Increase extension count in DB for next time
            appLockRepository.incrementExtensionCount(targetPackageName)
        }
    }

    private suspend fun extendLimit(addedTimeMs: Long) {
         appLockRepository.extendLimit(targetPackageName, addedTimeMs)
    }
}
