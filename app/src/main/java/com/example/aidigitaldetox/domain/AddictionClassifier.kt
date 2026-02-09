package com.example.aidigitaldetox.domain

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.inject.Inject

class AddictionClassifier @Inject constructor(
    private val context: Context
) {
    private var interpreter: Interpreter? = null

    init {
        try {
            val modelBuffer = loadModelFile("addiction_model.tflite")
            interpreter = Interpreter(modelBuffer)
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback or error handling
        }
    }

    private fun loadModelFile(modelName: String): ByteBuffer {
        val assetFileDescriptor = context.assets.openFd(modelName)
        val fileInputStream = java.io.FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun predictAddictionScore(
        totalUsageTime: Long,
        openCount: Int,
        emergencyUnlocks: Int
    ): Float {
        // Prepare input tensor: [1, 3] -> [Time, Opens, Unlocks]
        // Normalized dummy values for safe input range
        val inputs = floatArrayOf(
            (totalUsageTime / (1000 * 60 * 60 * 24f)).coerceIn(0f, 1f),
            (openCount / 100f).coerceIn(0f, 1f),
            (emergencyUnlocks / 10f).coerceIn(0f, 1f)
        )
        
        // Output tensor: [1, 1] -> [Addiction Score 0..1]
        val output = Array(1) { FloatArray(1) }

        return try {
            if (interpreter != null) {
                interpreter?.run(arrayOf(inputs), output)
                // specific model output scaling
                (output[0][0] * 100).coerceIn(0f, 100f)
            } else {
                // Fallback heuristic if ML fails
                calculateHeuristicScore(inputs[0], inputs[1], inputs[2])
            }
        } catch (e: Exception) {
            e.printStackTrace()
            calculateHeuristicScore(inputs[0], inputs[1], inputs[2])
        }
    }
    
    private fun calculateHeuristicScore(time: Float, opens: Float, unlocks: Float): Float {
        val score = (time * 0.5f) + (opens * 0.3f) + (unlocks * 0.2f)
        return (score * 100).coerceIn(0f, 100f)
    }

    fun recommendDifficulty(addictionScore: Float): Int {
        return when {
            addictionScore > 80 -> 10 // Hard
            addictionScore > 50 -> 6  // Medium
            else -> 3                 // Easy
        }
    }
}
