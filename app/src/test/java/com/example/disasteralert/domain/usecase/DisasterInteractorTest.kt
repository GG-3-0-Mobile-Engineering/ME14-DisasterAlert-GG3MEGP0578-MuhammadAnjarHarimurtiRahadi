package com.example.disasteralert.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.disasteralert.MainDispatcherRule
import com.example.disasteralert.data.Results
import com.example.disasteralert.data.local.entity.DisasterEntity
import com.example.disasteralert.domain.repository.DisasterRepository
import com.example.disasteralert.ui.home.HomeViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class DisasterInteractorTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var disasterRepositoryMock: DisasterRepository

    private lateinit var disasterInteractor: DisasterInteractor

    @Before
    fun initMockK() {
        disasterRepositoryMock = mockk()
        disasterInteractor = DisasterInteractor(disasterRepositoryMock)
    }

    @Test
    fun check_getApiDisasterData() = runTest {
        coEvery { disasterRepositoryMock.getApiDisasterData() } just Runs

        disasterInteractor.getApiDisasterData()

        coVerify(exactly = 1) { disasterRepositoryMock.getApiDisasterData() }
    }

    @Test
    fun check_getPeriodicDisasterData() = runTest {
        val startDateMock = "2023-08-10T00:00:00+0700"
        val endDateMock = "2023-08-10T01:00:00+0700"
        val dateSlot = slot<String>()

        val expectedResult = Results.Success(
            listOf(
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
        )

        every {
            disasterRepositoryMock.getPeriodicDisasterData(capture(dateSlot), capture(dateSlot)).value
        } answers { expectedResult }

        disasterInteractor.getPeriodicDisasterData()

        coVerify(exactly = 1) { disasterRepositoryMock.getPeriodicDisasterData() }
    }

    @Test
    fun check_getAllDisasterData() {
        val locFilterMock = "ID-JK"
        val disasterFilterMock = "flood"
        val filterSlot = slot<String>()

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
            disasterRepositoryMock.getDataByLocationAndDisaster(capture(filterSlot), capture(filterSlot)).value
        } answers { expectedResult }

        disasterInteractor.getAllDisasterData(locFilterMock, disasterFilterMock)

        verify(exactly = 1) { disasterRepositoryMock.getDataByLocationAndDisaster(locFilterMock, disasterFilterMock) }
    }

    @Test
    fun check_getAllDisasterData_locationEmpty() {
        val locFilterMock = ""
        val disasterFilterMock = "flood"
        val filterSlot = slot<String>()

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
            disasterRepositoryMock.getDataByDisaster(capture(filterSlot)).value
        } answers { expectedResult }

        disasterInteractor.getAllDisasterData(locFilterMock, disasterFilterMock)

        verify(exactly = 1) { disasterRepositoryMock.getDataByDisaster(disasterFilterMock) }
    }

    @Test
    fun check_getAllDisasterData_disasterEmpty() {
        val locFilterMock = "ID-JK"
        val disasterFilterMock = ""
        val filterSlot = slot<String>()

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
            disasterRepositoryMock.getDataByLocation(capture(filterSlot)).value
        } answers { expectedResult }

        disasterInteractor.getAllDisasterData(locFilterMock, disasterFilterMock)

        verify(exactly = 1) { disasterRepositoryMock.getDataByLocation(locFilterMock) }
    }

    @Test
    fun check_getAllDisasterData_disasterEmpty_locationEmpty() {
        val locFilterMock = ""
        val disasterFilterMock = ""

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
            disasterRepositoryMock.getAllDisasterData().value
        } answers { expectedResult }

        disasterInteractor.getAllDisasterData(locFilterMock, disasterFilterMock)

        verify(exactly = 1) { disasterRepositoryMock.getAllDisasterData() }
    }

    @Test
    fun check_getThemeSetting() {
        val themeSettingsMock = true

        every {
            disasterRepositoryMock.getThemeSettings().value
        } answers {themeSettingsMock}

        disasterInteractor.getThemeSettings()

        verify(exactly = 1) { disasterRepositoryMock.getThemeSettings() }
    }

    @Test
    fun check_saveThemeSettings() = runTest {
        val themeSettingsMock = true
        val themeSettingsSlot = slot<Boolean>()

        coEvery {
            disasterRepositoryMock.saveThemeSetting(capture(themeSettingsSlot))
        } just Runs

        disasterInteractor.saveThemeSetting(themeSettingsMock)

        coVerify(exactly = 1) { disasterRepositoryMock.saveThemeSetting(themeSettingsMock) }
    }

    @Test
    fun check_getNotificationSetting() {
        val themeSettingsMock = true

        every {
            disasterRepositoryMock.getNotificationSettings().value
        } answers {themeSettingsMock}

        disasterInteractor.getNotificationSettings()

        verify(exactly = 1) { disasterRepositoryMock.getNotificationSettings() }
    }

    @Test
    fun check_saveNotificationSettings() = runTest {
        val themeSettingsMock = true
        val themeSettingsSlot = slot<Boolean>()

        coEvery {
            disasterRepositoryMock.saveNotificationSetting(capture(themeSettingsSlot))
        } just Runs

        disasterInteractor.saveNotificationSetting(themeSettingsMock)

        coVerify(exactly = 1) { disasterRepositoryMock.saveNotificationSetting(themeSettingsMock) }
    }
}