package com.example.aidigitaldetox.di;

import com.example.aidigitaldetox.data.AppDatabase;
import com.example.aidigitaldetox.data.RestrictedAppDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideRestrictedAppDaoFactory implements Factory<RestrictedAppDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideRestrictedAppDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public RestrictedAppDao get() {
    return provideRestrictedAppDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideRestrictedAppDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideRestrictedAppDaoFactory(databaseProvider);
  }

  public static RestrictedAppDao provideRestrictedAppDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideRestrictedAppDao(database));
  }
}
