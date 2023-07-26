package com.example.disasteralert.ui.home

import androidx.lifecycle.ViewModel
import com.example.disasteralert.data.DisasterRepository

class HomeViewModel(private val disasterRepository: DisasterRepository) : ViewModel() {

    fun getAllDisasterData(locFilter: String, disasterFilter: String) =
        disasterRepository.getAllDisasterData(locFilter, disasterFilter)
}