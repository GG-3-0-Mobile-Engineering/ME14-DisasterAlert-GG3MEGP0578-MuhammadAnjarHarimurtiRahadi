package com.example.disasteralert.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.disasteralert.data.local.entity.DisasterEntity
import com.example.disasteralert.domain.repository.DisasterRepository
import com.example.disasteralert.helper.SettingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val disasterRepository: DisasterRepository,
    private val pref: SettingPreferences
) : ViewModel() {

    private var isFilter: Boolean = false
    private var startDateFilter: String = ""
    private var endDateFilter: String = ""

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

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
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
}