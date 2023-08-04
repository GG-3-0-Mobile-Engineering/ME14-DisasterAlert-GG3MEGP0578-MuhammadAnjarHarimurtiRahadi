package com.example.disasteralert.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.disasteralert.domain.repository.DisasterRepository
import com.example.disasteralert.helper.SettingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val pref: SettingPreferences,
    private val disasterRepository: DisasterRepository
): ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun getNotificationSettings(): LiveData<Boolean> {
        return pref.getNotificationSetting().asLiveData()
    }

    fun saveNotificationSetting(isNotificationActive: Boolean) {
        viewModelScope.launch {
            pref.saveNotificationSetting(isNotificationActive)
        }
    }

//    fun getFloodGaugesData() = disasterRepository.getFloodGaugesData()
}