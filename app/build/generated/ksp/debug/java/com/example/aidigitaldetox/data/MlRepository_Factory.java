package com.example.aidigitaldetox.data;

import com.example.aidigitaldetox.domain.AddictionClassifier;
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
public final class MlRepository_Factory implements Factory<MlRepository> {
  private final Provider<UsageDao> usageDaoProvider;

  private final Provider<AddictionClassifier> classifierProvider;

  public MlRepository_Factory(Provider<UsageDao> usageDaoProvider,
      Provider<AddictionClassifier> classifierProvider) {
    this.usageDaoProvider = usageDaoProvider;
    this.classifierProvider = classifierProvider;
  }

  @Override
  public MlRepository get() {
    return newInstance(usageDaoProvider.get(), classifierProvider.get());
  }

  public static MlRepository_Factory create(Provider<UsageDao> usageDaoProvider,
      Provider<AddictionClassifier> classifierProvider) {
    return new MlRepository_Factory(usageDaoProvider, classifierProvider);
  }

  public static MlRepository newInstance(UsageDao usageDao, AddictionClassifier classifier) {
    return new MlRepository(usageDao, classifier);
  }
}
