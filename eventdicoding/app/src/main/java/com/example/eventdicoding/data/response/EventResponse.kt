package com.example.eventdicoding.data.response

import com.google.gson.annotations.SerializedName

data class EventResponse(
	@field:SerializedName("listEvents")
	val listEvents: List<ListEventsItem>, // Ini harus benar
	@field:SerializedName("error")
	val error: Boolean,
	@field:SerializedName("message")
	val message: String
)