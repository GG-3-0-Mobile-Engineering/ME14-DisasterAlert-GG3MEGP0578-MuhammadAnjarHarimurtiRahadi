package com.example.disasteralert.data.remote.response

import com.google.gson.annotations.SerializedName

data class Objects(

	@field:SerializedName("output")
	val output: Output
)