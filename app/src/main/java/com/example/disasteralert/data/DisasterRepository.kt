package com.example.disasteralert.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.disasteralert.data.local.entity.DisasterEntity
import com.example.disasteralert.data.local.room.DisasterDao
import com.example.disasteralert.data.remote.response.floodgaugesresponse.FloodGaugesResponse
import com.example.disasteralert.data.remote.service.DisasterAPI
import com.example.disasteralert.helper.Util

class DisasterRepository private constructor(
    private val apiService: DisasterAPI, private val disasterDao: DisasterDao
) {

    suspend fun getApiDisasterData() {
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

    fun getPeriodicDisasterData(
        startDate: String = "", endDate: String = ""
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

    fun getAllDisasterData(): LiveData<List<DisasterEntity>> {
        return disasterDao.getAllDisaster()
    }

    fun getDataByLocation(
        locFilter: String
    ): LiveData<List<DisasterEntity>> {
        return disasterDao.getDataByLocation(locFilter)
    }

    fun getDataByDisaster(
        disasterFilter: String
    ): LiveData<List<DisasterEntity>> {
        return disasterDao.getDataByDisaster(disasterFilter)
    }

    fun getDataByLocationAndDisaster(
        locFilter: String, disasterFilter: String
    ): LiveData<List<DisasterEntity>> {
        return disasterDao.getDataByLocationAndDisaster(locFilter, disasterFilter)
    }

    fun getFloodGaugesData(): LiveData<Results<FloodGaugesResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiService.getFloodGaugesData()
            emit(Results.Success(response))

        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: DisasterRepository? = null
        fun getInstance(
            apiService: DisasterAPI, disasterDao: DisasterDao
        ): DisasterRepository = instance ?: synchronized(this) {
            instance ?: DisasterRepository(apiService, disasterDao)
        }.also { instance = it }
    }
}