package com.example.asteroidrader.main

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.asteroidrader.api.RetrofitClientBuilder
import com.example.asteroidrader.api.isNetworkAvailable
import com.example.asteroidrader.database.getDatabase
import com.example.asteroidrader.model.Asteroid
import com.example.asteroidrader.model.PictureOfDay
import com.example.asteroidrader.repo.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class SelectedAsteroid {
    TODAY,
    WEEK,
    ALL
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val repo = Repository(database)

    val pictureOfDay = MutableLiveData<PictureOfDay>()
    val navigateToDetailAsteroid = MutableLiveData<Asteroid?>()
    private var filterAsteroid = MutableLiveData(SelectedAsteroid.ALL)

    var asteroidList = Transformations.switchMap(filterAsteroid) {

        when (it!!) {
            SelectedAsteroid.WEEK -> repo.weekAsteroids
            SelectedAsteroid.TODAY -> repo.todayAsteroids
            else -> repo.allAsteroids
        }
    }

    init {
        viewModelScope.launch {
            repo.refreshAsteroids(application)
            refreshPictureOfDay()
            if (!isNetworkAvailable(getApplication<Application?>().applicationContext))
                asteroidList = repo.allAsteroids
        }
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        navigateToDetailAsteroid.value = asteroid
    }

    fun onAsteroidNavigated() {
        navigateToDetailAsteroid.value = null
    }

    fun onChangeFilter(filter: SelectedAsteroid) {
        filterAsteroid.postValue(filter)
    }

    private suspend fun refreshPictureOfDay() {
        withContext(Dispatchers.IO) {
            val apiKey = repo.getApiKey()
            pictureOfDay.postValue(RetrofitClientBuilder.retrofitService.getPictureOfTheDay(apiKey))
        }
    }
}