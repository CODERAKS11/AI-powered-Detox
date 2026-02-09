package com.example.aidigitaldetox.service

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import com.example.aidigitaldetox.ui.LockScreen
import com.example.aidigitaldetox.ui.LockScreenViewModel

class OverlayService : Service(), LifecycleOwner, ViewModelStoreOwner, SavedStateRegistryOwner {

    private lateinit var windowManager: WindowManager
    private var overlayView: ComposeView? = null

    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    private val store = ViewModelStore()

    override val lifecycle: Lifecycle get() = lifecycleRegistry
    override val savedStateRegistry: SavedStateRegistry get() = savedStateRegistryController.savedStateRegistry
    override val viewModelStore: ViewModelStore get() = store

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        
        // Notify State Manager
        com.example.aidigitaldetox.util.OverlayStateManager.setOverlayActive(true)
    }
    
    // ... onStartCommand ...

    override fun onDestroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        store.clear()
        removeOverlay()
        
        // Notify State Manager
        com.example.aidigitaldetox.util.OverlayStateManager.setOverlayActive(false)
        
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = androidx.core.app.NotificationCompat.Builder(this, "OVERLAY_CHANNEL")
            .setContentTitle("Digital Detox Verify")
            .setContentText("Access Restricted App Overlay Active")
            .setSmallIcon(android.R.drawable.ic_lock_idle_lock)
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
            .setCategory(androidx.core.app.NotificationCompat.CATEGORY_SERVICE)
            .build()
            
        startForeground(1, notification)
        
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)

        val packageName = intent?.getStringExtra("PACKAGE_NAME")
        if (packageName != null) {
            showOverlay(packageName)
        }

        return START_NOT_STICKY
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = android.app.NotificationChannel(
                "OVERLAY_CHANNEL",
                "Overlay Service Channel",
                android.app.NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(android.app.NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun showOverlay(packageName: String) {
        if (overlayView != null) return

        // 1. Get Dependencies via EntryPoint
        val entryPoint = dagger.hilt.android.EntryPointAccessors.fromApplication(
            applicationContext,
            com.example.aidigitaldetox.di.OverlayEntryPoint::class.java
        )

        val addictionRepo = entryPoint.getAddictionRiskRepository()
        val usageRepo = entryPoint.getUsageRepository()
        val challengeManager = entryPoint.getChallengeManager()
        val appLockRepo = entryPoint.getAppLockRepository()
        val emergencyManager = entryPoint.getEmergencyUnlockManager()

        // 2. Create Custom Factory
        val factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(LockScreenViewModel::class.java)) {
                    return LockScreenViewModel(
                        repository = addictionRepo,
                        usageRepository = usageRepo,
                        challengeManager = challengeManager,
                        appLockRepository = appLockRepo,
                        emergencyUnlockManager = emergencyManager
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        // 3. Obtain ViewModel
        val provider = ViewModelProvider(store, factory)
        val viewModel: LockScreenViewModel = provider.get(LockScreenViewModel::class.java)

        overlayView = ComposeView(this).apply {
            setViewTreeSavedStateRegistryOwner(this@OverlayService)
            setViewTreeLifecycleOwner(this@OverlayService)
            setViewTreeViewModelStoreOwner(this@OverlayService)

            setContent {
                LockScreen(
                    packageName = packageName,
                    viewModel = viewModel,
                    onUnlock = { removeOverlay() },
                    onExit = { 
                        // Go Home
                        val startMain = Intent(Intent.ACTION_MAIN)
                        startMain.addCategory(Intent.CATEGORY_HOME)
                        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(startMain)
                        
                        // Remove Overlay
                        removeOverlay() 
                    }
                )
            }
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        params.gravity = Gravity.CENTER

        try {
            windowManager.addView(overlayView, params)
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun removeOverlay() {
        if (overlayView != null) {
            try {
                windowManager.removeView(overlayView)
                overlayView = null
                lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        // CRITICAL: Stop the service so onDestroy is called and state is reset
        stopSelf()
    }
}
