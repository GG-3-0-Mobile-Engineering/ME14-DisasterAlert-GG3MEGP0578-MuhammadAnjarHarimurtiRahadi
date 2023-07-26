package com.example.disasteralert

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.disasteralert.data.DisasterRepository
import com.example.disasteralert.di.Injection
import com.example.disasteralert.helper.SettingPreferences
import com.example.disasteralert.ui.home.HomeViewModel
import com.example.disasteralert.ui.settings.SettingsViewModel

class ViewModelFactory private constructor(
    private val disasterRepository: DisasterRepository, private val pref: SettingPreferences
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        HomeViewModel::class.java -> HomeViewModel(disasterRepository, pref) as T
        SettingsViewModel::class.java -> SettingsViewModel(pref) as T
        else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context, pref: SettingPreferences): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context), pref)
            }.also { instance = it }
    }
}