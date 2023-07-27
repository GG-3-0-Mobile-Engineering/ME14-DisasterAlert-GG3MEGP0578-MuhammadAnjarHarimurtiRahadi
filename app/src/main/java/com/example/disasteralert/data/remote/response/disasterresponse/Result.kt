package com.example.disasteralert.data.remote.response.disasterresponse

import com.google.gson.annotations.SerializedName

data class Result(

	@field:SerializedName("objects")
	val objects: Objects,
)