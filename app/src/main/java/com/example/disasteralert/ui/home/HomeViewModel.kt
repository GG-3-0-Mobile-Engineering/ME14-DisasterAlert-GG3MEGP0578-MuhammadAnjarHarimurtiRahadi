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

    private var currentFilter: String = ""
    private var isFilter: Boolean = false
    private var startDateFilter: String = ""
    private var endDateFilter: String = ""

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

    fun setFilterStatus(isFilter: Boolean) {
        this.isFilter = isFilter
    }

    fun getFilterStatus(): Boolean = isFilter

    fun setDateStatus(startDate: String, endDate: String) {
        this.startDateFilter = startDate
        this.endDateFilter = endDate
    }

    fun getStartDate(): String = startDateFilter

    fun getEndDate(): String = endDateFilter

    fun setCurrentFilter(currentFilter: String) {
        this.currentFilter = currentFilter
    }

    fun getCurrentFilter(): String = currentFilter
}