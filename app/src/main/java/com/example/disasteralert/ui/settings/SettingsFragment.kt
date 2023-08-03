package com.example.disasteralert.ui.settings

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.disasteralert.R
import com.example.disasteralert.data.Results
import com.example.disasteralert.databinding.FragmentHomeBinding
import com.example.disasteralert.databinding.FragmentSettingsBinding
import com.example.disasteralert.helper.SettingPreferences
import com.example.disasteralert.helper.Util
import com.example.disasteralert.helper.Util.cancelWorkManager
import com.example.disasteralert.helper.Util.setWorkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setWorkManager(requireActivity())

        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            binding.switchTheme.isChecked = isDarkModeActive
        }

        viewModel.getNotificationSettings().observe(viewLifecycleOwner) { isNotificationActive: Boolean ->
            binding.switchNotification.isChecked = isNotificationActive
            setNotificationAlert(isNotificationActive)
        }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }

        binding.switchNotification.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveNotificationSetting(isChecked)
        }
    }

    private fun setNotificationAlert(isNotificationActive: Boolean) {
        if (isNotificationActive) {
            getFloodGaugesData()
        } else {
            cancelWorkManager()
        }
    }

    private fun getFloodGaugesData() {
        viewModel.getFloodGaugesData().observe(viewLifecycleOwner) { floodGauges ->
            if (floodGauges != null) {
                when (floodGauges) {
                    is Results.Success -> {
                        val floodGaugesData =
                            floodGauges.data.floodGaugesResult.objects.output.geometries

                        if (floodGaugesData.isNotEmpty()) {
                            Util.setPeriodicWorkManager(floodGaugesData.last())
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}