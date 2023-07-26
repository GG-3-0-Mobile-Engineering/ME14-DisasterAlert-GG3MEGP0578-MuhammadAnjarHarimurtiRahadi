package com.example.disasteralert.helper

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object Util {

    fun getEndDate(): String {
        val calendar = Calendar.getInstance()

        val current = LocalDateTime.now()
        return current.toString().substring(0,19) + "+0700"
    }

    fun getStartDate(): String {
        val currentDate = getEndDate()

        return currentDate.substring(0,11) + "00:00:00+0700"
    }

}