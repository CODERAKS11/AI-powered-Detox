package com.example.aidigitaldetox.ml;

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
public final class DetoxModelRunner_Factory implements Factory<DetoxModelRunner> {
  private final Provider<Context> contextProvider;

  public DetoxModelRunner_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public DetoxModelRunner get() {
    return newInstance(contextProvider.get());
  }

  public static DetoxModelRunner_Factory create(Provider<Context> contextProvider) {
    return new DetoxModelRunner_Factory(contextProvider);
  }

  public static DetoxModelRunner newInstance(Context context) {
    return new DetoxModelRunner(context);
  }
}
