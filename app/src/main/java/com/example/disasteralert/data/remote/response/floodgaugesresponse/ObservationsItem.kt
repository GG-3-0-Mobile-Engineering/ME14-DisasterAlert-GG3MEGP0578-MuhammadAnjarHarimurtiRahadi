package com.example.disasteralert.data.remote.response.floodgaugesresponse

import com.google.gson.annotations.SerializedName

data class ObservationsItem(

	@field:SerializedName("f1")
	val f1: String,

	@field:SerializedName("f2")
	val f2: Int,

	@field:SerializedName("f3")
	val f3: Int,

	@field:SerializedName("f4")
	val f4: String
)