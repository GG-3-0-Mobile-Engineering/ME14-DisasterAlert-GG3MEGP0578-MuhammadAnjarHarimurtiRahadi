package com.example.disasteralert.domain.usecase

import androidx.lifecycle.LiveData
import com.example.disasteralert.data.Results
import com.example.disasteralert.data.local.entity.DisasterEntity

interface DisasterUseCase {

    suspend fun getApiDisasterData()

    fun getPeriodicDisasterData(startDate: String = "", endDate: String = ""): LiveData<Results<List<DisasterEntity>>>

    fun getAllDisasterData(locFilter: String, disasterFilter: String): LiveData<List<DisasterEntity>>

    fun getThemeSettings(): LiveData<Boolean>

    suspend fun saveThemeSetting(isDarkModeActive: Boolean)

    fun getNotificationSettings(): LiveData<Boolean>

    suspend fun saveNotificationSetting(isNotificationActive: Boolean)
}