package com.example.aidigitaldetox.util;

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
public final class AppUsageHelper_Factory implements Factory<AppUsageHelper> {
  private final Provider<Context> contextProvider;

  public AppUsageHelper_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AppUsageHelper get() {
    return newInstance(contextProvider.get());
  }

  public static AppUsageHelper_Factory create(Provider<Context> contextProvider) {
    return new AppUsageHelper_Factory(contextProvider);
  }

  public static AppUsageHelper newInstance(Context context) {
    return new AppUsageHelper(context);
  }
}
