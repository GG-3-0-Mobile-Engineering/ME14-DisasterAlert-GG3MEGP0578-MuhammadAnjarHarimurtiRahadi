package com.example.disasteralert.data.remote.response.disasterresponse

import com.google.gson.annotations.SerializedName

data class Properties(

	@field:SerializedName("pkey")
	val pkey: String,

	@field:SerializedName("image_url")
	val imageUrl: String,

	@field:SerializedName("disaster_type")
	val disasterType: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("tags")
	val tags: Tags,
)