package com.example.aidigitaldetox.domain;

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
public final class AddictionClassifier_Factory implements Factory<AddictionClassifier> {
  private final Provider<Context> contextProvider;

  public AddictionClassifier_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AddictionClassifier get() {
    return newInstance(contextProvider.get());
  }

  public static AddictionClassifier_Factory create(Provider<Context> contextProvider) {
    return new AddictionClassifier_Factory(contextProvider);
  }

  public static AddictionClassifier newInstance(Context context) {
    return new AddictionClassifier(context);
  }
}
