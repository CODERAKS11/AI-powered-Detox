package com.example.aidigitaldetox.data;

import com.example.aidigitaldetox.ml.DetoxModelRunner;
import com.example.aidigitaldetox.ml.FeatureExtractor;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class AddictionRiskRepository_Factory implements Factory<AddictionRiskRepository> {
  private final Provider<FeatureExtractor> featureExtractorProvider;

  private final Provider<DetoxModelRunner> modelRunnerProvider;

  public AddictionRiskRepository_Factory(Provider<FeatureExtractor> featureExtractorProvider,
      Provider<DetoxModelRunner> modelRunnerProvider) {
    this.featureExtractorProvider = featureExtractorProvider;
    this.modelRunnerProvider = modelRunnerProvider;
  }

  @Override
  public AddictionRiskRepository get() {
    return newInstance(featureExtractorProvider.get(), modelRunnerProvider.get());
  }

  public static AddictionRiskRepository_Factory create(
      Provider<FeatureExtractor> featureExtractorProvider,
      Provider<DetoxModelRunner> modelRunnerProvider) {
    return new AddictionRiskRepository_Factory(featureExtractorProvider, modelRunnerProvider);
  }

  public static AddictionRiskRepository newInstance(FeatureExtractor featureExtractor,
      DetoxModelRunner modelRunner) {
    return new AddictionRiskRepository(featureExtractor, modelRunner);
  }
}
