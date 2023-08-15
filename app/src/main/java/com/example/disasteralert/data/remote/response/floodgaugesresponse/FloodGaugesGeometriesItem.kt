package com.example.disasteralert.data.remote.response.floodgaugesresponse

import com.google.gson.annotations.SerializedName

data class FloodGaugesGeometriesItem(

	@field:SerializedName("properties")
	val floodGaugesProperties: FloodGaugesProperties
)