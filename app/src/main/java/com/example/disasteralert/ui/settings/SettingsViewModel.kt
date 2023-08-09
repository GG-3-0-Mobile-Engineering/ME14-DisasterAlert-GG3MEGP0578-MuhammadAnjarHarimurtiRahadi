package com.example.disasteralert.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.disasteralert.domain.repository.DisasterRepository
import com.example.disasteralert.domain.usecase.DisasterUseCase
import com.example.disasteralert.helper.SettingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val disasterUseCase: DisasterUseCase
): ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return disasterUseCase.getThemeSettings()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            disasterUseCase.saveThemeSetting(isDarkModeActive)
        }
    }

    fun getNotificationSettings(): LiveData<Boolean> {
        return disasterUseCase.getNotificationSettings()
    }

    fun saveNotificationSetting(isNotificationActive: Boolean) {
        viewModelScope.launch {
            disasterUseCase.saveNotificationSetting(isNotificationActive)
        }
    }
}