package com.example.eventdicoding.database.local.remote.response

import com.google.gson.annotations.SerializedName

data class ItemsItem(

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("id")
    val id: Int,
)