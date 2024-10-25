package com.example.eventdicoding.data.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

data class ListEventsItem(
    @field:SerializedName("summary") val summary: String = "",
    @field:SerializedName("mediaCover") val mediaCover: String = "",
    @field:SerializedName("registrants") val registrants: Int = 0,
    @field:SerializedName("imageLogo") val imageLogo: String = "",
    @field:SerializedName("link") val link: String = "",
    @field:SerializedName("description") val description: String = "",
    @field:SerializedName("ownerName") val ownerName: String = "",
    @field:SerializedName("cityName") val cityName: String = "",
    @field:SerializedName("quota") val quota: Int = 0,
    @field:SerializedName("name") val name: String = "",
    @field:SerializedName("id") val id: Int = 0,
    @field:SerializedName("beginTime") val beginTime: String = "",
    @field:SerializedName("endTime") val endTime: String = "",
    @field:SerializedName("category") val category: String = "",
    var isFavorite: Boolean = false
) : Parcelable {

    // Properti untuk menentukan apakah event masih aktif (endTime lebih besar dari waktu sekarang)
    val isActive: Boolean
        get() {
            return try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val endDate: Date = dateFormat.parse(endTime) ?: Date(0)
                endDate.after(Date())
            } catch (e: ParseException) {
                false
            }
        }

    // Konstruktor parcelable
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(summary)
        parcel.writeString(mediaCover)
        parcel.writeInt(registrants)
        parcel.writeString(imageLogo)
        parcel.writeString(link)
        parcel.writeString(description)
        parcel.writeString(ownerName)
        parcel.writeString(cityName)
        parcel.writeInt(quota)
        parcel.writeString(name)
        parcel.writeInt(id)
        parcel.writeString(beginTime)
        parcel.writeString(endTime)
        parcel.writeString(category)
        parcel.writeByte(if (isFavorite) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ListEventsItem> {
        override fun createFromParcel(parcel: Parcel): ListEventsItem {
            return ListEventsItem(parcel)
        }

        override fun newArray(size: Int): Array<ListEventsItem?> {
            return arrayOfNulls(size)
        }
    }
}
