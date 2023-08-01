package com.example.disasteralert.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "disaster")
data class DisasterEntity(
    @field:ColumnInfo(name = "pkey")
    @field:PrimaryKey
    val pKey: String,

    @field:ColumnInfo(name = "disasterType")
    val disasterType: String,

    @field:ColumnInfo(name = "disasterImageUrl")
    val disasterImageUrl: String? = null,

    @field:ColumnInfo(name = "disasterLoc")
    val disasterLoc: String? = null,

    @field:ColumnInfo(name = "latitude")
    val latitude: Double,

    @field:ColumnInfo(name = "longitude")
    val longitude: Double,

    @field:ColumnInfo(name = "disasterDate")
    val disasterDate: String,

)
