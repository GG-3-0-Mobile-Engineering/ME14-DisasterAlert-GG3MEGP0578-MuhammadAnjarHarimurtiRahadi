package com.example.disasteralert.helper

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.disasteralert.MainDispatcherRule
import com.example.disasteralert.ui.settings.SettingsViewModel
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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class SettingPreferencesTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    lateinit var dataStore: DataStore<Preferences>

    private lateinit var settingPreferences: SettingPreferences
    @Before
    fun initMockK() {
        dataStore = mockk()
        settingPreferences = SettingPreferences(dataStore)
    }

    @Test
    fun check_getThemeSetting() {
        val themeSettingsMock = true

        every {
            settingPreferences.getThemeSetting()
        } returns flow { themeSettingsMock }

        settingPreferences.getThemeSetting()

        verify(exactly = 1) { settingPreferences.getThemeSetting() }
    }

    @Test
    fun check_getNotificationSetting() {
        val notificationSettingsMock = true

        every {
            settingPreferences.getNotificationSetting()
        } returns flow { notificationSettingsMock }

        settingPreferences.getNotificationSetting()

        verify(exactly = 1) { settingPreferences.getNotificationSetting() }
    }
}