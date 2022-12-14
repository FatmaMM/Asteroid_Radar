package com.example.asteroidrader.api

import com.example.asteroidrader.model.PictureOfDay
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("api_key") api_key: String,
        @Query("start_date") start_date: String,
        @Query("end_date") end_date:String
    ): String

    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(
        @Query("api_key") api_key: String
    ): PictureOfDay
}