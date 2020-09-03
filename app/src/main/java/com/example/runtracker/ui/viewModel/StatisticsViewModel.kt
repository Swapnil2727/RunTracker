package com.example.runtracker.ui.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.runtracker.repository.MainRepository

class StatisticsViewModel @ViewModelInject  constructor(
    val mainRepository: MainRepository
):ViewModel(){
}