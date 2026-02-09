package com.example.aidigitaldetox.di

import android.content.Context
import com.example.aidigitaldetox.domain.AddictionClassifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MlModule {

    @Provides
    @Singleton
    fun provideAddictionClassifier(@ApplicationContext context: Context): AddictionClassifier {
        return AddictionClassifier(context)
    }
}
