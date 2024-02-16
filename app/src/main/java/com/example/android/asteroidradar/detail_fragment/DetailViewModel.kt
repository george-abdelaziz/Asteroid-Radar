package com.example.android.asteroidradar.detail_fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.android.asteroidradar.database.Asteroid
import com.example.android.asteroidradar.database.AsteroidDao

class DetailViewModel(
    val database: AsteroidDao, application: Application, val id: Long
) : AndroidViewModel(application) {
    val asteroid: LiveData<Asteroid?> = database.getItem(id)
}