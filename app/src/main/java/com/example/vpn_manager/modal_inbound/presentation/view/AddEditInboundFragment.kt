package com.example.vpn_manager.modal_inbound.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vpn_manager.app.App
import com.example.vpn_manager.databinding.FragmentAddEditInboundBinding
import com.example.vpn_manager.modal_inbound.di.AddEditInboundViewModelFactory
import com.example.vpn_manager.modal_inbound.presentation.model.AddEditInboundState
import com.example.vpn_manager.modal_inbound.presentation.viewmodel.AddEditInboundEffect
import com.example.vpn_manager.modal_inbound.presentation.viewmodel.AddEditInboundIntent
import com.example.vpn_manager.modal_inbound.presentation.viewmodel.AddEditInboundViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


class AddEditInboundFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: AddEditInboundViewModelFactory

    private var _binding: FragmentAddEditInboundBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel: AddEditInboundViewModel by viewModels { viewModelFactory }

    companion object {
        private const val REQUEST_KEY = "request_key"
        private const val INBOUND_ID_KEY = "Inbound_id"
        private const val SERVER_ID_KEY = "server_id"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditInboundBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        (requireActivity().application as App).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragmentResultListener()
        binding.btnCancel.setOnClickListener {
            viewModel.onAction(AddEditInboundIntent.NavigateToInbounds)
        }

        binding.btnSave.setOnClickListener {
            saveInbound()
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

    }

    private fun handleEffect(effect: AddEditInboundEffect) {
        when(effect) {
            is AddEditInboundEffect.NavigateToInbounds -> {
                findNavController().navigateUp()
            }

            is AddEditInboundEffect.ShowMessage -> {
                Toast.makeText(requireContext(), effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun renderState(state: AddEditInboundState) {
        binding.etRemark.setText(state.remark)
        binding.etPort.setText(state.port)
        binding.tvProtocol.text = state.protocol
        binding.switchEnabled.isChecked = state.enable
        when(state){
            is AddEditInboundIntent.AddInbound -> {
                this.saveInbound()
            }
            is AddEditInboundIntent.UpdateInbound -> {
                this.saveInbound()
            }
            is AddEditInboundIntent.GetInboundId -> {
            }
        }
    }

    private fun setupFragmentResultListener() {
        parentFragmentManager.setFragmentResultListener(
            REQUEST_KEY,
            viewLifecycleOwner
        ) { requestKey, bundle ->
            val inboundIdString = bundle.getString(INBOUND_ID_KEY)
            val inboundId = inboundIdString?.toIntOrNull()

            val serverIdString = bundle.getString(SERVER_ID_KEY)
            viewModel.onAction(AddEditInboundIntent.SetInboundAndServerId(inboundId, serverIdString))
            if (inboundId != null) {
                binding.titleTextView.text = "Редактировать инбаунд"
                binding.tvId.text = "ID: $inboundId"
                binding.tvId.visibility = View.VISIBLE
            } else {
                binding.titleTextView.text = "Создать инбаунд"
                binding.tvId.visibility = View.GONE
            }
        }
    }

    private fun saveInbound() {
        val remark = binding.etRemark.text?.toString() ?: ""
        val enable = binding.switchEnabled.isChecked
        val port = binding.etPort.text?.toString() ?: ""
        val protocol = binding.tvProtocol.text?.toString() ?: "vless"

        val state = viewModel.state.value
        if (state.isEditMode) {
            viewModel.onAction(
                AddEditInboundIntent.UpdateInbound(
                    remark = remark,
                    enable = enable,
                    port = port,
                    protocol = protocol
                )
            )
        } else {
            viewModel.onAction(
                AddEditInboundIntent.AddInbound(
                    remark = remark,
                    enable = enable,
                    port = port,
                    protocol = protocol
                )
            )
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}