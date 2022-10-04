package com.example.asteroidrader.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.asteroidrader.model.Asteroid

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM asteroid ORDER BY closeApproachDate DESC")
    fun getALLAsteroids(): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroid WHERE closeApproachDate = :startDate ORDER BY closeApproachDate DESC")
    fun getAsteroidsForToday(startDate: String): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroid WHERE closeApproachDate BETWEEN :startDate AND :endDate ORDER BY closeApproachDate DESC")
    fun getAsteroidsWithinSpecificDate(startDate: String, endDate: String): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroids(vararg asteroid: Asteroid)

    @Query("delete from asteroid where closeApproachDate < :today")
    fun deleteOldAsteroid(today: String)
}
