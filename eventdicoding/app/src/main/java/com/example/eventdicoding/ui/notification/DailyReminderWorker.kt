package com.example.eventdicoding.ui.notification

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.eventdicoding.data.retrofit.ApiService
import com.example.eventdicoding.ui.notification.NotificationHelper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DailyReminderWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        // Initialize Retrofit and API service
        val retrofit = Retrofit.Builder()
            .baseUrl("https://event-api.dicoding.dev")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val eventApi = retrofit.create(ApiService::class.java)

        return try {
            // Fetch the upcoming event with a limit of 1
            val response = eventApi.getUpcomingEvent(limit = 1).execute()

            if (response.isSuccessful) {
                // Accessing the list of events via `listEvents`
                val event = response.body()?.listEvents?.firstOrNull()

                event?.let {
                    // If event is available, trigger a notification
                    NotificationHelper(applicationContext).showNotification(
                        title = it.name, // Ensure `name` exists in ListEventsItem
                        message = "Upcoming event: ${it.name} on ${it.beginTime}" // Ensure `beginTime` exists in ListEventsItem
                    )
                }
                Result.success() // Return success
            } else {
                // Return failure if response is not successful
                Result.failure()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure() // Return failure in case of exception
        }
    }
}
