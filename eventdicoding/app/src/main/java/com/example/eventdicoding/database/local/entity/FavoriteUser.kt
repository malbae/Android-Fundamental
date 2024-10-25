package com.example.eventdicoding.database.local.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_user")
data class FavoriteUser(
    var login: String,
    var avatar_url: String,
    var description: String,
    var eventLocation: String,
    var beginTime: String,
    var endTime: String,
    var quota: Int,               // New field for quota
    var registrants: Int,         // New field for registrants
    @PrimaryKey
    val id: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),             // Read quota
        parcel.readInt(),             // Read registrants
        parcel.readInt()              // Read id
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(login)
        parcel.writeString(avatar_url)
        parcel.writeString(description)
        parcel.writeString(eventLocation)
        parcel.writeString(beginTime)
        parcel.writeString(endTime)
        parcel.writeInt(quota)         // Write quota
        parcel.writeInt(registrants)   // Write registrants
        parcel.writeInt(id)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<FavoriteUser> {
        override fun createFromParcel(parcel: Parcel): FavoriteUser {
            return FavoriteUser(parcel)
        }

        override fun newArray(size: Int): Array<FavoriteUser?> {
            return arrayOfNulls(size)
        }
    }
}
