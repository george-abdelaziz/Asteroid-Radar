package com.example.android.asteroidradar.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "asteroid_table")
data class Asteroid(
    @PrimaryKey(autoGenerate = false)
    var id: Long = 0L,
    var codename: String = "",
    var closeApproachDate: String = "",
    var absoluteMagnitude: Double = 0.0,
    var estimatedDiameter: Double = 0.0,
    var relativeVelocity: Double = 0.0,
    var distanceFromEarth: Double = 0.0,
    var isPotentiallyHazardous: Boolean = true,
    var closeApproachDateInt: Long = 0
) : Parcelable {
    fun covert() {
        closeApproachDate.forEach {
            if (it != '-') {
                closeApproachDateInt = it.digitToInt() + closeApproachDateInt
                closeApproachDateInt = closeApproachDateInt * 10
            }
        }
    }

    fun doubleToString(num: Double): String {
        return num.toString()
    }

    fun pot(): String {
        if (isPotentiallyHazardous) {
            return "Potential Hazardous Status Image"
        } else {
            return "Is Not Potential Hazardous Status Image"
        }
    }

    fun pot2(): String {
        if (isPotentiallyHazardous) {
            return "Potential Hazardous Status Image"
        } else {
            return "Is Not Potential Hazardous Status Image"
        }
    }
}