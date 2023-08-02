package com.example.disasteralert.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.disasteralert.data.DisasterRepository
import com.example.disasteralert.data.local.entity.DisasterEntity
import com.example.disasteralert.helper.SettingPreferences
import kotlinx.coroutines.launch

class HomeViewModel(
    private val disasterRepository: DisasterRepository, private val pref: SettingPreferences
) : ViewModel() {

    init {
        getApiDisasterData()
    }

    fun getApiDisasterData() {
        viewModelScope.launch {
            disasterRepository.getApiDisasterData()
        }
    }

    fun getPeriodicDisasterData(
        startDate: String = "", endDate: String = ""
    ) = disasterRepository.getPeriodicDisasterData(startDate, endDate)

    fun getAllDisasterData(
        locFilter: String, disasterFilter: String
    ): LiveData<List<DisasterEntity>>? {
        return if (disasterFilter.isNotBlank() && locFilter.isNotBlank()) {
            disasterRepository.getDataByLocationAndDisaster(locFilter, disasterFilter.lowercase())
        } else if (disasterFilter.isNotBlank()) {
            disasterRepository.getDataByDisaster(disasterFilter.lowercase())
        } else if (locFilter.isNotBlank()) {
            disasterRepository.getDataByLocation(locFilter)
        } else disasterRepository.getAllDisasterData()
    }

    fun getFloodGaugesData() = disasterRepository.getFloodGaugesData()

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }
}