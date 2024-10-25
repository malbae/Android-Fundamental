package com.example.eventdicoding.data.retrofit

import com.example.eventdicoding.data.response.EventResponse
import com.example.eventdicoding.database.local.entity.FavoriteUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // Get upcoming events (active = 1)
    @GET("events")
    fun getUpcomingEvent(
        @Query("active") active: Int = 1,
        @Query("limit") limit: Int? = null // Optionally limit results
    ): Call<EventResponse>

    // Get finished events (active = 0)
    @GET("events")
    fun getFinishedEvent(
        @Query("active") active: Int = 0,
        @Query("limit") limit: Int? = null // Optionally limit results
    ): Call<EventResponse>

    // Get all events (without active filter)
    @GET("events")
    fun getAllEvents(
        @Query("limit") limit: Int? = null // Optionally limit results
    ): Call<EventResponse>

    // Get finished events from a different endpoint (if required)
    @GET("finished-events")
    fun getFinishedEvents(): Call<EventResponse>

    // Get favorite users
    @GET("favorites")
    fun getFavoriteUsers(): Call<List<FavoriteUser>>

}
