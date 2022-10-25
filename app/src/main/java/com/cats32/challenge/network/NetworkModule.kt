package com.cats32.challenge.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideNetwork(network: Network): Retrofit {
        return network.build()
    }

    @Provides
    @Singleton
    fun provideProjectResources(retrofit: Retrofit): ProjectResources {
        return retrofit.create(ProjectResources::class.java)
    }

}