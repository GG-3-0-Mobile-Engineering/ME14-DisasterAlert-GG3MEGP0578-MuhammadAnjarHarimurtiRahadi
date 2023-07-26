package com.example.disasteralert.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.disasteralert.data.remote.response.DisasterResponse
import com.example.disasteralert.data.remote.service.DisasterAPI

class DisasterRepository private constructor(
    private val apiService: DisasterAPI,
) {

    fun getAllDisasterData(): LiveData<Results<DisasterResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiService.getAllDisasterAlert()
            emit(Results.Success(response))
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