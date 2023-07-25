package com.example.disasteralert.di

import android.content.Context
import com.example.disasteralert.data.DisasterRepository
import com.example.disasteralert.data.remote.service.DisasterServiceAPI

object Injection {
    fun provideRepository(context: Context): DisasterRepository {
        val apiService = DisasterServiceAPI.getApiService()
        return DisasterRepository.getInstance(apiService)
    }
}