package com.example.aidigitaldetox.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Progress
        Text(
            "Math Challenge", 
            style = MaterialTheme.typography.titleMedium,
            color = Color.White.copy(alpha = 0.8f)
        )
        Text(
            "$solvedCount / $targetCount", 
            style = MaterialTheme.typography.displayMedium,
            color = Color.White
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Problem
        Text(
            "${currentProblem.first} = ?", 
            style = MaterialTheme.typography.displayLarge, 
            color = Color.White
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Input Display
        Text(
            text = input.ifEmpty { " " },
            style = MaterialTheme.typography.displayMedium,
            color = if (error) Color.Red else Color.Cyan,
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.1f), androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                .padding(horizontal = 32.dp, vertical = 8.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Custom Numpad
        Numpad(
            onDigitClick = { digit ->
                if (input.length < 6) {
                    input += digit
                    error = false
                }
            },
            onDeleteClick = {
                if (input.isNotEmpty()) {
                    input = input.dropLast(1)
                    error = false
                }
            },
            onEnterClick = {
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
                    input = ""
                }
            }
        )
    }
}

@Composable
fun Numpad(
    onDigitClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onEnterClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val rows = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf("", "0", "DEL")
        )
        
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                row.forEach { key ->
                    if (key.isEmpty()) {
                         Spacer(modifier = Modifier.weight(1f))
                    } else if (key == "DEL") {
                         NumpadButton(text = "DEL", color = Color.Red.copy(alpha = 0.5f), onClick = onDeleteClick, modifier = Modifier.weight(1f))
                    } else {
                         NumpadButton(text = key, onClick = { onDigitClick(key) }, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Button(
            onClick = onEnterClick,
            modifier = Modifier.fillMaxWidth().height(64.dp), 
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)) // Vibrant Green
        ) {
            Text("SUBMIT", fontSize = 24.sp, color = Color.White, style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
fun NumpadButton(
    text: String,
    color: Color = Color.White.copy(alpha = 0.15f),
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(72.dp)
            .background(color, androidx.compose.foundation.shape.CircleShape)
            .border(1.dp, Color.White.copy(alpha = 0.3f), androidx.compose.foundation.shape.CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = 28.sp, color = Color.White, style = MaterialTheme.typography.headlineMedium)
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
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                val vibrator = context.getSystemService(android.os.VibratorManager::class.java)?.defaultVibrator
                vibrator?.vibrate(android.os.VibrationEffect.createOneShot(50, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? android.os.Vibrator
                vibrator?.vibrate(50)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    // Collect sensor flow
    LaunchedEffect(Unit) {
        try {
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
        } catch (e: Exception) {
             e.printStackTrace()
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("WALK TO UNLOCK", fontSize = 24.sp, color = Color.White, style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(40.dp))
        
        Box(contentAlignment = Alignment.Center) {
             CircularProgressIndicator(
                progress = (steps / targetSteps.toFloat()).coerceIn(0f, 1f),
                modifier = Modifier.size(200.dp),
                color = Color.Green,
                strokeWidth = 12.dp
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("$steps", fontSize = 64.sp, color = Color.White, style = MaterialTheme.typography.displayLarge)
                Text("/ $targetSteps", fontSize = 24.sp, color = Color.Gray)
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))
        Text("Keep moving!", fontSize = 18.sp, color = Color.White.copy(alpha = 0.8f))
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
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                val vibrator = context.getSystemService(android.os.VibratorManager::class.java)?.defaultVibrator
                vibrator?.vibrate(android.os.VibrationEffect.createWaveform(longArrayOf(0, 100), -1))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? android.os.Vibrator
                vibrator?.vibrate(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    LaunchedEffect(Unit) {
        try {
            sensorHelper.getSquatDetectionFlow()
                .collect {
                    reps++
                    vibrate() // Vibrate on squat
                    if (reps >= targetReps) {
                        onComplete()
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
             // Fallback: If sensors fail, maybe auto-complete or show error?
             // For now, just logging to prevent crash
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("SQUATS", fontSize = 24.sp, color = Color.White, style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(40.dp))
        
        Box(contentAlignment = Alignment.Center) {
             CircularProgressIndicator(
                progress = (reps / targetReps.toFloat()).coerceIn(0f, 1f),
                modifier = Modifier.size(200.dp),
                color = Color.Cyan,
                strokeWidth = 12.dp
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("$reps", fontSize = 64.sp, color = Color.White, style = MaterialTheme.typography.displayLarge)
                Text("/ $targetReps", fontSize = 24.sp, color = Color.Gray)
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))
        Text("Down and Up!", fontSize = 18.sp, color = Color.White.copy(alpha = 0.8f))
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
    var retryTrigger by remember { mutableIntStateOf(0) } // Forces recomposition of LaunchedEffect

    // Effect to show the sequence. Runs when sequence changes OR retryTrigger increments.
    LaunchedEffect(sequence, retryTrigger) {
        userSequence = listOf() // Clear input
        showingSequence = true
        error = false
        
        delay(1000)
        // Flash them 
        delay(2000)
        
        showingSequence = false
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val title = when {
            showingSequence -> "Memorize Pattern!"
            error -> "Wrong! Watching again..."
            else -> "Repeat Pattern"
        }
        Text(title, color = if (error) Color.Red else Color.White, fontSize = 24.sp)
        
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
                                
                                // Check if the latest tap is wrong immediately? 
                                // Or wait? The original code checked immediately if it's NOT in sequence.
                                if (!sequence.contains(index)) {
                                    // Wrong tap!
                                    error = true
                                    // Trigger replay after a short delay so user sees the red
                                    retryTrigger++ 
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
