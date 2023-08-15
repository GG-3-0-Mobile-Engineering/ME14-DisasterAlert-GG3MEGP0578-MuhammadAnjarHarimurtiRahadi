package com.example.disasteralert.helper

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class UtilTest {

    private lateinit var util: Util

    @RelaxedMockK
    private lateinit var contextMock : Context

    @Before
    fun initMockK() {
        contextMock = mockk()
        util = Util
    }

    @Test
    fun check_getDateApiFormat() {
        val dateInputMock = "12-02-2023"
        val dateResultMock = "12-02-2023T00:00:00+0700"


        val actualValue = util.getDateApiFormat(dateInputMock)

        assert(actualValue == dateResultMock)
    }

    @Test
    fun check_getDatePresentationFormat() {
        val dateInputMock = "2023-08-14T07:02:01.856Z"
        val dateFormattedMock = "14 Aug 2023 07:02:01"


        val actualValue = util.getDatePresentationFormat(dateInputMock)

        assert(actualValue == dateFormattedMock)
    }

    @Test
    fun check_getProvinceName() {
        val provinceInputMock = "ID-JI"
        val provinceResultMock = "Jawa Timur"

        val actualValue = util.getProvinceName(provinceInputMock)

        assertEquals(provinceResultMock, actualValue)
    }

    @Test
    fun check_getLatLngFormat() {
        val latitudeMock = -7.5887484709
        val longitudeMock = 111.7968825656
        val positionMock = LatLng(-7.5887484709, 111.7968825656)

        val actualValue = util.getLatLngFormat(latitudeMock,longitudeMock)

        assertEquals(positionMock, actualValue)
    }

    @Test
    fun check_getAreaCode() {
        val selectedItemMock = "Jawa Timur"
        val selectedItemResultMock = "ID-JI"

        val actualValue = util.getAreaCode(selectedItemMock)

        assertEquals(selectedItemResultMock, actualValue)
    }

    @Test
    fun check_getAreaCode_EmptyValue() {
        val selectedItemMock = ""
        val selectedItemResultMock = ""

        val actualValue = util.getAreaCode(selectedItemMock)

        assertEquals(selectedItemResultMock, actualValue)
    }
}