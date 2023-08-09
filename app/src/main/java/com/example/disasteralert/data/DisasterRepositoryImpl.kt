package com.example.disasteralert.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.disasteralert.data.local.entity.DisasterEntity
import com.example.disasteralert.data.local.room.DisasterDao
import com.example.disasteralert.data.remote.response.floodgaugesresponse.FloodGaugesGeometriesItem
import com.example.disasteralert.data.remote.response.floodgaugesresponse.FloodGaugesResponse
import com.example.disasteralert.data.remote.service.DisasterAPI
import com.example.disasteralert.domain.repository.DisasterRepository
import com.example.disasteralert.helper.SettingPreferences
import com.example.disasteralert.helper.Util
import kotlinx.coroutines.launch
import javax.inject.Inject

class DisasterRepositoryImpl @Inject constructor(
    private val apiService: DisasterAPI,
    private val disasterDao: DisasterDao,
    private val pref: SettingPreferences
) : DisasterRepository {
    override suspend fun getApiDisasterData() {
        try {
            val response = apiService.getAllDisasterData()
            val responseList = response.result.objects.output.geometries
            val disasterData = responseList.map { disaster ->
                DisasterEntity(
                    pKey = disaster.properties.pkey,
                    disasterType = disaster.properties.disasterType,
                    disasterImageUrl = disaster.properties.imageUrl,
                    disasterLoc = disaster.properties.tags.instanceRegionCode,
                    latitude = disaster.coordinates[1] as Double,
                    longitude = disaster.coordinates[0] as Double,
                    disasterDate = disaster.properties.createdAt
                )
            }
            disasterDao.deleteAllDisaster()
            disasterDao.insertDisaster(disasterData)
        } catch (e: Exception) {
            Log.d("Repository", "getData: ${e.message.toString()} ")
        }
    }

    override fun getPeriodicDisasterData(
        startDate: String,
        endDate: String
    ): LiveData<Results<List<DisasterEntity>>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiService.getDisasterDataByPeriod(
                Util.getDateApiFormat(startDate), Util.getDateApiFormat(endDate)
            )
            val responseList = response.result.objects.output.geometries
            val disasterData = responseList.map { disaster ->
                DisasterEntity(
                    pKey = disaster.properties.pkey,
                    disasterType = disaster.properties.disasterType,
                    disasterImageUrl = disaster.properties.imageUrl,
                    disasterLoc = disaster.properties.tags.instanceRegionCode,
                    latitude = disaster.coordinates[1] as Double,
                    longitude = disaster.coordinates[0] as Double,
                    disasterDate = disaster.properties.createdAt
                )
            }
            disasterDao.deleteAllDisaster()
            disasterDao.insertDisaster(disasterData)
        } catch (e: Exception) {
            Log.d("Repository", "getData: ${e.message.toString()} ")
            emit(Results.Error(e.message.toString()))
        }
    }

    override fun getAllDisasterData(): LiveData<List<DisasterEntity>> = disasterDao.getAllDisaster()

    override fun getDataByLocation(locFilter: String): LiveData<List<DisasterEntity>> = disasterDao.getDataByLocation(locFilter)

    override fun getDataByDisaster(disasterFilter: String): LiveData<List<DisasterEntity>> = disasterDao.getDataByDisaster(disasterFilter)

    override fun getDataByLocationAndDisaster(
        locFilter: String,
        disasterFilter: String
    ): LiveData<List<DisasterEntity>> = disasterDao.getDataByLocationAndDisaster(locFilter, disasterFilter)

    override suspend fun getFloodGaugesData() : Results<List<FloodGaugesGeometriesItem>> {
        return try {
            val response = apiService.getFloodGaugesData()
            val floodGaugesObsData = response.floodGaugesResult.objects.output.geometries

            Results.Success(floodGaugesObsData)

        } catch (e: Exception) {
            Results.Error(e.message.toString())
        }
    }

    override fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    override suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        pref.saveThemeSetting(isDarkModeActive)
    }

    override fun getNotificationSettings(): LiveData<Boolean> {
        return pref.getNotificationSetting().asLiveData()
    }

    override suspend fun saveNotificationSetting(isNotificationActive: Boolean) {
        pref.saveNotificationSetting(isNotificationActive)
    }

}