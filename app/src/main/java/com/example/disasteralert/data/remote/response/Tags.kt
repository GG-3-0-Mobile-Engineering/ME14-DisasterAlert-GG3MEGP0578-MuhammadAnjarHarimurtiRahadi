package com.example.disasteralert.data.remote.response

import com.google.gson.annotations.SerializedName

data class Tags(

	@field:SerializedName("instance_region_code")
	val instanceRegionCode: String,

	@field:SerializedName("district_id")
	val districtId: Any,

	@field:SerializedName("local_area_id")
	val localAreaId: String
)