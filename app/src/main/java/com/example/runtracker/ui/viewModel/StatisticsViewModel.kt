package com.example.runtracker.ui.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.runtracker.repository.MainRepository

class StatisticsViewModel @ViewModelInject  constructor(
    val mainRepository: MainRepository
):ViewModel(){


    val totalTimeRun = mainRepository.getTotalRunTime()
    val totalDistance = mainRepository.getTotalDistance()
    val totalCaloriesBurned = mainRepository.getTotalCaloriesBurned()
    val totalAverageSpeed = mainRepository.getTotalAvgSpeed()

    val runsSortedByDate = mainRepository.getAllRunSortedByDate()
}