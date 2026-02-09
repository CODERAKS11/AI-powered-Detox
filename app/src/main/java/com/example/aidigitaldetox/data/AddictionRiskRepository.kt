package com.example.aidigitaldetox.data

import com.example.aidigitaldetox.ml.DetoxModelRunner
import com.example.aidigitaldetox.ml.FeatureExtractor
import com.example.aidigitaldetox.ml.PredictionResult
import javax.inject.Inject

class AddictionRiskRepository @Inject constructor(
    private val featureExtractor: FeatureExtractor,
    private val modelRunner: DetoxModelRunner
) {
    suspend fun getRiskAssessment(): PredictionResult {
        val features = featureExtractor.extractFeatures()
        return modelRunner.predict(features)
    }
}
