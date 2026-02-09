package com.example.aidigitaldetox.util

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import kotlin.math.abs

class SensorManagerHelper @Inject constructor(
    private val context: Context
) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    fun getStepCounterFlow(): Flow<Int> = callbackFlow {
        // User requested manual tuning ("reduce sensitivity"), so we rely on Accelerometer
        // which gives us full control over thresholds, unlike the hardware Step Detector.
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometer == null) {
            close(Exception("Accelerometer not available"))
            return@callbackFlow
        }

        var steps = 0
        val listener = object : SensorEventListener {
            // Magnitude Threshold for a step (tuned for "easier" detection)
            // Standard gravity is ~9.8.
            // Walking peak is usually > 12.0.
            // We set it to 11.5 to be sensitive (easy to trigger).
            private val threshold = 11.5f 
            private var lastStepTime = 0L
            private val minTimeBetweenSteps = 300L // 300ms debounce (max 3 steps/sec)

            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]
                    
                    // 1. Calculate Magnitude (Total G-Force)
                    val magnitude = kotlin.math.sqrt(x * x + y * y + z * z)
                    
                    // 2. Check for step peak
                    if (magnitude > threshold) {
                        val now = System.currentTimeMillis()
                        if (now - lastStepTime > minTimeBetweenSteps) {
                            steps++
                            lastStepTime = now
                            trySend(steps)
                        }
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }

    fun getSquatDetectionFlow(): Flow<Boolean> = callbackFlow {
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometer == null) {
            close(Exception("Accelerometer not available"))
            return@callbackFlow
        }

        val listener = object : SensorEventListener {
            private var isDown = false
            
            // Tuned Thresholds (Magnitude based - works in any orientation)
            // Gravity ~ 9.8
            // Down: Feeling lighter (< 9.8). Set to 9.2 (Very easy to trigger)
            private val thresholdDown = 9.2f 
            // Up: Feeling heavier (> 9.8). Set to 10.5 (Very easy to trigger)
            private val thresholdUp = 10.5f 
            
            private var lastEventTime = 0L
            private val minTimeBetweenSquats = 1000L // 1 sec debounce

            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]
                    
                    // 1. Calculate Magnitude
                    val magnitude = kotlin.math.sqrt(x * x + y * y + z * z)
                    
                    // 2. State Machine
                    if (magnitude < thresholdDown && !isDown) {
                        isDown = true
                    } else if (magnitude > thresholdUp && isDown) {
                        val now = System.currentTimeMillis()
                        if (now - lastEventTime > minTimeBetweenSquats) {
                            isDown = false
                            lastEventTime = now
                            trySend(true) // Squat completed
                        }
                    }
                    
                    // Simple Reset if held steady for too long
                    if (isDown && magnitude > 9.5 && magnitude < 10.0) {
                         // Optional: could add timeout logic here
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }
}
