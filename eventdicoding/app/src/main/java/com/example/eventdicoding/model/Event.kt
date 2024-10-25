package com.example.eventdicoding.model

data class Event(
    val id: String,
    val name: String,
    val description: String,
    val date: String,
    val imageUrl: String,
    var isFavorite: Boolean
)
