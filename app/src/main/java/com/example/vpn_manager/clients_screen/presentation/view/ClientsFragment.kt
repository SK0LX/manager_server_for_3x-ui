package com.example.vpn_manager.clients_screen.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vpn_manager.databinding.FragmentClientsBinding
import com.example.vpn_manager.databinding.ItemClientBinding
import com.example.vpn_manager.clients_screen.di.ClientsViewModelFactory
import com.example.vpn_manager.clients_screen.presentation.viewmodel.ClientsViewModel
import com.example.vpn_manager.clients_screen.presentation.viewmodel.ClientsEffect
import com.example.vpn_manager.clients_screen.presentation.viewmodel.ClientsIntent
import kotlinx.coroutines.launch
import android.graphics.Color
import android.widget.Toast
import androidx.core.os.bundleOf
import com.example.vpn_manager.R
import com.example.vpn_manager.app.App
import com.example.vpn_manager.clients_screen.domain.model.ClientsEntity
import com.example.vpn_manager.main_screen.di.MainViewModelFactory
import javax.inject.Inject

class ClientsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ClientsViewModelFactory
    private var _binding: FragmentClientsBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: ClientsViewModel by viewModels { viewModelFactory }
    companion object {
        const val CLIENTS_REQUEST_KEY = "clients_request_key"
        const val CLIENTS_SERVER_ID_KEY = "server_id"
        const val CLIENTS_INBOUND_ID_KEY = "Inbound_id"
    }

    override fun onAttach(context: Context) {
        (requireActivity().application as App).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClientsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragmentResultListener()

        binding.addButtonClient.setOnClickListener {
            viewModel.onAction(ClientsIntent.OpenAndEditClientModal(null))
        }

        binding.refreshButtonClient.setOnClickListener {
            viewModel.onAction(ClientsIntent.LoadClients)
        }

        binding.backButtonToInbound.setOnClickListener {
            viewModel.onAction(ClientsIntent.NavigateToBack)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.effects
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { effect ->
                    handleEffects(effect)
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { state ->
                    updateClientsList(state.clients)
                }
        }
        viewModel.onAction(ClientsIntent.LoadClients)
    }

    private fun setupFragmentResultListener() {
        parentFragmentManager.setFragmentResultListener(
            CLIENTS_REQUEST_KEY,
            viewLifecycleOwner
        ) { requestKey, bundle ->
            val serverId = bundle.getString(CLIENTS_SERVER_ID_KEY)
            val inboundId = bundle.getString(CLIENTS_INBOUND_ID_KEY)
            if (serverId != null && inboundId != null) {
                viewModel.onAction(ClientsIntent.SetServerAndInboundId(serverId, inboundId))
                viewModel.onAction(ClientsIntent.LoadClients)
            } else {
                handleEffects(ClientsEffect.ShowError("Не удалось получить данные сервера"))
            }
        }
    }

    private fun handleEffects(effect: ClientsEffect) {
        when (effect) {
            is ClientsEffect.NavigateToBack -> {
                findNavController().navigateUp()
            }

            is ClientsEffect.ShowError -> {
                Toast.makeText(requireContext(), effect.message, Toast.LENGTH_SHORT).show()
            }

            is ClientsEffect.ShowSuccess -> {
            }

            is ClientsEffect.NavigateToModal -> {
                navigateToAddEditModalClient(
                    effect.clientUUID,
                    effect.inboundId,
                    effect.serverId
                )
            }

            is ClientsEffect.OpenAndEditClientModal -> {
                openAddEditClientModal(effect.clientId)
            }
        }
    }

    private fun updateClientsList(clients: List<ClientsEntity>) {
        binding.clientsContainer.removeAllViews()

        clients.forEach { client ->
            val itemBinding = ItemClientBinding.inflate(
                layoutInflater,
                binding.clientsContainer,
                false
            )

            itemBinding.titleText.text = client.isOnline

            when {
                client.isOnline.contains("Online", ignoreCase = true) ->
                    itemBinding.titleText.setTextColor(Color.parseColor("#00FF00"))
                client.isOnline.contains("Офлайн", ignoreCase = true) ->
                    itemBinding.titleText.setTextColor(Color.RED)
                else ->
                    itemBinding.titleText.setTextColor(Color.GRAY)
            }

            itemBinding.subtitleText.text = client.name

            if (client.traffic.size >= 2) {
                itemBinding.trafficLeft.text = client.traffic[0]
                itemBinding.trafficRight.text = client.traffic[1]
            } else {
                itemBinding.trafficLeft.text = "0 B"
                itemBinding.trafficRight.text = "0 B"
            }

            itemBinding.expiryDate.text = "До: ${client.dateEnd}"

            itemBinding.root.setOnClickListener {
                viewModel.onAction(ClientsIntent.NavigateToModalClient(client))
            }

            itemBinding.root.setOnLongClickListener {
                viewModel.onAction(ClientsIntent.SetOnLongClickListener(client))
                true

            }

            binding.clientsContainer.addView(itemBinding.root)
        }
    }

    private fun openAddEditClientModal(clientId: String?) {
        val resultBundle = bundleOf(
            "client_id" to clientId
        )
        parentFragmentManager.setFragmentResult(
            "client_request_key",
            resultBundle
        )
        viewModel.onAction(ClientsIntent.NavigateToModal)
    }

    private fun navigateToAddEditModalClient(
        clientUUID: String?,
        inboundId: String?,
        serverId: String?
    ) {
        if (serverId == null || inboundId == null) {
            Toast.makeText(requireContext(), "Не загрузился сервер или инбаунд", Toast.LENGTH_SHORT).show()
            return
        }
        val resultBundle = bundleOf(
            "client_id" to clientUUID,
            "Inbound_id" to inboundId,
            "server_id" to serverId
        )

        parentFragmentManager.setFragmentResult(
            "client_request_key",
            resultBundle
        )

        findNavController().navigate(R.id.addEditClientFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}