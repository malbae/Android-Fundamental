package com.example.eventdicoding.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventdicoding.data.response.EventResponse // Pastikan ini adalah respons dari API
import com.example.eventdicoding.data.response.ListEventsItem
import com.example.eventdicoding.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvents

    private val _filteredEvents = MutableLiveData<List<ListEventsItem>>()
    val filteredEvents: LiveData<List<ListEventsItem>> = _filteredEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        loadFinishedEvents()
    }

    // Fungsi untuk memuat data event yang sudah selesai dari API
    private fun loadFinishedEvents() {
        _isLoading.value = true // Tampilkan indikator loading

        // Gunakan Call<EventResponse> sebagai tipe callback, bukan Call<List<ListEventsItem>>
        val client = ApiConfig.getApiService().getAllEvents() // Pastikan getAllEvents mengembalikan EventResponse
        client.enqueue(object : Callback<EventResponse> { // Ubah Call<List<ListEventsItem>> menjadi Call<EventResponse>
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false // Sembunyikan indikator loading
                if (response.isSuccessful) {
                    response.body()?.let { eventResponse ->
                        // Ambil listEvents dari EventResponse
                        val finishedEventsList = eventResponse.listEvents.filter { event ->
                            // Tambahkan logika untuk menentukan event selesai
                            !event.isActive // Ganti dengan logika filter yang tepat
                        }
                        _finishedEvents.value = finishedEventsList
                        _filteredEvents.value = finishedEventsList // Set default filtered events
                    }
                } else {
                    _errorMessage.value = "Failed to load data: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false // Sembunyikan indikator loading
                _errorMessage.value = "Failed to load data: ${t.message}"
            }
        })
    }

    // Fungsi untuk memfilter event berdasarkan query
    fun filterEvents(query: String) {
        val currentList = _finishedEvents.value.orEmpty() // Gunakan `orEmpty()` jika `_finishedEvents.value` null
        val filteredList = currentList.filter {
            it.name.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true)
        }
        _filteredEvents.value = filteredList // `filteredList` sekarang tidak mungkin `null`
    }
}
