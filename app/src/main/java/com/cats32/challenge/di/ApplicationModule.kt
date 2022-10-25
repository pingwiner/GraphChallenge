package com.cats32.challenge.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun provideContext(application: Application?): Context? {
        return application
    }

}