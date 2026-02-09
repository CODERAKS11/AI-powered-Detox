package com.example.aidigitaldetox.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
            .background(Color.Black.copy(alpha = 0.95f)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Digital Detox Lock", color = Color.White, fontSize = 28.sp)
        Text("Accessing: $packageName", color = Color.Gray, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(30.dp))

        if (isLoading) {
            CircularProgressIndicator(color = Color.White)
            Text("Analyzing Addiction Risk...", color = Color.White)
        } else {

            challengeConfig?.let { config ->

                Text(config.description, color = Color.Yellow, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(20.dp))

                when (config.type) {

                    ChallengeType.MATH -> {
                        MathChallengeScreen(config.targetCount) {
                            viewModel.onChallengeComplete()
                            onUnlock()
                        }
                    }

                    ChallengeType.STEP -> {
                        StepChallengeScreen(config.targetCount) {
                            viewModel.onChallengeComplete()
                            onUnlock()
                        }
                    }

                    ChallengeType.SQUAT -> {
                        SquatChallengeScreen(config.targetCount) {
                            viewModel.onChallengeComplete()
                            onUnlock()
                        }
                    }

                    ChallengeType.MEMORY -> {
                        VisualMemoryChallengeScreen(config.targetCount) {
                            viewModel.onChallengeComplete()
                            onUnlock()
                        }
                    }
                    ChallengeType.PATTERN -> {
                        // Add your pattern challenge composable here
                        // For now, we'll just unlock
                        onUnlock()
                    }
                    ChallengeType.HOLD_STILL -> {
                        // Add your hold still challenge composable here
                        // For now, we'll just unlock
                        onUnlock()
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

            val emergencyCount by viewModel.emergencyUnlockState.collectAsState()

            if (emergencyCount > 0) {
                TextButton(
                    onClick = { viewModel.triggerEmergencyUnlock() }
                ) {
                    Text("Emergency Unlock ($emergencyCount left)", color = Color.Red)
                }
            } else {
                Text("No Emergency Unlocks Left", color = Color.Gray, fontSize = 12.sp)
            }

            // "Extend" button removed as per strict detox rules. 
            // Users must complete the challenge above to unlock.
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        TextButton(onClick = { onExit() }) {
            Text("I'll use it later (Exit)", color = Color.White.copy(alpha = 0.7f))
        }
    }
}
