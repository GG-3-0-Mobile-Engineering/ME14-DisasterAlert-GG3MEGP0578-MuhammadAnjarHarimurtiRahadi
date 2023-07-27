package com.example.disasteralert.data.remote.response.floodgaugesresponse

import com.example.disasteralert.data.remote.response.disasterresponse.Objects
import com.google.gson.annotations.SerializedName

data class FloodGaugesResult(

    @field:SerializedName("transform")
	val transform: Transform,

    @field:SerializedName("objects")
	val objects: Objects,

    @field:SerializedName("bbox")
	val bbox: List<Any>,

    @field:SerializedName("type")
	val type: String,

    @field:SerializedName("arcs")
	val arcs: List<Any>
)