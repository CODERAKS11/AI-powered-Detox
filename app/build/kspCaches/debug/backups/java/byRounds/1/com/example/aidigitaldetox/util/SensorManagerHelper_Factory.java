package com.example.aidigitaldetox.util;

import android.content.Context;
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
public final class SensorManagerHelper_Factory implements Factory<SensorManagerHelper> {
  private final Provider<Context> contextProvider;

  public SensorManagerHelper_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SensorManagerHelper get() {
    return newInstance(contextProvider.get());
  }

  public static SensorManagerHelper_Factory create(Provider<Context> contextProvider) {
    return new SensorManagerHelper_Factory(contextProvider);
  }

  public static SensorManagerHelper newInstance(Context context) {
    return new SensorManagerHelper(context);
  }
}
