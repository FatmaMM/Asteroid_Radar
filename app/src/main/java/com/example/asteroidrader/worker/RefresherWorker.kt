package com.example.asteroidrader.worker

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.asteroidrader.database.getDatabase
import com.example.asteroidrader.repo.Repository
import retrofit2.HttpException

class RefresherWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    companion object {
        const val WORK_NAME = "RefresherWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = Repository(database)

        return try {
            repository.refreshAsteroids(applicationContext)
            repository.deleteOldAsteroid()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}