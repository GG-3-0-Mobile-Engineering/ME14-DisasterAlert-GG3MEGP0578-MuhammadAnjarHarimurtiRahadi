package com.example.disasteralert.data.remote.response.disasterresponse

import com.google.gson.annotations.SerializedName

data class Output(

	@field:SerializedName("geometries")
	val geometries: List<GeometriesItem>,
)