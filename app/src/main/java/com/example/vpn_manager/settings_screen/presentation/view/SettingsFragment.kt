package com.example.vpn_manager.settings_screen.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vpn_manager.app.App
import com.example.vpn_manager.databinding.FragmentSettingsBinding
import com.example.vpn_manager.settings_screen.di.SettingsViewModelFactory
import com.example.vpn_manager.settings_screen.presentation.viewmodal.SettingsEffect
import com.example.vpn_manager.settings_screen.presentation.viewmodal.SettingsIntent
import com.example.vpn_manager.settings_screen.presentation.modal.SettingsState
import com.example.vpn_manager.settings_screen.presentation.viewmodal.SettingsViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: SettingsViewModelFactory
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: SettingsViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        (requireActivity().application as App).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            viewModel.onAction(SettingsIntent.NavigateBack)
        }

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onAction(SettingsIntent.ToggleTheme(isChecked))
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.effects
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { effect ->
                    handleEffect(effect)
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { state ->
                    renderState(state)
                }
        }

        viewModel.onAction(SettingsIntent.LoadSettings)
    }

    private fun renderState(state: SettingsState) {
        state.settingsData?.let { settings ->
            binding.themeSwitch.isChecked = settings.isDarkTheme
            binding.versionText.text = settings.appVersion
        }
    }

    private fun handleEffect(effect: SettingsEffect) {
        when (effect) {
            is SettingsEffect.ShowError -> {
                Toast.makeText(requireContext(), effect.message, Toast.LENGTH_SHORT).show()
            }

            SettingsEffect.ThemeUpdated -> {
                requireActivity().recreate()
            }

            SettingsEffect.NavigateBack -> {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}