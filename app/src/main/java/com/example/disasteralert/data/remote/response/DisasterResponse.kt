package com.example.disasteralert.data.remote.response

import com.google.gson.annotations.SerializedName

data class DisasterResponse(

	@field:SerializedName("result")
	val result: Result,

	@field:SerializedName("statusCode")
	val statusCode: Int
)