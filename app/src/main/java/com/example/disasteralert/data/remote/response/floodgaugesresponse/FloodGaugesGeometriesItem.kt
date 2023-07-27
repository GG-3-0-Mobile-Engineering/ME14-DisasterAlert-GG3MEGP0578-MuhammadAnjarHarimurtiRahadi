package com.example.disasteralert.data.remote.response.floodgaugesresponse

import com.google.gson.annotations.SerializedName

data class FloodGaugesGeometriesItem(

	@field:SerializedName("coordinates")
	val coordinates: List<Int>,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("properties")
	val floodGaugesProperties: FloodGaugesProperties
)