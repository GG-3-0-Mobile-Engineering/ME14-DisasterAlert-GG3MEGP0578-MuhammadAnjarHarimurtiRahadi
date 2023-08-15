package com.example.disasteralert.ui.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.disasteralert.MainDispatcherRule
import com.example.disasteralert.domain.usecase.DisasterUseCase
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class SettingsViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var disasterUseCaseMock: DisasterUseCase

    private lateinit var settingsViewModel: SettingsViewModel

    @Before
    fun initMockK() {
        disasterUseCaseMock = mockk()
        settingsViewModel = SettingsViewModel(disasterUseCaseMock)
    }

    @Test
    fun check_getThemeSettings() {
        val themeSettingsMock = true

        every {
            disasterUseCaseMock.getThemeSettings().value
        } answers { themeSettingsMock }

        settingsViewModel.getThemeSettings()

        verify(exactly = 1) { disasterUseCaseMock.getThemeSettings() }
    }

    @Test
    fun check_saveThemeSettings() {
        val themeSettingsMock = true
        val themeSettingsSlot = slot<Boolean>()

        coEvery {
            disasterUseCaseMock.saveThemeSetting(capture(themeSettingsSlot))
        } just Runs

        settingsViewModel.saveThemeSetting(themeSettingsMock)

        coVerify(exactly = 1) { disasterUseCaseMock.saveThemeSetting(themeSettingsMock) }
    }

    @Test
    fun check_getNotificationSettings() {
        val notificationSettingsMock = true

        every {
            disasterUseCaseMock.getNotificationSettings().value
        } answers { notificationSettingsMock }

        settingsViewModel.getNotificationSettings()

        verify(exactly = 1) { disasterUseCaseMock.getNotificationSettings() }
    }

    @Test
    fun check_saveNotificationSettings() {
        val notificationSettingsMock = true
        val notificationSettingsSlot = slot<Boolean>()

        coEvery {
            disasterUseCaseMock.saveNotificationSetting(capture(notificationSettingsSlot))
        } just Runs

        settingsViewModel.saveNotificationSetting(notificationSettingsMock)

        coVerify(exactly = 1) { disasterUseCaseMock.saveNotificationSetting(notificationSettingsMock) }
    }
}