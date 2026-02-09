package com.example.aidigitaldetox.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import com.example.aidigitaldetox.data.RestrictedApp

@Composable
fun RestrictedAppsScreen(
    onBack: () -> Unit,
    viewModel: RestrictedAppsViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val restrictedApps by viewModel.restrictedApps.collectAsState()
    val installedApps by viewModel.installedApps.collectAsState()
    
    var showAddDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = onBack) { Text("Back") }
            Spacer(Modifier.width(16.dp))
            Text("Restricted Apps", style = MaterialTheme.typography.headlineMedium)
        }
        
        Spacer(Modifier.height(16.dp))
        
        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add New Limit")
        }
        
        Spacer(Modifier.height(16.dp))
        
        LazyColumn {
            items(restrictedApps) { app ->
                RestrictedAppItem(
                    app = app, 
                    onDelete = { viewModel.removeLimit(app.packageName) },
                    onAdjustLimit = { delta -> viewModel.adjustLimit(app.packageName, delta) }
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
    
    if (showAddDialog) {
        AddLimitDialog(
            apps = installedApps,
            onDismiss = { showAddDialog = false },
            onConfirm = { pkg, name, limit ->
                viewModel.setLimit(pkg, name, limit)
                showAddDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLimitDialog(
    apps: List<com.example.aidigitaldetox.util.AppUsageInfo>,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Long) -> Unit
) {
    var selectedApp by remember { mutableStateOf<com.example.aidigitaldetox.util.AppUsageInfo?>(null) }
    var durationMinutes by remember { mutableStateOf(60f) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredApps = remember(searchQuery, apps) {
        if (searchQuery.isBlank()) apps
        else apps.filter { it.appName.contains(searchQuery, ignoreCase = true) }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set App Limit") },
        text = {
            Column {
                if (selectedApp == null) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search App") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))
                    LazyColumn(modifier = Modifier.height(300.dp)) {
                        items(filteredApps) { app ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { 
                                        selectedApp = app 
                                        // Default limit to current usage + 5 mins (or 60 if unused)
                                        val currentUsageMin = app.usageTimeMillis / 60000f
                                        durationMinutes = if (currentUsageMin > 0) currentUsageMin else 60f
                                    }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(app.appName, style = MaterialTheme.typography.bodyLarge)
                            }
                            Divider()
                        }
                    }
                } else {
                    Text("App: ${selectedApp?.appName}", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(16.dp))
                    Text("Time Limit: ${durationMinutes.toInt()} mins (${durationMinutes.toInt() / 60}h ${durationMinutes.toInt() % 60}m)")
                    Slider(
                        value = durationMinutes,
                        onValueChange = { durationMinutes = it },
                        valueRange = 1f..300f
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedApp?.let {
                        onConfirm(it.packageName, it.appName, durationMinutes.toLong())
                    }
                },
                enabled = selectedApp != null
            ) {
                Text("Set Limit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun RestrictedAppItem(
    app: RestrictedApp, 
    onDelete: () -> Unit,
    onAdjustLimit: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (app.isLocked) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(Modifier.padding(16.dp).fillMaxWidth()) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(app.appName, style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove Limit", tint = MaterialTheme.colorScheme.error)
                }
            }
            
            Spacer(Modifier.height(8.dp))
            
            val usedMin = app.todayUsageMs / 60000
            val limitMin = app.dailyLimitMs / 60000
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                 Text(
                    text = "Usage: ${usedMin}m / ${limitMin}m", 
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (app.isLocked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onAdjustLimit(-1) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text("-", style = MaterialTheme.typography.headlineSmall)
                    }
                    
                    Spacer(Modifier.width(8.dp))
                    
                    IconButton(
                        onClick = { onAdjustLimit(1) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text("+", style = MaterialTheme.typography.headlineSmall)
                    }
                }
            }
        }
    }
}
