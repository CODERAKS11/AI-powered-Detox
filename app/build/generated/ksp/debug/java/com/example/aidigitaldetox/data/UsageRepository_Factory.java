package com.example.aidigitaldetox.data;

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
public final class UsageRepository_Factory implements Factory<UsageRepository> {
  private final Provider<UsageDao> usageDaoProvider;

  public UsageRepository_Factory(Provider<UsageDao> usageDaoProvider) {
    this.usageDaoProvider = usageDaoProvider;
  }

  @Override
  public UsageRepository get() {
    return newInstance(usageDaoProvider.get());
  }

  public static UsageRepository_Factory create(Provider<UsageDao> usageDaoProvider) {
    return new UsageRepository_Factory(usageDaoProvider);
  }

  public static UsageRepository newInstance(UsageDao usageDao) {
    return new UsageRepository(usageDao);
  }
}
