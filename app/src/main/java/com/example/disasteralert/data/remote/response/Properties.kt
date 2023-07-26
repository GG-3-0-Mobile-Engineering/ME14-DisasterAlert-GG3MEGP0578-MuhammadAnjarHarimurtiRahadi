package com.example.disasteralert.data.remote.response

import com.google.gson.annotations.SerializedName

data class Properties(

	@field:SerializedName("image_url")
	val imageUrl: String,

	@field:SerializedName("disaster_type")
	val disasterType: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("source")
	val source: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("tags")
	val tags: Tags,

	@field:SerializedName("partner_icon")
	val partnerIcon: Any,

	@field:SerializedName("report_data")
	val reportData: Any,

	@field:SerializedName("pkey")
	val pkey: String,

	@field:SerializedName("text")
	val text: String,

	@field:SerializedName("partner_code")
	val partnerCode: Any,

	@field:SerializedName("status")
	val status: String
)