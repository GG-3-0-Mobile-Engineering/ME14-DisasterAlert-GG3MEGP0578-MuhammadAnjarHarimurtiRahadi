package com.example.disasteralert.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.disasteralert.MainDispatcherRule
import com.example.disasteralert.data.Results
import com.example.disasteralert.data.local.entity.DisasterEntity
import com.example.disasteralert.domain.usecase.DisasterUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any

class HomeViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var disasterUseCaseMock: DisasterUseCase

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun initMockK() {
        disasterUseCaseMock = mockk()
        homeViewModel = HomeViewModel(disasterUseCaseMock)
    }

    @Test
    fun check_getApiDisasterData() {
        coEvery { disasterUseCaseMock.getApiDisasterData() } just Runs

        homeViewModel.getApiDisasterData()

        coVerify(exactly = 1) { disasterUseCaseMock.getApiDisasterData() }
    }

    @Test
    fun check_getPeriodicDisasterData() {
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
            disasterUseCaseMock.getPeriodicDisasterData(capture(dateSlot), capture(dateSlot)).value
        } answers { expectedResult }

        homeViewModel.getPeriodicDisasterData(startDateMock, endDateMock)

        verify(exactly = 1) { disasterUseCaseMock.getPeriodicDisasterData(startDateMock, endDateMock) }
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
            disasterUseCaseMock.getAllDisasterData(capture(filterSlot), capture(filterSlot)).value
        } answers { expectedResult }

        homeViewModel.getAllDisasterData(locFilterMock, disasterFilterMock)

        verify(exactly = 1) { disasterUseCaseMock.getAllDisasterData(locFilterMock, disasterFilterMock) }
    }

    @Test
    fun check_getAllDisasterData_LocEmpty() {
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
            disasterUseCaseMock.getAllDisasterData(capture(filterSlot), capture(filterSlot)).value
        } answers { expectedResult }

        homeViewModel.getAllDisasterData(locFilterMock, disasterFilterMock)

        verify(exactly = 1) { disasterUseCaseMock.getAllDisasterData(locFilterMock, disasterFilterMock) }
    }

    @Test
    fun check_getThemeSettings() {
        val themeSettingsMock = true

        every {
            disasterUseCaseMock.getThemeSettings().value
        } answers { themeSettingsMock }

        homeViewModel.getThemeSettings()

        verify(exactly = 1) { disasterUseCaseMock.getThemeSettings() }
    }

    @Test
    fun check_isFilter_changeValue() {
        val isFilterMock = true

        homeViewModel.isFilter = isFilterMock

        val actualValue = homeViewModel.isFilter

        assert(actualValue)
    }
}