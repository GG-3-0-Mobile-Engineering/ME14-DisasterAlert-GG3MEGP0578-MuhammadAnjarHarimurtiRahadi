package com.example.disasteralert.helper

import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object Util {

    fun getDateApiFormat(date: String): String {
        return date + "T00:00:00+0700"
    }

    fun getDatePresentationFormat(date: String): String {
        val dateTransform = LocalDateTime.parse(date.substring(0,19))
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss")
        val formatted = dateTransform.format(formatter)
        return formatted.toString()
    }

    fun getProvinceName(placeFormat: String): String = Constant.AREA[placeFormat].toString()

    fun getLatLngFormat(lat: Double, lon: Double) = LatLng(lat,lon)

}