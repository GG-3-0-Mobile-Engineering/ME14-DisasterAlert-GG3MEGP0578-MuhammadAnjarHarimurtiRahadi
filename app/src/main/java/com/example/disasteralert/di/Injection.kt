package com.example.disasteralert.di

import android.content.Context
import com.example.disasteralert.data.DisasterRepository
import com.example.disasteralert.data.local.room.DisasterDatabase
import com.example.disasteralert.data.remote.service.DisasterServiceAPI

object Injection {
    fun provideRepository(context: Context): DisasterRepository {
        val apiService = DisasterServiceAPI.getApiService()
        val database = DisasterDatabase.getInstance(context)
        val dao = database.disasterDao()
        return DisasterRepository.getInstance(apiService, dao)
    }
}