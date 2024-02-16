package com.example.android.asteroidradar

import android.app.Application
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidRadarApp : Application() {
    private val appScope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        setupWork()
    }

    private fun setupWork() {
        appScope.launch {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(true)
                .build()

            val syncWorkRequest =
                PeriodicWorkRequestBuilder<Worker>(
                    1,
                    TimeUnit.DAYS
                )
                    .setConstraints(constraints)
                    .build()

            WorkManager.getInstance().enqueueUniquePeriodicWork(
                "Worker",
                ExistingPeriodicWorkPolicy.KEEP,
                syncWorkRequest
            )
        }
    }
}
