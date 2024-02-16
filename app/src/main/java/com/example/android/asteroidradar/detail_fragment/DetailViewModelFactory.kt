package com.example.android.asteroidradar.detail_fragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.asteroidradar.database.AsteroidDao

class DetailViewModelFactory(
    private val database: AsteroidDao,
    private val application: Application,
    private val id: Long
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(database, application, id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}