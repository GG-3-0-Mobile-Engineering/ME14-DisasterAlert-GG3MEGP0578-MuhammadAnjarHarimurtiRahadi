package com.example.disasteralert.data.remote.response

import com.google.gson.annotations.SerializedName

data class Output(

	@field:SerializedName("geometries")
	val geometries: List<GeometriesItem>,

	@field:SerializedName("type")
	val type: String
)