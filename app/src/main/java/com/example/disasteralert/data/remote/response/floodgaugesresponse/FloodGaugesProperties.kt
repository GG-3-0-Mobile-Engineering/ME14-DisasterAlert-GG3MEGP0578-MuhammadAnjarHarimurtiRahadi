package com.example.disasteralert.data.remote.response.floodgaugesresponse

import com.google.gson.annotations.SerializedName

data class FloodGaugesProperties(

	@field:SerializedName("gaugenameid")
	val gaugenameid: String,

	@field:SerializedName("observations")
	val observations: List<ObservationsItem>,

	@field:SerializedName("gaugeid")
	val gaugeid: String
)