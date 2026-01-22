package com.example.vpn_manager.main_screen.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vpn_manager.R
import com.example.vpn_manager.app.App
import com.example.vpn_manager.databinding.FragmentMainBinding
import com.example.vpn_manager.databinding.ItemServerBinding
import com.example.vpn_manager.main_screen.presentation.viewmodel.MainEffect
import com.example.vpn_manager.main_screen.presentation.viewmodel.MainIntent
import com.example.vpn_manager.main_screen.di.MainViewModelFactory
import com.example.vpn_manager.main_screen.domain.model.ServerEntity
import com.example.vpn_manager.main_screen.presentation.model.MainUiState
import com.example.vpn_manager.main_screen.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainFragment(): Fragment() {
    companion object {
        const val SERVER_REQUEST_KEY = "server_request_key"
        const val SERVER_ID_KEY = "serverId"
    }

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private val viewModel: MainViewModel by viewModels { viewModelFactory }
    private var _binding: FragmentMainBinding? = null
    private val binding get() = requireNotNull(_binding)


    override fun onAttach(context: android.content.Context) {
        (requireActivity().application as App).appComponent.inject(this)
        super.onAttach(context)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.setOnClickListener {
            viewModel.onAction(MainIntent.NavigateToAddEditServer(null))
        }

        binding.refreshButton.setOnClickListener {
            viewModel.onAction(MainIntent.RefreshServers)
        }

        binding.settingsButton.setOnClickListener {
            viewModel.onAction(MainIntent.NavigateToSettings)
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { state ->
                    renderState(state)
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.effects
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { effect ->
                    handleEffect(effect)
                }
        }
        viewModel.onAction(MainIntent.LoadServers)
    }

    private fun handleEffect(effect: MainEffect) {
        when (effect) {
            is MainEffect.ShowMessage -> {
                Toast.makeText(
                    requireContext(),
                    effect.message, Toast.LENGTH_SHORT
                )
                    .show()
            }
            is MainEffect.NavigateAddOrUpdateIServers -> {
                navigateToAddEditServer(effect.serverId)
            }

            is MainEffect.NavigateToSettings -> {
                findNavController().navigate(R.id.setingsFragment)
            }

            is MainEffect.NavigateToServerMetrics -> {
                navigateToMetricsServer(effect.serverId)
            }

            is MainEffect.NavigateToInbounds ->{
                navigateToInbounds(effect.serverId)
            }

            else -> {}
        }
    }

    private fun renderState(state: MainUiState) {
        updateServersList(state.servers)
    }

    private fun updateServersList(servers: Map<Int, ServerEntity>) {
        binding.serversContainer.removeAllViews()

        servers.values.forEach { server ->
            val serverView = ItemServerBinding.inflate(layoutInflater,
                binding.serversContainer,
                false)

            serverView.serverName.text = server.name
            serverView.editButton.setOnClickListener {
                viewModel.onAction(MainIntent.NavigateToAddEditServer(server.id))
            }

            serverView.deleteButton.setOnClickListener {
                viewModel.onAction(MainIntent.DeleteServer(server.id))
            }
            serverView.goInServer.setOnClickListener {
                viewModel.onAction(MainIntent.NavigateToInbounds(server.id))
            }
            serverView.metricsButton.setOnClickListener {
                viewModel.onAction(MainIntent.NavigateToServerMetrics(server.id))
            }

            binding.serversContainer.addView(serverView.root)
        }
    }

    private fun navigateToInbounds(serverId: String) {
        val resultBundle = bundleOf(SERVER_ID_KEY to serverId)
        parentFragmentManager.setFragmentResult(
            SERVER_REQUEST_KEY,
            resultBundle
        )
        findNavController().navigate(R.id.inboundsFragment)
    }

    private fun navigateToAddEditServer(serverId: String?) {
        try {
            val resultBundle = bundleOf(SERVER_ID_KEY to serverId)
            parentFragmentManager.setFragmentResult(
                SERVER_REQUEST_KEY,
                resultBundle
            )
            findNavController().navigate(R.id.addEditServerFragment)
        } catch (e: Exception) {
            handleEffect(MainEffect.ShowMessage("Navigation error: ${e.message}"))
        }
    }

    private fun navigateToMetricsServer(serverId: String?) {
        try {
            val resultBundle = bundleOf(SERVER_ID_KEY to serverId)
            parentFragmentManager.setFragmentResult(
                SERVER_REQUEST_KEY,
                resultBundle
            )
            findNavController().navigate(R.id.metricsFragment)
        } catch (e: Exception) {
            handleEffect(MainEffect.ShowMessage("Navigation error: ${e.message}"))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}