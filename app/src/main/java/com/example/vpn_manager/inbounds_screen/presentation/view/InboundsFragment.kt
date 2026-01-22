package com.example.vpn_manager.inbounds_screen.presentation.view

import android.annotation.SuppressLint
import android.content.Context
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
import com.example.vpn_manager.databinding.FragmentInboundsBinding
import com.example.vpn_manager.databinding.ItemInboundsBinding
import com.example.vpn_manager.inbounds_screen.di.InboundsViewModelFactory
import com.example.vpn_manager.inbounds_screen.domain.model.InboundEntity
import com.example.vpn_manager.inbounds_screen.presentation.viewmodel.InboundsEffect
import com.example.vpn_manager.inbounds_screen.presentation.viewmodel.InboundsIntent
import com.example.vpn_manager.inbounds_screen.presentation.viewmodel.InboundsViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class InboundsFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: InboundsViewModelFactory
    private val viewModel: InboundsViewModel by viewModels{ viewModelFactory }
    private var _binding: FragmentInboundsBinding? = null
    private val binding get() = requireNotNull(_binding)

    companion object {
        const val REQUEST_KEY = "server_request_key"
        private const val SERVER_ID_KEY = "serverId"
    }

    override fun onAttach(context: Context) {
        (requireActivity().application as App).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInboundsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragmentResultListener()
        binding.addButton.setOnClickListener {
            viewModel.onAction(InboundsIntent.OnModalGo())
        }

        binding.backButton.setOnClickListener {
            viewModel.onAction(InboundsIntent.NavigateToBack)
        }

        binding.refreshButton.setOnClickListener {
            viewModel.onAction(InboundsIntent.LoadInbounds)
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
                    updateInboundsList(state.inbounds)
                }
        }
    }

    private fun handleEffect(effect: InboundsEffect){
        when (effect) {
            is InboundsEffect.NavigateToBack -> {
                findNavController().navigateUp()
            }
            is InboundsEffect.ShowError -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                println(effect.message)
            }
            is InboundsEffect.NavigateToModalInbounds ->{
                openAddEditInboundModal(effect.inboundId, viewModel.state.value.serverId!!)
            }
            is InboundsEffect.AddEditInboundModal ->{
                openAddEditInboundModal(effect.inboundId, effect.serverId)
            }

            is InboundsEffect.NavigateToClientsFragment ->{
                navigateToClientsFragment(effect.inboundId, effect.serverId)
            }
        }
    }

    private fun updateInboundsList(inbounds: List<InboundEntity>) {
        binding.inboundsContainer.removeAllViews()

        inbounds.forEach { inbound ->
            val itemBinding = ItemInboundsBinding.inflate(
                layoutInflater,
                binding.inboundsContainer,
                false
            )
            itemBinding.inboundName.text = inbound.name
            itemBinding.inboundPort.text = inbound.port
            itemBinding.inboundProtocol.text = inbound.protocols
            itemBinding.clientCount.text = inbound.clients
            itemBinding.trafficInfo.text = inbound.traffic
            itemBinding.expiryDate.text = inbound.dataTime

            itemBinding.editButton.setOnClickListener {
                viewModel.onAction(InboundsIntent.OnEditClicked(inbound.id, viewModel.state.value.serverId))
            }

            itemBinding.deleteButton.setOnClickListener {
                viewModel.onAction(InboundsIntent.DeleteInbound(inbound.id))
            }

            itemBinding.goInClients.setOnClickListener {
                viewModel.onAction(InboundsIntent.NavigateToClientsFragment(inbound.id, viewModel.state.value.serverId))
            }

            binding.inboundsContainer.addView(itemBinding.root)
        }
    }

    private fun openAddEditInboundModal(inboundId: String?, serverId: String) {
        val resultBundle = bundleOf(
            "Inbound_id" to inboundId,
            "server_id" to serverId
        )
        parentFragmentManager.setFragmentResult(
            "request_key",
            resultBundle
        )
        findNavController().navigate(R.id.addEditInboundFragment)
    }

    private fun navigateToClientsFragment(inboundId: String?, serverId: String){
        val resultBundle = bundleOf(
            "Inbound_id" to inboundId,
            "server_id" to serverId
        )
        parentFragmentManager.setFragmentResult(
            "clients_request_key",
            resultBundle
        )
        findNavController().navigate(R.id.clientsFragment)
    }

    private fun setupFragmentResultListener() {
        parentFragmentManager.setFragmentResultListener(
            REQUEST_KEY,
            viewLifecycleOwner
        ) { requestKey, bundle ->
            val serverId = bundle.getString(SERVER_ID_KEY)
            viewModel.onAction(InboundsIntent.SetServerId(serverId))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}