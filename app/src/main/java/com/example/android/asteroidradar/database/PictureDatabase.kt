package com.example.android.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android.pictureOfDayradar.database.PictureDao

@Database(entities = [PictureOfDay::class], version = 3, exportSchema = false)
abstract class PictureDatabase : RoomDatabase() {

    abstract val pictureDaoRepository: PictureDao

    companion object {
        @Volatile
        private var INSTANCE: PictureDatabase? = null
        fun getInstance(context: Context): PictureDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PictureDatabase::class.java,
                        "picture_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
