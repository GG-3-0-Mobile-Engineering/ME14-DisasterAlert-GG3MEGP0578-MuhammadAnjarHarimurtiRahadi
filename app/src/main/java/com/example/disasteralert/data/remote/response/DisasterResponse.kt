package com.example.disasteralert.data.remote.response

import com.google.gson.annotations.SerializedName

data class DisasterResponse(

	@field:SerializedName("result")
	val result: Result,

	@field:SerializedName("statusCode")
	val statusCode: Int
)

data class Geometry(

	@field:SerializedName("coordinates")
	val coordinates: List<Any>,

	@field:SerializedName("type")
	val type: String
)

data class ReportData(

	@field:SerializedName("report_type")
	val reportType: String,

	@field:SerializedName("condition")
	val condition: Int,

	@field:SerializedName("accessabilityFailure")
	val accessabilityFailure: Int,

	@field:SerializedName("structureFailure")
	val structureFailure: Int
)

data class Result(

	@field:SerializedName("features")
	val features: List<FeaturesItem>,

	@field:SerializedName("type")
	val type: String
)

data class FeaturesItem(

	@field:SerializedName("geometry")
	val geometry: Geometry,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("properties")
	val properties: Properties
)

data class Tags(

	@field:SerializedName("instance_region_code")
	val instanceRegionCode: String,

	@field:SerializedName("district_id")
	val districtId: Any,

	@field:SerializedName("local_area_id")
	val localAreaId: String,

	@field:SerializedName("region_code")
	val regionCode: String
)

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
	val title: Any,

	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("tags")
	val tags: Tags,

	@field:SerializedName("partner_icon")
	val partnerIcon: Any,

	@field:SerializedName("report_data")
	val reportData: ReportData,

	@field:SerializedName("pkey")
	val pkey: String,

	@field:SerializedName("text")
	val text: String,

	@field:SerializedName("partner_code")
	val partnerCode: Any,

	@field:SerializedName("status")
	val status: String
)
