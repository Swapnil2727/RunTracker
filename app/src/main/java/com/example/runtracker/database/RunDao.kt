package com.example.runtracker.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RunDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: Run)

    @Delete
    suspend fun deleteRun(run: Run)

    @Query("SELECT * FROM running_table ORDER BY timestamp DESC")
    fun getAllRunsSortedByDate(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY timeInMillis DESC")
    fun getAllRunsSortedByTimeInMillis(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY caloriesBurned DESC")
    fun getAllRunsSortedByCaloriesBurned(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY avgSpeedInKMH DESC")
    fun getAllRunsSortedByAvgSpeed(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY distanceInMeters DESC")
    fun getAllRunsSortedByDistance(): LiveData<List<Run>>

    @Query("select SUM(timeInMillis) from running_table ")
    fun getTotalRunTime(): LiveData<Long>

    @Query("select SUM(caloriesBurned) from running_table ")
    fun getTotalCaloriesBurned(): LiveData<Int>

    @Query("select SUM(distanceInMeters) from running_table ")
    fun getTotalAverageSpeed(): LiveData<Float>

    @Query("select SUM(avgSpeedInKMH) from running_table ")
    fun getTotalAvgSpeed(): LiveData<Float>






}