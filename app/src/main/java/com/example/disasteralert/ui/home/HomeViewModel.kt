package com.example.disasteralert.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.disasteralert.data.local.entity.DisasterEntity
import com.example.disasteralert.domain.repository.DisasterRepository
import com.example.disasteralert.domain.usecase.DisasterUseCase
import com.example.disasteralert.helper.SettingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val disasterUseCase: DisasterUseCase
) : ViewModel() {

    var currentFilter: String = ""
    var isFilter: Boolean = false
    var startDateFilter: String = ""
    var endDateFilter: String = ""

    init {
        getApiDisasterData()
    }

    fun getApiDisasterData() {
        viewModelScope.launch {
            disasterUseCase.getApiDisasterData()
        }
    }

    fun getPeriodicDisasterData(
        startDate: String = "", endDate: String = ""
    ) = disasterUseCase.getPeriodicDisasterData(startDate, endDate)

    fun getAllDisasterData(
        locFilter: String, disasterFilter: String
    ): LiveData<List<DisasterEntity>> = disasterUseCase.getAllDisasterData(locFilter, disasterFilter)

    fun getThemeSettings(): LiveData<Boolean> {
        return disasterUseCase.getThemeSettings()
    }
    
}