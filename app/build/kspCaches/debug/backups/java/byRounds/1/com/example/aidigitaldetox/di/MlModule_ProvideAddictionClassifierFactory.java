package com.example.aidigitaldetox.di;

import android.content.Context;
import com.example.aidigitaldetox.domain.AddictionClassifier;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class MlModule_ProvideAddictionClassifierFactory implements Factory<AddictionClassifier> {
  private final Provider<Context> contextProvider;

  public MlModule_ProvideAddictionClassifierFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AddictionClassifier get() {
    return provideAddictionClassifier(contextProvider.get());
  }

  public static MlModule_ProvideAddictionClassifierFactory create(
      Provider<Context> contextProvider) {
    return new MlModule_ProvideAddictionClassifierFactory(contextProvider);
  }

  public static AddictionClassifier provideAddictionClassifier(Context context) {
    return Preconditions.checkNotNullFromProvides(MlModule.INSTANCE.provideAddictionClassifier(context));
  }
}
