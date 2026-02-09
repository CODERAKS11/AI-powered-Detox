package com.example.aidigitaldetox.ui;

import android.app.Application;
import com.example.aidigitaldetox.data.AppLockRepository;
import com.example.aidigitaldetox.util.AppUsageHelper;
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
public final class RestrictedAppsViewModel_Factory implements Factory<RestrictedAppsViewModel> {
  private final Provider<Application> applicationProvider;

  private final Provider<AppLockRepository> appLockRepositoryProvider;

  private final Provider<AppUsageHelper> usageHelperProvider;

  public RestrictedAppsViewModel_Factory(Provider<Application> applicationProvider,
      Provider<AppLockRepository> appLockRepositoryProvider,
      Provider<AppUsageHelper> usageHelperProvider) {
    this.applicationProvider = applicationProvider;
    this.appLockRepositoryProvider = appLockRepositoryProvider;
    this.usageHelperProvider = usageHelperProvider;
  }

  @Override
  public RestrictedAppsViewModel get() {
    return newInstance(applicationProvider.get(), appLockRepositoryProvider.get(), usageHelperProvider.get());
  }

  public static RestrictedAppsViewModel_Factory create(Provider<Application> applicationProvider,
      Provider<AppLockRepository> appLockRepositoryProvider,
      Provider<AppUsageHelper> usageHelperProvider) {
    return new RestrictedAppsViewModel_Factory(applicationProvider, appLockRepositoryProvider, usageHelperProvider);
  }

  public static RestrictedAppsViewModel newInstance(Application application,
      AppLockRepository appLockRepository, AppUsageHelper usageHelper) {
    return new RestrictedAppsViewModel(application, appLockRepository, usageHelper);
  }
}
