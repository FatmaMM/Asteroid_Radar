package com.example.asteroidrader.repo

import android.content.Context
import android.content.res.AssetManager
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.asteroidrader.Constants
import com.example.asteroidrader.api.RetrofitClientBuilder
import com.example.asteroidrader.api.parseAsteroidsJsonResult
import com.example.asteroidrader.database.AsteroidDatabase
import com.example.asteroidrader.model.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.InputStream
import android.text.format.DateFormat;
import com.example.asteroidrader.BuildConfig
import com.example.asteroidrader.Constants.api_key
import com.example.asteroidrader.MyApp
import com.example.asteroidrader.api.isNetworkAvailable
import java.text.SimpleDateFormat
import java.util.*


class Repository(private val database: AsteroidDatabase) {

    val allAsteroids: LiveData<List<Asteroid>> = database.asteroidDao.getALLAsteroids()

    val todayAsteroids: LiveData<List<Asteroid>> =
        database.asteroidDao.getAsteroidsForToday(getFormattedDate())

    val weekAsteroids: LiveData<List<Asteroid>> =
        database.asteroidDao.getAsteroidsWithinSpecificDate(
            getFormattedDate(),
            getFormattedDate(7)
        )


    suspend fun refreshAsteroids(context: Context) {
        withContext(Dispatchers.IO) {
            if (isNetworkAvailable(context)) {
                val asteroids = RetrofitClientBuilder.retrofitService.getAsteroids(getApiKey(),getFormattedDate(),
                    getFormattedDate(7))
                val result = parseAsteroidsJsonResult(JSONObject(asteroids))
                database.asteroidDao.insertAsteroids(*result.toTypedArray())
            }
        }
    }

    suspend fun deleteOldAsteroid() {
        withContext(Dispatchers.IO) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val today = getFormattedDate()
                database.asteroidDao.deleteOldAsteroid(today)
            }
        }
    }

    fun getApiKey(): String {
        return BuildConfig.API_KEY
    }


    fun getFormattedDate(days: Int = 0): String {
        val calendar = GregorianCalendar()
        if (days > 0) {
            calendar.add(Calendar.DATE, days)
        }
        val currentTime = calendar.time
        val dateFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        } else {
            DateFormat.getTimeFormat(MyApp.instance)
        }
        return dateFormat.format(currentTime)
    }
}