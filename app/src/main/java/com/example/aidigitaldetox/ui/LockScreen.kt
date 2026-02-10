package com.example.aidigitaldetox.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aidigitaldetox.domain.ChallengeType

@Composable
fun LockScreen(
    packageName: String,
    viewModel: LockScreenViewModel,
    onUnlock: () -> Unit,
    onExit: () -> Unit
) {
    val challengeConfig by viewModel.challengeState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setTargetPackage(packageName)
        viewModel.loadChallenge()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A237E), Color(0xFF000000)) 
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Main Content (Scrollable if needed)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text("DIGITAL DETOX", color = Color.White, style = MaterialTheme.typography.headlineSmall, letterSpacing = 2.sp)
            Text(packageName.substringAfterLast('.'), color = Color.Gray, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(30.dp))

            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
                Text("Analyzing Addiction Risk...", color = Color.White)
            } else {
                challengeConfig?.let { config ->
                    Text(config.description, color = Color.Yellow, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(20.dp))

                    when (config.type) {
                        ChallengeType.MATH -> MathChallengeScreen(config.targetCount) {
                            viewModel.onChallengeComplete { onUnlock() }
                        }
                        ChallengeType.STEP -> StepChallengeScreen(config.targetCount) {
                            viewModel.onChallengeComplete { onUnlock() }
                        }
                        ChallengeType.SQUAT -> SquatChallengeScreen(config.targetCount) {
                            viewModel.onChallengeComplete { onUnlock() }
                        }
                        ChallengeType.MEMORY -> VisualMemoryChallengeScreen(config.targetCount) {
                            viewModel.onChallengeComplete { onUnlock() }
                        }
                        else -> onUnlock()
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Bottom Section (Fixed)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val emergencyCount by viewModel.emergencyUnlockState.collectAsState()

            if (emergencyCount > 0) {
                TextButton(onClick = { viewModel.triggerEmergencyUnlock() }) {
                    Text("Emergency Unlock ($emergencyCount left)", color = Color.Red)
                }
            } else {
                Text("No Emergency Unlocks Left", color = Color.Gray, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedButton(
                onClick = { onExit() },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("GIVE UP (EXIT APP)")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
