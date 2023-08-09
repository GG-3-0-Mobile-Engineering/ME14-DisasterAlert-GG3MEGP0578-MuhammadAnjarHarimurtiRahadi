package com.example.disasteralert.domain.usecase

import androidx.lifecycle.LiveData
import com.example.disasteralert.data.Results
import com.example.disasteralert.data.local.entity.DisasterEntity
import com.example.disasteralert.domain.repository.DisasterRepository
import javax.inject.Inject

class DisasterInteractor @Inject constructor(
    private val disasterRepository: DisasterRepository
) : DisasterUseCase {
    override suspend fun getApiDisasterData() {
        disasterRepository.getApiDisasterData()
    }

    override fun getPeriodicDisasterData(
        startDate: String, endDate: String
    ): LiveData<Results<List<DisasterEntity>>> =
        disasterRepository.getPeriodicDisasterData(startDate, endDate)

    override fun getAllDisasterData(
        locFilter: String, disasterFilter: String
    ): LiveData<List<DisasterEntity>> {
        return if (disasterFilter.isNotBlank() && locFilter.isNotBlank()) {
            disasterRepository.getDataByLocationAndDisaster(locFilter, disasterFilter.lowercase())
        } else if (disasterFilter.isNotBlank()) {
            disasterRepository.getDataByDisaster(disasterFilter.lowercase())
        } else if (locFilter.isNotBlank()) {
            disasterRepository.getDataByLocation(locFilter)
        } else disasterRepository.getAllDisasterData()
    }

    override fun getThemeSettings(): LiveData<Boolean> = disasterRepository.getThemeSettings()

    override suspend fun saveThemeSetting(isDarkModeActive: Boolean) =
        disasterRepository.saveThemeSetting(isDarkModeActive)

    override fun getNotificationSettings(): LiveData<Boolean> =
        disasterRepository.getNotificationSettings()

    override suspend fun saveNotificationSetting(isNotificationActive: Boolean) =
        disasterRepository.saveNotificationSetting(isNotificationActive)
}