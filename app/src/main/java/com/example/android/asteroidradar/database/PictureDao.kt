package com.example.android.pictureOfDayradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.asteroidradar.database.PictureOfDay

@Dao
interface PictureDao {

    fun insertPhoto(pic: PictureOfDay) {
        deleteAll()
        insertItem(pic)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(pictureOfDay: PictureOfDay)


    @Query("DELETE FROM picture_table")
    fun deleteAll()

    @Query("SELECT * FROM picture_table")
    fun getAll(): LiveData<PictureOfDay?>

    @Query("SELECT * FROM picture_table")
    fun getAllList(): PictureOfDay?
}