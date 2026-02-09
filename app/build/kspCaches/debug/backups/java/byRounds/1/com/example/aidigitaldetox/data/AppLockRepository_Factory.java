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
public final class AppLockRepository_Factory implements Factory<AppLockRepository> {
  private final Provider<RestrictedAppDao> restrictedAppDaoProvider;

  public AppLockRepository_Factory(Provider<RestrictedAppDao> restrictedAppDaoProvider) {
    this.restrictedAppDaoProvider = restrictedAppDaoProvider;
  }

  @Override
  public AppLockRepository get() {
    return newInstance(restrictedAppDaoProvider.get());
  }

  public static AppLockRepository_Factory create(
      Provider<RestrictedAppDao> restrictedAppDaoProvider) {
    return new AppLockRepository_Factory(restrictedAppDaoProvider);
  }

  public static AppLockRepository newInstance(RestrictedAppDao restrictedAppDao) {
    return new AppLockRepository(restrictedAppDao);
  }
}
