package com.example.android.asteroidradar.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "picture_table")
data class PictureOfDay(
    var copyright: String = "",
    var date: String = "",
    var explanation: String = "",
    var hdurl: String = "",
    var media_type: String = "",
    var service_version: String = "",
    var title: String = "",
    @PrimaryKey(autoGenerate = false)
    var url: String = ""
) : Parcelable {

    fun image(): String {
        return "Image Of The Day: " + title
    }
}
