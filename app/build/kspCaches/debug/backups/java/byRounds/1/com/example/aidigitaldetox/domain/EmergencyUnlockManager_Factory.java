package com.example.aidigitaldetox.domain;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class EmergencyUnlockManager_Factory implements Factory<EmergencyUnlockManager> {
  private final Provider<Context> contextProvider;

  public EmergencyUnlockManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public EmergencyUnlockManager get() {
    return newInstance(contextProvider.get());
  }

  public static EmergencyUnlockManager_Factory create(Provider<Context> contextProvider) {
    return new EmergencyUnlockManager_Factory(contextProvider);
  }

  public static EmergencyUnlockManager newInstance(Context context) {
    return new EmergencyUnlockManager(context);
  }
}
