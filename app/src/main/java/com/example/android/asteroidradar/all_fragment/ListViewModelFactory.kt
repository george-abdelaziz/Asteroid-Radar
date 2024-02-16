package com.example.android.asteroidradar.all_fragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.asteroidradar.api.AsteroidRepository
import com.example.android.asteroidradar.database.AsteroidDao
import com.example.android.pictureOfDayradar.database.PictureDao

class ListViewModelFactory(
    private val picDatabse: PictureDao,
    private val database: AsteroidDao,
    private val repo: AsteroidRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(picDatabse, database, repo, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}