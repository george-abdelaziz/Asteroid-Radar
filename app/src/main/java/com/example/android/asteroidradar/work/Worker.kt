package com.example.android.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android.asteroidradar.api.AsteroidRepository
import retrofit2.HttpException

class Worker(
    appContext: Context, params: WorkerParameters, val reprository: AsteroidRepository
) : CoroutineWorker(appContext, params) {
    companion object {
        const val WorkerName = "Worker"
    }

    override suspend fun doWork(): Result {
        return try {
            reprository.onStarUp()
            return Result.success()
        } catch (exception: HttpException) {
            return Result.retry()
        }
    }
}
