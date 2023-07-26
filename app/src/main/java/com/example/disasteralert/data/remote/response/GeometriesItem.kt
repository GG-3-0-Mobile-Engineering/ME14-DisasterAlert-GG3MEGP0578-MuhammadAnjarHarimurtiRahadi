package com.example.disasteralert.data.remote.response

import com.google.gson.annotations.SerializedName

data class GeometriesItem(

	@field:SerializedName("coordinates")
	val coordinates: List<Any>,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("properties")
	val properties: Properties
)