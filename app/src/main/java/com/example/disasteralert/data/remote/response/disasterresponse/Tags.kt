package com.example.disasteralert.data.remote.response.disasterresponse

import com.google.gson.annotations.SerializedName

data class Tags(

	@field:SerializedName("instance_region_code")
	val instanceRegionCode: String,
)