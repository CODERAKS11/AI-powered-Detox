package com.example.aidigitaldetox.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object OverlayStateManager {
    private val _isOverlayActive = MutableStateFlow(false)
    val isOverlayActive: StateFlow<Boolean> = _isOverlayActive

    fun setOverlayActive(active: Boolean) {
        _isOverlayActive.value = active
    }
}
