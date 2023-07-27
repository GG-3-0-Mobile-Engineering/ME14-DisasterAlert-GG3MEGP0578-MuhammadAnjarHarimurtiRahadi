package com.example.disasteralert.data.remote.response.floodgaugesresponse

import com.google.gson.annotations.SerializedName

data class FloodGaugesOutput(

	@field:SerializedName("geometries")
	val geometries: List<FloodGaugesGeometriesItem>,
)