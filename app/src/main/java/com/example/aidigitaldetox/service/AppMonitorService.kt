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
            var ticker = 0
            while (true) {
                checkRealTimeUsage()
                
                // Periodic commit every 60 seconds (approx 300 cycles * 200ms)
                // This ensures UI updates and data persistence even during long sessions
                ticker++
                if (ticker >= 300) {
                   lastPackageName?.let { commitUsageToDb(it) }
                   ticker = 0
                }

                // Ultra-fast check (200ms) to ensure we catch the exact second the limit expires
                kotlinx.coroutines.delay(200) 
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


    private fun showWarningOverlay(packageName: String, usageMs: Long) {
        // Prevent multiple warnings
        if (currentRestrictedApp?.warningShown == true) return

        val windowManager = getSystemService(WINDOW_SERVICE) as android.view.WindowManager
        val params = android.view.WindowManager.LayoutParams(
            android.view.WindowManager.LayoutParams.MATCH_PARENT,
            android.view.WindowManager.LayoutParams.WRAP_CONTENT,
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) 
                android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY 
            else 
                android.view.WindowManager.LayoutParams.TYPE_PHONE,
            android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or 
            android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            android.graphics.PixelFormat.TRANSLUCENT
        )
        params.gravity = android.view.Gravity.TOP
        
        val context = this
        val view = android.widget.FrameLayout(context).apply {
            background = android.graphics.drawable.GradientDrawable().apply {
                setColor(android.graphics.Color.parseColor("#CC000000")) // Semi-transparent black
                cornerRadius = 16f
            }
            setPadding(32, 32, 32, 32)
            
            val message = android.widget.TextView(context).apply {
                val minutes = java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(usageMs)
                val timeText = if (minutes < 1) {
                    "${java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(usageMs)} sec"
                } else {
                    "$minutes min"
                }
                text = "⚠️ Warning: You've used $timeText today."
                textSize = 16f
                setTextColor(android.graphics.Color.WHITE)
                gravity = android.view.Gravity.CENTER
            }
            addView(message)
        }
        
        try {
            windowManager.addView(view, params)
            
            // Mark as shown in DB
            serviceScope.launch {
                appLockRepository.setWarningShown(packageName)
                currentRestrictedApp = currentRestrictedApp?.copy(warningShown = true)
            }
            
            // Auto-dismiss after 5 seconds
            serviceScope.launch {
                kotlinx.coroutines.delay(5000)
                try {
                    windowManager.removeView(view)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun checkRealTimeUsage() {
        val packageName = lastPackageName ?: return
        val app = currentRestrictedApp ?: return // No restriction
        
        // Skip checking if it's our own app or launcher/system ui
        if (packageName == this.packageName || packageName == "com.android.systemui") return

        val now = System.currentTimeMillis()
        val sessionDuration = now - sessionStartTime
        
        // Calculate total usage: Previous Usage + Current Session
        val projectedUsage = app.todayUsageMs + sessionDuration
        
        // Phase 2: Warning Zone (75% - 99%)
        if (projectedUsage >= (app.dailyLimitMs * 0.75) && projectedUsage < app.dailyLimitMs) {
            if (!app.warningShown) {
                 // Run on UI thread (Service is UI thread mostly, but check context)
                 kotlinx.coroutines.withContext(Dispatchers.Main) {
                     showWarningOverlay(packageName, projectedUsage)
                 }
            }
        }

        // Phase 3: Limit Reached
        if (projectedUsage >= app.dailyLimitMs) {
            // Limit reached!
            
            // 1. Commit usage immediately
            commitUsageToDb(packageName)
            
            // 2. Mark as locked LOCALLY to prevent race conditions
            currentRestrictedApp = currentRestrictedApp?.copy(isLocked = true, todayUsageMs = projectedUsage)
            
            // 3. Show overlay IMMEDIATELY
            showBlockOverlay(packageName)
            
            // 4. Force refresh from DB to ensure persistence
            // But don't wait for it to block
            serviceScope.launch {
                appLockRepository.addUsage(packageName, 0) // Just to trigger DB update if needed
                currentRestrictedApp = appLockRepository.getRestrictedApp(packageName)
            }
        }
    }

    private suspend fun commitUsageToDb(packageName: String) {
        val now = System.currentTimeMillis()
        val delta = now - sessionStartTime
        
        if (delta > 0) {
            // Add usage to DB (returns true if locked)
            val isLocked = appLockRepository.addUsage(packageName, delta)
            
            // Advance session start time to now
            sessionStartTime = now
            
            // Update local cache
            if (packageName == lastPackageName) {
                 // Update the cached object so the next loop cycle uses accurate data
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
            
            // 3. Fetch restriction data for the NEW app
            // CRITICAL: Get fresh data from DB, which includes the new limit from "Extend" action
            val freshAppConfig = appLockRepository.getRestrictedApp(newPackageName)
            currentRestrictedApp = freshAppConfig

            // 4. Immediately check if new app is locked
            if (freshAppConfig?.isLocked == true) {
                // Double check usage vs limit in case "isLocked" flag is stale but math says otherwise
                if (freshAppConfig.todayUsageMs >= freshAppConfig.dailyLimitMs) {
                     showBlockOverlay(newPackageName)
                }
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
