package com.example.aidigitaldetox.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aidigitaldetox.service.AppMonitorService
import com.example.aidigitaldetox.util.AppUsageInfo
import com.example.aidigitaldetox.util.PermissionUtils

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsState()

    var hasUsageStats by remember { mutableStateOf(false) }
    var hasOverlay by remember { mutableStateOf(false) }
    var isAccessibilityEnabled by remember { mutableStateOf(false) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasUsageStats = PermissionUtils.hasUsageStatsPermission(context)
                hasOverlay = PermissionUtils.hasOverlayPermission(context)
                isAccessibilityEnabled = PermissionUtils.isAccessibilityServiceEnabled(context, AppMonitorService::class.java)
                
                if (hasUsageStats) viewModel.refreshStats()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Stats Card
        Card(
            modifier = Modifier.fillMaxWidth().height(160.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
             elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total Screen Time",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = formatTime(uiState.totalScreenTime),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Permissions Warning
        if (!hasUsageStats || !hasOverlay || !isAccessibilityEnabled) {
             PermissionWarningCard(context, hasUsageStats, hasOverlay, isAccessibilityEnabled)
             Spacer(Modifier.height(24.dp))
        }

        // Charts
        if (uiState.categoryData.isNotEmpty()) {
            Text("Usage Breakdown", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.align(Alignment.Start))
            Spacer(Modifier.height(12.dp))
            DonutChart(uiState.categoryData)
            Spacer(Modifier.height(24.dp))
        }

        // Top Apps
        Text("Most Used Apps", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.align(Alignment.Start))
        Spacer(Modifier.height(12.dp))

        uiState.topApps.forEach { app ->
            AppUsageItem(app)
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun PermissionWarningCard(
    context: android.content.Context,
    hasUsageStats: Boolean,
    hasOverlay: Boolean,
    isAccessibilityEnabled: Boolean
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                "Permissions Required",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(Modifier.height(8.dp))
            if (!hasUsageStats) {
                Button(onClick = { context.startActivity(PermissionUtils.getUsageStatsIntent()) }, modifier = Modifier.fillMaxWidth()) { Text("Grant Usage Access") }
            }
            if (!hasOverlay) {
                Button(onClick = { context.startActivity(PermissionUtils.getOverlayIntent(context)) }, modifier = Modifier.fillMaxWidth()) { Text("Grant Overlay Permission") }
            }
            if (!isAccessibilityEnabled) {
                Button(onClick = { context.startActivity(PermissionUtils.getAccessibilityIntent()) }, modifier = Modifier.fillMaxWidth()) { Text("Enable Accessibility") }
            }
        }
    }
}

@Composable
fun AppUsageItem(app: AppUsageInfo) {
    Card(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(app.appName.take(1), color = Color.White)
            }

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(app.appName, style = MaterialTheme.typography.bodyLarge)
                Text(app.category, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }

            Text(formatTime(app.usageTimeMillis), style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun DonutChart(data: Map<String, Long>) {
    val total = data.values.sum().toFloat()
    val colors = listOf(Color.Blue, Color.Green, Color.Red, Color.Yellow, Color.Magenta)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Canvas(modifier = Modifier.size(120.dp)) {
            var startAngle = -90f
            var colorIndex = 0

            data.forEach { (_, value) ->
                val sweepAngle = (value / total) * 360f
                drawArc(
                    color = colors[colorIndex % colors.size],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = 30f)
                )
                startAngle += sweepAngle
                colorIndex++
            }
        }

        Column {
            var colorIndex = 0
            data.forEach { (category, _) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(12.dp).background(colors[colorIndex % colors.size]))
                    Spacer(Modifier.width(8.dp))
                    Text(category, style = MaterialTheme.typography.bodySmall)
                }
                colorIndex++
            }
        }
    }
}

fun formatTime(millis: Long): String {
    val hours = java.util.concurrent.TimeUnit.MILLISECONDS.toHours(millis)
    val minutes = java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(millis) % 60
    return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
}
