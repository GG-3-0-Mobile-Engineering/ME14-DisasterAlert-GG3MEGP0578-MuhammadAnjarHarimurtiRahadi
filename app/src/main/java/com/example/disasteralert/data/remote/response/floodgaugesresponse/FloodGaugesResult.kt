package com.example.disasteralert.data.remote.response.floodgaugesresponse

import com.example.disasteralert.data.remote.response.disasterresponse.Objects
import com.google.gson.annotations.SerializedName

data class FloodGaugesResult(

    @field:SerializedName("objects")
	val objects: FloodGaugesObjects,
)