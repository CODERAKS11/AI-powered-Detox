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
                }

                if (selectedApp != null) {
                    // Calculation: durationMinutes <-> hours/minutes
                    val hours = (durationMinutes / 60).toInt()
                    val minutes = (durationMinutes % 60).toInt()
                    
                    var showHourDropdown by remember { mutableStateOf(false) }
                    var showMinuteDropdown by remember { mutableStateOf(false) }

                    Text(
                        text = "Set Daily Limit: ${hours}h ${minutes}m",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    // Dropdowns Row
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Hour Dropdown
                        Box {
                            OutlinedButton(onClick = { showHourDropdown = true }) {
                                Text("${hours} hr")
                            }
                            DropdownMenu(
                                expanded = showHourDropdown,
                                onDismissRequest = { showHourDropdown = false }
                            ) {
                                (0..23).forEach { h ->
                                    DropdownMenuItem(
                                        text = { Text("$h hr") },
                                        onClick = {
                                            durationMinutes = (h * 60 + minutes).toFloat()
                                            showHourDropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        // Minute Dropdown
                        Box {
                            OutlinedButton(onClick = { showMinuteDropdown = true }) {
                                Text("${minutes} min")
                            }
                            DropdownMenu(
                                expanded = showMinuteDropdown,
                                onDismissRequest = { showMinuteDropdown = false }
                            ) {
                                (0..59).forEach { m ->
                                    DropdownMenuItem(
                                        text = { Text("$m min") },
                                        onClick = {
                                            durationMinutes = (hours * 60 + m).toFloat()
                                            showMinuteDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Slider with Extended Range (up to 3 hours)
                    Slider(
                        value = durationMinutes,
                        onValueChange = { durationMinutes = it },
                        valueRange = 1f..180f, 
                        steps = 179
                    )
                    
                    // Fine-tune Buttons
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        IconButton(
                            onClick = { durationMinutes = (durationMinutes - 1).coerceAtLeast(1f) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Text("-", style = MaterialTheme.typography.headlineSmall)
                        }
                        
                        Spacer(Modifier.width(16.dp))
                        
                        IconButton(
                            onClick = { durationMinutes = (durationMinutes + 1) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Text("+", style = MaterialTheme.typography.headlineSmall)
                        }
                    }
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
