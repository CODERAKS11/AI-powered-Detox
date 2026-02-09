package com.example.aidigitaldetox.service;

import com.example.aidigitaldetox.data.AppLockRepository;
import com.example.aidigitaldetox.util.AppUsageHelper;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class AppMonitorService_MembersInjector implements MembersInjector<AppMonitorService> {
  private final Provider<AppLockRepository> appLockRepositoryProvider;

  private final Provider<AppUsageHelper> appUsageHelperProvider;

  public AppMonitorService_MembersInjector(Provider<AppLockRepository> appLockRepositoryProvider,
      Provider<AppUsageHelper> appUsageHelperProvider) {
    this.appLockRepositoryProvider = appLockRepositoryProvider;
    this.appUsageHelperProvider = appUsageHelperProvider;
  }

  public static MembersInjector<AppMonitorService> create(
      Provider<AppLockRepository> appLockRepositoryProvider,
      Provider<AppUsageHelper> appUsageHelperProvider) {
    return new AppMonitorService_MembersInjector(appLockRepositoryProvider, appUsageHelperProvider);
  }

  @Override
  public void injectMembers(AppMonitorService instance) {
    injectAppLockRepository(instance, appLockRepositoryProvider.get());
    injectAppUsageHelper(instance, appUsageHelperProvider.get());
  }

  @InjectedFieldSignature("com.example.aidigitaldetox.service.AppMonitorService.appLockRepository")
  public static void injectAppLockRepository(AppMonitorService instance,
      AppLockRepository appLockRepository) {
    instance.appLockRepository = appLockRepository;
  }

  @InjectedFieldSignature("com.example.aidigitaldetox.service.AppMonitorService.appUsageHelper")
  public static void injectAppUsageHelper(AppMonitorService instance,
      AppUsageHelper appUsageHelper) {
    instance.appUsageHelper = appUsageHelper;
  }
}
