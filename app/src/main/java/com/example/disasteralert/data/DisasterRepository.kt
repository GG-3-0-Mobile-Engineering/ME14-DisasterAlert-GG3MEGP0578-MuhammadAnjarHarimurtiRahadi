package com.example.disasteralert.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.disasteralert.data.remote.response.DisasterResponse
import com.example.disasteralert.data.remote.service.DisasterAPI
import com.example.disasteralert.helper.Util

class DisasterRepository private constructor(
    private val apiService: DisasterAPI,
) {

    fun getAllDisasterData(filter: String): LiveData<Results<DisasterResponse>> = liveData {
        emit(Results.Loading)
        try {
            if (filter.isBlank()) {
                val response = apiService.getAllDisasterData()
                emit(Results.Success(response))
            }
            else {
                val response = apiService.getDisasterDataByLocation(
                    start = Util.getStartDate(),
                    end = Util.getEndDate(),
                    location = filter
                )
                emit(Results.Success(response))
            }

        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: DisasterRepository? = null
        fun getInstance(
            apiService: DisasterAPI
        ): DisasterRepository = instance ?: synchronized(this) {
            instance ?: DisasterRepository(apiService)
        }.also { instance = it }
    }
}