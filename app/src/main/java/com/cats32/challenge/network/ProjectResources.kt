package com.cats32.challenge.network

import com.cats32.challenge.network.entities.PointsResponse
import retrofit2.http.GET
import retrofit2.http.Query



interface ProjectResources {

    @GET("test/points")
    suspend fun getPoints(
        @Query("count") count: Int
    ): PointsResponse
}