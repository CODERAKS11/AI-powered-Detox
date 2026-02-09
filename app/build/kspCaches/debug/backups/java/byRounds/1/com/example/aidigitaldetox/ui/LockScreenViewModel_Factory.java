package com.example.aidigitaldetox.ui;

import com.example.aidigitaldetox.data.AddictionRiskRepository;
import com.example.aidigitaldetox.data.AppLockRepository;
import com.example.aidigitaldetox.data.UsageRepository;
import com.example.aidigitaldetox.domain.ChallengeManager;
import com.example.aidigitaldetox.domain.EmergencyUnlockManager;
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
public final class LockScreenViewModel_Factory implements Factory<LockScreenViewModel> {
  private final Provider<AddictionRiskRepository> repositoryProvider;

  private final Provider<UsageRepository> usageRepositoryProvider;

  private final Provider<ChallengeManager> challengeManagerProvider;

  private final Provider<AppLockRepository> appLockRepositoryProvider;

  private final Provider<EmergencyUnlockManager> emergencyUnlockManagerProvider;

  public LockScreenViewModel_Factory(Provider<AddictionRiskRepository> repositoryProvider,
      Provider<UsageRepository> usageRepositoryProvider,
      Provider<ChallengeManager> challengeManagerProvider,
      Provider<AppLockRepository> appLockRepositoryProvider,
      Provider<EmergencyUnlockManager> emergencyUnlockManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.usageRepositoryProvider = usageRepositoryProvider;
    this.challengeManagerProvider = challengeManagerProvider;
    this.appLockRepositoryProvider = appLockRepositoryProvider;
    this.emergencyUnlockManagerProvider = emergencyUnlockManagerProvider;
  }

  @Override
  public LockScreenViewModel get() {
    return newInstance(repositoryProvider.get(), usageRepositoryProvider.get(), challengeManagerProvider.get(), appLockRepositoryProvider.get(), emergencyUnlockManagerProvider.get());
  }

  public static LockScreenViewModel_Factory create(
      Provider<AddictionRiskRepository> repositoryProvider,
      Provider<UsageRepository> usageRepositoryProvider,
      Provider<ChallengeManager> challengeManagerProvider,
      Provider<AppLockRepository> appLockRepositoryProvider,
      Provider<EmergencyUnlockManager> emergencyUnlockManagerProvider) {
    return new LockScreenViewModel_Factory(repositoryProvider, usageRepositoryProvider, challengeManagerProvider, appLockRepositoryProvider, emergencyUnlockManagerProvider);
  }

  public static LockScreenViewModel newInstance(AddictionRiskRepository repository,
      UsageRepository usageRepository, ChallengeManager challengeManager,
      AppLockRepository appLockRepository, EmergencyUnlockManager emergencyUnlockManager) {
    return new LockScreenViewModel(repository, usageRepository, challengeManager, appLockRepository, emergencyUnlockManager);
  }
}
