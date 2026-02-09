package com.example.aidigitaldetox.ml;

import com.example.aidigitaldetox.data.UsageDao;
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
public final class FeatureExtractor_Factory implements Factory<FeatureExtractor> {
  private final Provider<UsageDao> usageDaoProvider;

  public FeatureExtractor_Factory(Provider<UsageDao> usageDaoProvider) {
    this.usageDaoProvider = usageDaoProvider;
  }

  @Override
  public FeatureExtractor get() {
    return newInstance(usageDaoProvider.get());
  }

  public static FeatureExtractor_Factory create(Provider<UsageDao> usageDaoProvider) {
    return new FeatureExtractor_Factory(usageDaoProvider);
  }

  public static FeatureExtractor newInstance(UsageDao usageDao) {
    return new FeatureExtractor(usageDao);
  }
}
