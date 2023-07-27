package com.example.disasteralert.data.remote.response.floodgaugesresponse

import com.google.gson.annotations.SerializedName

data class FloodGaugesResponse(

    @field:SerializedName("result")
	val floodGaugesResult: FloodGaugesResult,

    @field:SerializedName("statusCode")
	val statusCode: Int
)