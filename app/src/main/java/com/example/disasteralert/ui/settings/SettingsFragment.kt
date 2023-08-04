package com.example.disasteralert.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import com.example.disasteralert.databinding.FragmentSettingsBinding
import com.example.disasteralert.helper.Util.cancelWorkManager
import com.example.disasteralert.helper.Util.setPeriodicWorkManager
import com.example.disasteralert.helper.Util.setWorkManager
import dagger.hilt.android.AndroidEntryPoint

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
            setPeriodicWorkManager()
        } else {
            cancelWorkManager()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}