package com.example.aidigitaldetox.domain;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class ChallengeManager_Factory implements Factory<ChallengeManager> {
  @Override
  public ChallengeManager get() {
    return newInstance();
  }

  public static ChallengeManager_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ChallengeManager newInstance() {
    return new ChallengeManager();
  }

  private static final class InstanceHolder {
    private static final ChallengeManager_Factory INSTANCE = new ChallengeManager_Factory();
  }
}
