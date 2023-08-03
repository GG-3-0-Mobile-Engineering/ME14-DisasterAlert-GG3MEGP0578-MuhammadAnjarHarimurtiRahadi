package com.example.disasteralert.domain.repository

import androidx.lifecycle.LiveData
import com.example.disasteralert.data.Results
import com.example.disasteralert.data.local.entity.DisasterEntity
import com.example.disasteralert.data.remote.response.floodgaugesresponse.FloodGaugesResponse

interface DisasterRepository {

    suspend fun getApiDisasterData()

    fun getPeriodicDisasterData(startDate: String = "", endDate: String = ""): LiveData<Results<List<DisasterEntity>>>

    fun getAllDisasterData(): LiveData<List<DisasterEntity>>

    fun getDataByLocation(locFilter: String): LiveData<List<DisasterEntity>>

    fun getDataByDisaster(disasterFilter: String): LiveData<List<DisasterEntity>>

    fun getDataByLocationAndDisaster(locFilter: String, disasterFilter: String): LiveData<List<DisasterEntity>>

    fun getFloodGaugesData(): LiveData<Results<FloodGaugesResponse>>
}