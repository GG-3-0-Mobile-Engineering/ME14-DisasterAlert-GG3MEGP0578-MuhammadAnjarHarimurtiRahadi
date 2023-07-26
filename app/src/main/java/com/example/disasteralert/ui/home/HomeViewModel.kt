package com.example.disasteralert.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.disasteralert.data.DisasterRepository
import com.example.disasteralert.helper.SettingPreferences
import kotlinx.coroutines.launch

class HomeViewModel(
    private val disasterRepository: DisasterRepository, private val pref: SettingPreferences
) : ViewModel() {

    fun getAllDisasterData(locFilter: String, disasterFilter: String) =
        disasterRepository.getAllDisasterData(locFilter, disasterFilter)

    fun getLatestFilter(): LiveData<String> {
        return pref.getLatestFilter().asLiveData()
    }

    fun saveLatestFilter(latestFilter: String) {
        viewModelScope.launch {
            pref.saveLatestFilter(latestFilter)
        }
    }
}