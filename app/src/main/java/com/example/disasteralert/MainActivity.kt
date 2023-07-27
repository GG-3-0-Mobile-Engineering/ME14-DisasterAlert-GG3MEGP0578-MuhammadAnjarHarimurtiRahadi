package com.example.disasteralert

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.disasteralert.databinding.ActivityMainBinding
import com.example.disasteralert.helper.SettingPreferences
import com.example.disasteralert.ui.settings.SettingsViewModel
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.soloader.SoLoader

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(applicationContext, pref)
        val viewModel: SettingsViewModel by viewModels { factory }

        checkTheme(viewModel)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        setupActionBarWithNavController(navController)

        if (BuildConfig.DEBUG)
            initFlipper()
    }

    private fun checkTheme(viewModel: SettingsViewModel) {
        viewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun initFlipper() {
        SoLoader.init(this, false)

        val client = AndroidFlipperClient.getInstance(this.applicationContext).apply {
            addPlugin(
                InspectorFlipperPlugin(
                    applicationContext,
                    DescriptorMapping.withDefaults()
                )
            )
            addPlugin(CrashReporterPlugin.getInstance())
            addPlugin(networkFlipperPlugin)
        }
        client.start()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    companion object {
        val networkFlipperPlugin = NetworkFlipperPlugin()
    }
}