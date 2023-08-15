package com.example.disasteralert.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.disasteralert.MainDispatcherRule
import com.example.disasteralert.data.local.entity.DisasterEntity
import com.example.disasteralert.data.local.room.DisasterDao
import com.example.disasteralert.data.remote.response.disasterresponse.DisasterResponse
import com.example.disasteralert.data.remote.response.disasterresponse.GeometriesItem
import com.example.disasteralert.data.remote.response.disasterresponse.Objects
import com.example.disasteralert.data.remote.response.disasterresponse.Output
import com.example.disasteralert.data.remote.response.disasterresponse.Properties
import com.example.disasteralert.data.remote.response.disasterresponse.Result
import com.example.disasteralert.data.remote.response.disasterresponse.Tags
import com.example.disasteralert.data.remote.response.floodgaugesresponse.FloodGaugesGeometriesItem
import com.example.disasteralert.data.remote.response.floodgaugesresponse.FloodGaugesObjects
import com.example.disasteralert.data.remote.response.floodgaugesresponse.FloodGaugesOutput
import com.example.disasteralert.data.remote.response.floodgaugesresponse.FloodGaugesProperties
import com.example.disasteralert.data.remote.response.floodgaugesresponse.FloodGaugesResponse
import com.example.disasteralert.data.remote.response.floodgaugesresponse.FloodGaugesResult
import com.example.disasteralert.data.remote.response.floodgaugesresponse.ObservationsItem
import com.example.disasteralert.data.remote.service.DisasterAPI
import com.example.disasteralert.helper.SettingPreferences
import com.example.disasteralert.helper.Util
import com.example.disasteralert.ui.settings.SettingsViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class DisasterRepositoryImplTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var disasterAPI: DisasterAPI

    @RelaxedMockK
    lateinit var disasterDao: DisasterDao

    @RelaxedMockK
    lateinit var settingPreferences: SettingPreferences

    @RelaxedMockK
    lateinit var util: Util

    private lateinit var disasterRepositoryImpl: DisasterRepositoryImpl

    @Before
    fun initMockK() {
        disasterAPI = mockk()
        disasterDao = mockk()
        settingPreferences = mockk()
        util = mockk()
        disasterRepositoryImpl = DisasterRepositoryImpl(disasterAPI, disasterDao, settingPreferences)
    }

    @Test
    fun check_getApiDisasterData() = runTest {
        val disasterDataSlot = slot<List<DisasterEntity>>()

        val expectedResult = DisasterResponse(
            statusCode = 200,
            result = Result(
                objects = Objects(
                    output = Output(
                        geometries = listOf(
                            GeometriesItem(
                                coordinates = listOf(
                                    120.0, 119.0
                                ),
                                type = "Point",
                                properties = Properties(
                                    pkey = "1",
                                    imageUrl = "www.google.com",
                                    disasterType = "flood",
                                    createdAt = "2016-12-09T21:37:00.000Z",
                                    tags = Tags(
                                        instanceRegionCode = "ID-JK"
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        coEvery { disasterAPI.getAllDisasterData() } answers {expectedResult}
        coEvery { disasterDao.deleteAllDisaster() } just Runs
        coEvery { disasterDao.insertDisaster(capture(disasterDataSlot)) } just Runs

        disasterRepositoryImpl.getApiDisasterData()

        coVerify(exactly = 1) {
            disasterAPI.getAllDisasterData()
            disasterDao.deleteAllDisaster()
            disasterDao.insertDisaster(capture(disasterDataSlot))
        }
    }

    @Test
    fun check_getPeriodicDisasterData() = runBlocking {
        val startDateMock = "2023-08-10T00:00:00+0700"
        val endDateMock = "2023-08-10T01:00:00+0700"
        val dateSlot = slot<String>()
        val disasterDataSlot = slot<List<DisasterEntity>>()

        val expectedResult = DisasterResponse(
            statusCode = 200,
            result = Result(
                objects = Objects(
                    output = Output(
                        geometries = listOf(
                            GeometriesItem(
                                coordinates = listOf(
                                    120.0, 119.0
                                ),
                                type = "Point",
                                properties = Properties(
                                    pkey = "1",
                                    imageUrl = "www.google.com",
                                    disasterType = "flood",
                                    createdAt = "2016-12-09T21:37:00.000Z",
                                    tags = Tags(
                                        instanceRegionCode = "ID-JK"
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        every { util.getDateApiFormat(capture(dateSlot)) } answers {startDateMock}
        coEvery { disasterAPI.getDisasterDataByPeriod(capture(dateSlot), capture(dateSlot)) } answers {expectedResult}
        coEvery { disasterDao.deleteAllDisaster() } just Runs
        coEvery { disasterDao.insertDisaster(capture(disasterDataSlot)) } just Runs

        disasterRepositoryImpl.getPeriodicDisasterData(startDateMock, endDateMock)
        disasterAPI.getDisasterDataByPeriod(startDateMock, endDateMock)
        disasterDao.deleteAllDisaster()

        coVerify(exactly = 1) {
            disasterAPI.getDisasterDataByPeriod(startDateMock, endDateMock)
            disasterDao.deleteAllDisaster()
        }

        val actualResponse = disasterAPI.getDisasterDataByPeriod(startDateMock, endDateMock)
        assertEquals(expectedResult, actualResponse)
        assertEquals(startDateMock, util.getDateApiFormat(startDateMock))
    }

    @Test
    fun check_getAllDisasterData() {

        val expectedResult = listOf(
                DisasterEntity(
                    pKey = "1",
                    disasterType = "flood",
                    disasterImageUrl = "www.google.com",
                    disasterLoc = "ID-JK",
                    latitude = 119.0,
                    longitude = 120.0,
                    disasterDate = "12-02-2023"
                )
            )

        every {
            disasterDao.getAllDisaster().value
        } answers { expectedResult }

        disasterRepositoryImpl.getAllDisasterData()

        verify(exactly = 1) { disasterDao.getAllDisaster() }
    }

    @Test
    fun check_getDataByLocation() {
        val locFilterMock = "ID-JK"
        val locSlot = slot<String>()

        val expectedResult = listOf(
            DisasterEntity(
                pKey = "1",
                disasterType = "flood",
                disasterImageUrl = "www.google.com",
                disasterLoc = "ID-JK",
                latitude = 119.0,
                longitude = 120.0,
                disasterDate = "12-02-2023"
            )
        )

        every {
            disasterDao.getDataByLocation(capture(locSlot)).value
        } answers { expectedResult }

        disasterRepositoryImpl.getDataByLocation(locFilterMock)

        verify(exactly = 1) { disasterDao.getDataByLocation(locFilterMock) }
    }

    @Test
    fun check_getDataByDisaster() {
        val disasterFilterMock = "flood"
        val disasterSlot = slot<String>()

        val expectedResult = listOf(
            DisasterEntity(
                pKey = "1",
                disasterType = "flood",
                disasterImageUrl = "www.google.com",
                disasterLoc = "ID-JK",
                latitude = 119.0,
                longitude = 120.0,
                disasterDate = "12-02-2023"
            )
        )

        every {
            disasterDao.getDataByDisaster(capture(disasterSlot)).value
        } answers { expectedResult }

        disasterRepositoryImpl.getDataByDisaster(disasterFilterMock)

        verify(exactly = 1) { disasterDao.getDataByDisaster(disasterFilterMock) }
    }

    @Test
    fun check_getDataByLocationAndDisaster() {
        val locFilterMock = "ID-JK"
        val disasterFilterMock = "flood"
        val disasterSlot = slot<String>()

        val expectedResult = listOf(
            DisasterEntity(
                pKey = "1",
                disasterType = "flood",
                disasterImageUrl = "www.google.com",
                disasterLoc = "ID-JK",
                latitude = 119.0,
                longitude = 120.0,
                disasterDate = "12-02-2023"
            )
        )

        every {
            disasterDao.getDataByLocationAndDisaster(capture(disasterSlot),capture(disasterSlot)).value
        } answers { expectedResult }

        disasterRepositoryImpl.getDataByLocationAndDisaster(locFilterMock, disasterFilterMock)

        verify(exactly = 1) { disasterDao.getDataByLocationAndDisaster(locFilterMock, disasterFilterMock) }
    }

    @Test
    fun check_getFloodGaugesData() = runTest {
        val expectedResult = FloodGaugesResponse(
            statusCode = 200,
            floodGaugesResult = FloodGaugesResult(
                objects = FloodGaugesObjects(
                    output = FloodGaugesOutput(
                        geometries = listOf(
                            FloodGaugesGeometriesItem(
                                floodGaugesProperties = FloodGaugesProperties(
                                    gaugenameid = "Bendungan X",
                                    observations = listOf(
                                        ObservationsItem(
                                            f1 = "2016-12-09T04:00:00+00:00",
                                            f2 = 30,
                                            f3 = 4,
                                            f4 = "SIAGA IV"
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        coEvery { disasterAPI.getFloodGaugesData() } answers {expectedResult}

        disasterRepositoryImpl.getFloodGaugesData()

        coVerify(exactly = 1) {
            disasterAPI.getFloodGaugesData()
        }
    }

    @Test
    fun check_getThemeSettings() {
        val themeSettingsMock = true

        every {
            settingPreferences.getThemeSetting()
        } returns flow { themeSettingsMock }

        disasterRepositoryImpl.getThemeSettings()

        verify(exactly = 1) { settingPreferences.getThemeSetting() }
    }

    @Test
    fun check_saveThemeSettings() = runTest() {
        val themeSettingsMock = true
        val themeSettingsSlot = slot<Boolean>()

        coEvery {
            settingPreferences.saveThemeSetting(capture(themeSettingsSlot))
        } just Runs

        disasterRepositoryImpl.saveThemeSetting(themeSettingsMock)

        coVerify(exactly = 1) { settingPreferences.saveThemeSetting(themeSettingsMock) }
    }

    @Test
    fun check_getNotificationSettings() {
        val notificationSettingsMock = true

        every {
            settingPreferences.getNotificationSetting()
        } returns flow { notificationSettingsMock }

        disasterRepositoryImpl.getNotificationSettings()

        verify(exactly = 1) { settingPreferences.getNotificationSetting() }
    }

    @Test
    fun check_saveNotificationSettings() = runTest() {
        val notificationSettingsMock = true
        val notificationSettingsSlot = slot<Boolean>()

        coEvery {
            settingPreferences.saveNotificationSetting(capture(notificationSettingsSlot))
        } just Runs

        disasterRepositoryImpl.saveNotificationSetting(notificationSettingsMock)

        coVerify(exactly = 1) { settingPreferences.saveNotificationSetting(notificationSettingsMock) }
    }
}