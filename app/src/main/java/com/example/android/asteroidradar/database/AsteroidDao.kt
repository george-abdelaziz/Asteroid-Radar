package com.example.android.asteroidradar.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(asteroid: Asteroid)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroid: Asteroid)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroid: ArrayList<Asteroid>)

    @Update
    suspend fun updateItem(asteroid: Asteroid)

    @Delete
    suspend fun deleteItem(asteroid: Asteroid)

    @Query("DELETE FROM asteroid_table")
    suspend fun deleteAll()

    @Query("SELECT * from asteroid_table WHERE id = :id")
    fun getItem(id: Long): LiveData<Asteroid?>

    @Query("SELECT * FROM asteroid_table ORDER BY closeApproachDate DESC")
    fun getAll(): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroid_table ORDER BY closeApproachDate DESC")
    fun getAllList(): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroid_table WHERE closeApproachDateInt > :startDate AND closeApproachDateInt < :endDate ORDER BY closeApproachDateInt DESC")
    suspend fun filteredList(startDate: Long, endDate: Long):List<Asteroid>
}