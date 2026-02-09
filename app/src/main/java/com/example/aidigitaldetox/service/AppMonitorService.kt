package com.example.aidigitaldetox.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.example.aidigitaldetox.data.AppLockRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AppMonitorService : AccessibilityService() {

    @Inject
    lateinit var appLockRepository: AppLockRepository
    
    @Inject
    lateinit var appUsageHelper: com.example.aidigitaldetox.util.AppUsageHelper

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    // Valid only for the currently active session
    private var lastPackageName: String? = null
    private var sessionStartTime: Long = 0L
    
    // Cache the current app's restriction data to avoid DB hits every 200ms
    private var currentRestrictedApp: com.example.aidigitaldetox.data.RestrictedApp? = null
    
    private var monitorJob: kotlinx.coroutines.Job? = null
    
    // Track overlay state to prevent spamming startService
    private var isOverlayShowing = false

    override fun onServiceConnected() {
        super.onServiceConnected()
        sessionStartTime = System.currentTimeMillis()
        startMonitoring()
    }

    private fun startMonitoring() {
        monitorJob?.cancel()
        monitorJob = serviceScope.launch {
            while (true) {
                checkRealTimeUsage()
                kotlinx.coroutines.delay(200) // High-frequency check (5Hz)
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString() ?: return
            
            // If strictly different package
            if (packageName != lastPackageName) {
                handleAppSwitch(packageName)
            }
        }
    }



    private suspend fun checkRealTimeUsage() {
        val packageName = lastPackageName ?: return
        val app = currentRestrictedApp ?: return // No restriction, no need to check high-frequency
        
        // Skip checking if it's our own app or launcher/system ui
        if (packageName == this.packageName || packageName == "com.android.systemui") return

        val now = System.currentTimeMillis()
        val sessionDuration = now - sessionStartTime
        
        // Calculate total usage including current session
        val projectedUsage = app.todayUsageMs + sessionDuration
        
        if (projectedUsage >= app.dailyLimitMs) {
            // Limit reached!
            // 1. Commit to DB immediately to persist the lock
            commitUsageToDb(packageName)
            
            // 2. Refresh cache (it should now be locked)
            currentRestrictedApp = appLockRepository.getRestrictedApp(packageName)
            
            // 3. Show overlay
            showBlockOverlay(packageName)
        }
    }

    private suspend fun commitUsageToDb(packageName: String) {
        val now = System.currentTimeMillis()
        val delta = now - sessionStartTime
        
        if (delta > 0) {
            // Add usage to DB (returns true if locked)
            val isLocked = appLockRepository.addUsage(packageName, delta)
            
            // Advance session start time to now, so we don't double count
            sessionStartTime = now
            
            // Update local cache if we are still on the same app
            if (packageName == lastPackageName) {
                 currentRestrictedApp = currentRestrictedApp?.copy(
                     todayUsageMs = currentRestrictedApp!!.todayUsageMs + delta,
                     isLocked = isLocked
                 )
            }
        }
    }



    private fun showBlockOverlay(packageName: String) {
        // Use shared state to prevent spam
        if (com.example.aidigitaldetox.util.OverlayStateManager.isOverlayActive.value && lastPackageName == packageName) return

        val intent = Intent(this, OverlayService::class.java).apply {
            putExtra("PACKAGE_NAME", packageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }
    
    // Reset overlay flag when switching apps
    private fun handleAppSwitch(newPackageName: String) {
        serviceScope.launch {
             // 1. Commit usage for the previous app (flush to DB)
            lastPackageName?.let { previousPackage ->
                commitUsageToDb(previousPackage)
            }

            // 2. Update state to new app
            lastPackageName = newPackageName
            sessionStartTime = System.currentTimeMillis()
            
            // 3. Fetch restriction data for the NEW app (once per session)
            currentRestrictedApp = appLockRepository.getRestrictedApp(newPackageName)

            // 4. Immediately check if new app is already restricted/locked
            if (currentRestrictedApp?.isLocked == true) {
                showBlockOverlay(newPackageName)
            }
        }
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onInterrupt() {
        // Accessibility service interrupted
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
