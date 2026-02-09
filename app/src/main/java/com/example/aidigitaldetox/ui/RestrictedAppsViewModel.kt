package com.example.aidigitaldetox.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aidigitaldetox.data.AppLockRepository
import com.example.aidigitaldetox.data.RestrictedApp
import com.example.aidigitaldetox.util.AppUsageHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestrictedAppsViewModel @Inject constructor(
    application: Application,
    private val appLockRepository: AppLockRepository,
    private val usageHelper: AppUsageHelper
) : AndroidViewModel(application) {

    private val _restrictedApps = MutableStateFlow<List<RestrictedApp>>(emptyList())
    val restrictedApps: StateFlow<List<RestrictedApp>> = _restrictedApps
    
    // List of installed apps to choose from (simplified for now to just show all apps with usage)
    private val _installedApps = MutableStateFlow<List<com.example.aidigitaldetox.util.AppUsageInfo>>(emptyList())
    val installedApps: StateFlow<List<com.example.aidigitaldetox.util.AppUsageInfo>> = _installedApps

    init {
        viewModelScope.launch {
            appLockRepository.allRestrictedApps.collect {
                _restrictedApps.value = it
            }
        }
        loadInstalledApps()
    }

    private fun loadInstalledApps() {
        viewModelScope.launch(Dispatchers.IO) {
            // Load all installed apps so user can restrict any of them, not just used ones
            _installedApps.value = usageHelper.getAllInstalledApps()
        }
    }

    fun setLimit(packageName: String, appName: String, limitMinutes: Long) {
        val currentUsageStats = _installedApps.value.find { it.packageName == packageName }
        val currentUsageMs = currentUsageStats?.usageTimeMillis ?: 0L
        
        viewModelScope.launch {
            appLockRepository.addOrUpdateRestriction(packageName, appName, limitMinutes * 60 * 1000, currentUsageMs)
        }
    }

    fun removeLimit(packageName: String) {
        viewModelScope.launch {
            appLockRepository.removeRestriction(packageName)
        }
    }

    fun adjustLimit(packageName: String, deltaMinutes: Int) {
        val currentApp = _restrictedApps.value.find { it.packageName == packageName } ?: return
        val currentLimitMinutes = currentApp.dailyLimitMs / 60000
        val newLimit = (currentLimitMinutes + deltaMinutes).coerceAtLeast(2) // Minimum 2 mins
        
        setLimit(packageName, currentApp.appName, newLimit)
    }
}
