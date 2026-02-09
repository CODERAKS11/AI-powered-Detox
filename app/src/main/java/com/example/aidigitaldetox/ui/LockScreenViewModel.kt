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
            
            // 1. Get Base Risk from ML Model
            val mlPrediction = repository.getRiskAssessment()
            
            // 2. Calculate Hybrid Difficulty
            // Base (1-3) + Manual Increment (0+)
            val baseDifficulty = mlPrediction.recommendedDifficulty
            val hybridDifficultyLevel = (baseDifficulty + currentDifficultyLevel).coerceAtMost(3)
            
            // 3. Create Hybrid Prediction Result
            val hybridPrediction = mlPrediction.copy(
                recommendedDifficulty = hybridDifficultyLevel,
                // Boost doomscroll probability if we have manual increments
                doomscrollProbability = (mlPrediction.doomscrollProbability + (currentDifficultyLevel * 0.1f)).coerceAtMost(1f)
            )
            
            val config = challengeManager.generateChallenge(hybridPrediction)
            _challengeState.value = config
            challengeStartTime = System.currentTimeMillis()
            _isLoading.value = false
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
            extendLimit(1) // Challenge completion grants 1 minute
            
            // Increase difficulty for next time
            currentDifficultyLevel++
        }
    }

    private suspend fun extendLimit(minutes: Long) {
         appLockRepository.extendLimit(targetPackageName, minutes * 60 * 1000)
    }
}
