package com.example.runtracker.ui.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runtracker.database.Run
import com.example.runtracker.other.SortType
import com.example.runtracker.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject  constructor(
    val mainRepository: MainRepository
):ViewModel(){


    private val runSortedByDate = mainRepository.getAllRunSortedByDate()

    private val runSortedByAvgSpeed = mainRepository.getAllRunSortedByAvgSpeed()
    private val runSortedByCaloriesBurned = mainRepository.getAllRunsSortedByCaloriesBurned()
    private val runSortedByDistance = mainRepository.getAllRunsSortedByDistance()
    private val runSortedByTimeInMillis = mainRepository.getAllRunsSortedByTimeInMillis()


    val runs = MediatorLiveData<List<Run>>()

    var sortType = SortType.DATE
    init {
        runs.addSource(runSortedByDate){
            result ->
            if(sortType == SortType.DATE){
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runSortedByAvgSpeed){
                result ->
            if(sortType == SortType.AVG_SPEED){
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runSortedByCaloriesBurned){
                result ->
            if(sortType == SortType.CALORIES_BURNED){
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runSortedByDistance){
                result ->
            if(sortType == SortType.DISTANCE){
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runSortedByTimeInMillis){
                result ->
            if(sortType == SortType.TIME_IN_MILLIS){
                result?.let { runs.value = it }
            }
        }
    }

    fun sortRuns(sortType: SortType) = when(sortType){
        SortType.DATE -> runSortedByDate.value?.let { runs.value = it }
        SortType.CALORIES_BURNED -> runSortedByCaloriesBurned.value?.let { runs.value = it }
        SortType.AVG_SPEED -> runSortedByAvgSpeed.value?.let { runs.value = it }
        SortType.DISTANCE -> runSortedByDistance.value?.let { runs.value = it }
        SortType.TIME_IN_MILLIS -> runSortedByTimeInMillis.value?.let { runs.value = it }
    }.also {
        this.sortType = sortType
    }
    fun insertRun(run: Run) = viewModelScope.launch {
        mainRepository.insertRun(run)
    }
}