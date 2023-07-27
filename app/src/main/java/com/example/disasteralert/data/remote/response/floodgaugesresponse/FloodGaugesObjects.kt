package com.example.disasteralert.data.remote.response.floodgaugesresponse

import com.google.gson.annotations.SerializedName

data class FloodGaugesObjects(

	@field:SerializedName("output")
	val output: FloodGaugesOutput
)