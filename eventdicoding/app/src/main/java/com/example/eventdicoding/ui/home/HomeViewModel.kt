package com.example.eventdicoding.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventdicoding.data.response.EventResponse
import com.example.eventdicoding.data.response.ListEventsItem
import com.example.eventdicoding.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>() // Inisialisasi MutableLiveData untuk isLoading
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>() // Inisialisasi MutableLiveData untuk errorMessage
    val errorMessage: LiveData<String> = _errorMessage

    init {
        loadEvents()
    }

    private fun loadEvents() {
        _isLoading.value = true // Mengatur isLoading menjadi true saat memulai proses load
        val client = ApiConfig.getApiService().getAllEvents()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false // Mengatur isLoading menjadi false saat proses selesai
                if (response.isSuccessful) {
                    response.body()?.let { eventResponse ->
                        // Pastikan semua data dalam response tidak null
                        _upcomingEvents.value = eventResponse.listEvents.filter { it.isActive }
                        _finishedEvents.value = eventResponse.listEvents.filter { !it.isActive }
                    }
                } else {
                    _errorMessage.value = "Failed to load data: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false // Mengatur isLoading menjadi false saat terjadi kegagalan
                _errorMessage.value = "Failed to load data: ${t.message}"
            }
        })
    }
}
