package com.example.aidigitaldetox.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun MathChallengeScreen(
    targetCount: Int,
    onComplete: () -> Unit
) {
    var solvedCount by remember { mutableIntStateOf(0) }
    var currentProblem by remember { mutableStateOf(generateMathProblem()) }
    var input by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Math Challenge: $solvedCount / $targetCount", color = Color.White)
        Spacer(modifier = Modifier.height(20.dp))
        Text("${currentProblem.first} = ?", fontSize = 32.sp, color = Color.White)
        
        OutlinedTextField(
            value = input,
            onValueChange = { 
                input = it
                error = false
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = error
        )
        
        Button(onClick = {
            if (input == currentProblem.second.toString()) {
                solvedCount++
                input = ""
                if (solvedCount >= targetCount) {
                    onComplete()
                } else {
                    currentProblem = generateMathProblem()
                }
            } else {
                error = true
            }
        }) {
            Text("Submit")
        }
    }
}

fun generateMathProblem(): Pair<String, Int> {
    val a = Random.nextInt(2, 12)
    val b = Random.nextInt(2, 12)
    return " $a * $b" to (a * b)
}

@Composable
fun StepChallengeScreen(
    targetSteps: Int,
    onComplete: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val sensorHelper = remember { com.example.aidigitaldetox.util.SensorManagerHelper(context) }
    var steps by remember { mutableIntStateOf(0) }
    
    // Vibration effect
    fun vibrate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val vibrator = context.getSystemService(android.os.VibratorManager::class.java).defaultVibrator
            vibrator.vibrate(android.os.VibrationEffect.createOneShot(50, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as android.os.Vibrator
            vibrator.vibrate(50)
        }
    }
    
    // Collect sensor flow
    LaunchedEffect(Unit) {
        sensorHelper.getStepCounterFlow()
            .collect { offsetSteps ->
                if (offsetSteps > steps) {
                    vibrate() // Vibrate on new step
                }
                steps = offsetSteps
                if (steps >= targetSteps) {
                    onComplete()
                }
            }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Walk to Unlock", fontSize = 24.sp, color = Color.White)
        Spacer(modifier = Modifier.height(20.dp))
        Text("$steps / $targetSteps Steps", fontSize = 48.sp, color = Color.Green)
        LinearProgressIndicator(progress = (steps / targetSteps.toFloat()).coerceIn(0f, 1f))
        
        Spacer(modifier = Modifier.height(10.dp))
        Text("(Keep phone in hand/pocket)", fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
fun SquatChallengeScreen(
    targetReps: Int,
    onComplete: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val sensorHelper = remember { com.example.aidigitaldetox.util.SensorManagerHelper(context) }
    var reps by remember { mutableIntStateOf(0) }
    
    fun vibrate() {
         if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val vibrator = context.getSystemService(android.os.VibratorManager::class.java).defaultVibrator
            vibrator.vibrate(android.os.VibrationEffect.createWaveform(longArrayOf(0, 100), -1))
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as android.os.Vibrator
            vibrator.vibrate(100)
        }
    }
    
    LaunchedEffect(Unit) {
        sensorHelper.getSquatDetectionFlow()
            .collect {
                reps++
                vibrate() // Vibrate on squat
                if (reps >= targetReps) {
                    onComplete()
                }
            }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Squat Challenge", fontSize = 24.sp, color = Color.White)
        Spacer(modifier = Modifier.height(20.dp))
        Text("$reps / $targetReps", fontSize = 64.sp, color = Color.Cyan)
        
        Spacer(modifier = Modifier.height(20.dp))
        Text("Hold phone to chest. Go down and up!", color = Color.White)
    }
}

@Composable
fun VisualMemoryChallengeScreen(
    level: Int,
    onComplete: () -> Unit
) {
    var sequence by remember { mutableStateOf(generateSequence(level)) }
    var userSequence by remember { mutableStateOf(listOf<Int>()) }
    var showingSequence by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf(false) }

    LaunchedEffect(sequence) {
        showingSequence = true
        delay(1000)
        // Ideally flash them one by one, simplified here to show all for 2s
        delay(2000)
        showingSequence = false
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(if (showingSequence) "Memorize Pattern!" else "Repeat Pattern", color = Color.White, fontSize = 24.sp)
        
        Spacer(modifier = Modifier.height(20.dp))
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.size(300.dp)
        ) {
            items(9) { index ->
                val isActive = if (showingSequence) sequence.contains(index) else userSequence.contains(index)
                val isCorrect = if (!showingSequence && userSequence.contains(index)) sequence.contains(index) else true
                
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .aspectRatio(1f)
                        .background(
                            if (isActive) (if (isCorrect) Color.Green else Color.Red) else Color.Gray
                        )
                        .clickable(!showingSequence) {
                            if (!userSequence.contains(index)) {
                                val newSeq = userSequence + index
                                userSequence = newSeq
                                if (!sequence.contains(index)) {
                                    error = true
                                    // Reset
                                    userSequence = listOf()
                                    showingSequence = true
                                } else if (newSeq.size == sequence.size) {
                                    if (newSeq.toSet() == sequence.toSet()) {
                                        onComplete()
                                    }
                                }
                            }
                        }
                )
            }
        }
    }
}

fun generateSequence(length: Int): List<Int> {
    val list = (0..8).toMutableList()
    list.shuffle()
    return list.take(length)
}
