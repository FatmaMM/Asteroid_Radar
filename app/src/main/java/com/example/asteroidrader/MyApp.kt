package com.example.asteroidrader

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.work.*
import com.example.asteroidrader.worker.RefresherWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MyApp : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)


    companion object {
        lateinit var instance: MyApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED).setRequiresCharging(true).build()

            val repeatingRequest = PeriodicWorkRequestBuilder<RefresherWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints).build()

            WorkManager.getInstance().enqueueUniquePeriodicWork(
                RefresherWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest
            )
        }
    }


}