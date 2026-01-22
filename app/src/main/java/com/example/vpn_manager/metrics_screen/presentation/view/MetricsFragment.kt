package com.example.vpn_manager.metrics_screen.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material3.MediumTopAppBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vpn_manager.app.App
import com.example.vpn_manager.databinding.FragmentMetricsBinding
import com.example.vpn_manager.inbounds_screen.presentation.viewmodel.InboundsIntent
import com.example.vpn_manager.metrics_screen.data.mapper.formatMemory
import com.example.vpn_manager.metrics_screen.data.mapper.formatSpeed
import com.example.vpn_manager.metrics_screen.data.mapper.formatTraffic
import com.example.vpn_manager.metrics_screen.di.MetricsViewModelFactory
import com.example.vpn_manager.metrics_screen.presentation.modal.MetricsState
import com.example.vpn_manager.metrics_screen.presentation.viewmodal.MetricsEffect
import com.example.vpn_manager.metrics_screen.presentation.viewmodal.MetricsIntent
import com.example.vpn_manager.metrics_screen.presentation.viewmodal.MetricsViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class MetricsFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: MetricsViewModelFactory

    private var _binding: FragmentMetricsBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel: MetricsViewModel by viewModels { viewModelFactory }

    companion object {
        const val REQUEST_KEY = "server_request_key"
        const val SERVER_ID_KEY = "serverId"
    }

    override fun onAttach(context: Context) {
        (requireActivity().application as App).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMetricsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFragmentResultListener()
        binding.backButton.setOnClickListener {
            viewModel.onAction(MetricsIntent.NavigateBack)
        }

        binding.refreshMetricsButton.setOnClickListener {
            viewModel.onAction(MetricsIntent.LoadServerMetrics(viewModel.state.value.selectedServerId))
        }

        binding.advancedMetricsButton.setOnClickListener {
            viewModel.onAction(MetricsIntent.AdvancedMetricsClick)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
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
    }

    private fun handleEffect(effect: MetricsEffect) {
        when (effect) {
            is MetricsEffect.ShowError -> {
                binding.tvError.text = effect.message
                binding.tvError.visibility = View.VISIBLE
            }

            is MetricsEffect.ShowMessage -> {
                Toast.makeText(requireContext(), effect.message, Toast.LENGTH_SHORT).show()
            }

            MetricsEffect.NavigateBack -> {
                findNavController().popBackStack()
            }
        }
    }

    private fun renderState(state: MetricsState) {
        binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        binding.tvError.visibility = if (state.error != null) View.VISIBLE else View.GONE

        state.metricsData?.let { metrics ->
            binding.ipv4Text.text = metrics.publicIpv4.ifEmpty { "Not available" }
            binding.ipv6Text.text = metrics.publicIpv6.ifEmpty { "Not available" }
            binding.xrayVersionText.text = metrics.xrayVersion.ifEmpty { "Unknown" }
            binding.cpuPercentText.text = "${metrics.cpuUsage}%"
            binding.cpuProgressBar.progress = metrics.cpuUsage
            binding.cpuCoresText.text = metrics.cpuCores.toString()
            binding.memoryPercentText.text = "${metrics.memoryUsage}%"
            binding.memoryProgressBar.progress = metrics.memoryUsage
            binding.memoryUsageText.text = "${formatMemory(metrics.memoryCurrent)} / ${formatMemory(metrics.memoryTotal)}"
            binding.diskPercentText.text = "${metrics.diskUsage}%"
            binding.diskProgressBar.progress = metrics.diskUsage
            binding.diskUsageText.text = "${formatMemory(metrics.diskCurrent)} / ${formatMemory(metrics.diskTotal)}"
            binding.totalConnectionsText.text = metrics.activeConnections.toString()
            binding.connectionsDetailText.text = "TCP: ${metrics.tcpConnections} | UDP: ${metrics.udpConnections}"
            binding.uptimeText.text = metrics.uptime
            binding.loadAverageText.text = "Load: ${metrics.loadAverage}"
            binding.uploadSpeedText.text = formatSpeed(metrics.uploadSpeed)
            binding.downloadSpeedText.text = formatSpeed(metrics.downloadSpeed)
            binding.uploadTrafficText.text = "Total: ${formatTraffic(metrics.uploadTraffic)}"
            binding.downloadTrafficText.text = "Total: ${formatTraffic(metrics.downloadTraffic)}"
            binding.totalTrafficText.text = formatTraffic(metrics.totalTraffic)
            binding.trafficRatioText.text = "↑${formatTraffic(metrics.uploadTraffic)} / ↓${formatTraffic(metrics.downloadTraffic)}"
        }
    }

    private fun setupFragmentResultListener() {
        parentFragmentManager.setFragmentResultListener(
            REQUEST_KEY,
            viewLifecycleOwner
        ) { requestKey, bundle ->
            val serverId = bundle.getString(SERVER_ID_KEY)
            viewModel.onAction(MetricsIntent.SetServerId(serverId))
            if (!serverId.isNullOrEmpty()) {
                viewModel.onAction(MetricsIntent.LoadServerMetrics(serverId))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}