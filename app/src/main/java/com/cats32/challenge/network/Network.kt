package com.cats32.challenge.network


import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Network @Inject constructor() {
    companion object {
        private const val HOST = "https://hr-challenge.interactivestandard.com/api/"
    }

    fun build(): Retrofit {
        val clientBuilder = OkHttpClient().newBuilder()

        val networkBuilder = Retrofit.Builder()
        networkBuilder
            .baseUrl(HOST)
            .client(clientBuilder.build())
            .addConverterFactory(jsonConverter)
        return networkBuilder.build()
    }

    private val jsonConverter: GsonConverterFactory
        private get() {
            val gson = GsonBuilder().create()
            return GsonConverterFactory.create(gson)
        }

}