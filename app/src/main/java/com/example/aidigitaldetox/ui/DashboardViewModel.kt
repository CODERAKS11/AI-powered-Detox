package com.example.aidigitaldetox.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aidigitaldetox.util.AppUsageHelper
import com.example.aidigitaldetox.util.AppUsageInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DashboardUiState(
    val totalScreenTime: Long = 0,
    val topApps: List<AppUsageInfo> = emptyList(),
    val categoryData: Map<String, Long> = emptyMap(),
    val riskScore: Float = 45f // Placeholder, real impl would fetch from repo
)

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    
    private val usageHelper = AppUsageHelper(application)
    
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState

    fun refreshStats() {
        viewModelScope.launch(Dispatchers.IO) {
            val stats = usageHelper.getTodayUsageStats()
            val totalTime = stats.sumOf { it.usageTimeMillis }
            val topApps = stats.take(5)
            val categoryData = stats.groupBy { it.category }
                .mapValues { entry -> entry.value.sumOf { it.usageTimeMillis } }

            _uiState.value = DashboardUiState(
                totalScreenTime = totalTime,
                topApps = topApps,
                categoryData = categoryData
            )
        }
    }
}
