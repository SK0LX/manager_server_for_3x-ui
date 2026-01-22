package com.example.vpn_manager.modal_client.presentation.view

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
import com.example.vpn_manager.databinding.FragmentAddEditClientBinding
import com.example.vpn_manager.modal_client.di.AddEditClientViewModelFactory
import com.example.vpn_manager.modal_client.presentation.model.AddEditClientState
import com.example.vpn_manager.modal_client.presentation.viewmodel.AddEditClientEffect
import com.example.vpn_manager.modal_client.presentation.viewmodel.AddEditClientIntent
import com.example.vpn_manager.modal_client.presentation.viewmodel.AddEditClientViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddEditClientFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: AddEditClientViewModelFactory

    private var _binding: FragmentAddEditClientBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel: AddEditClientViewModel by viewModels { viewModelFactory }

    companion object {
        private const val REQUEST_KEY = "client_request_key"
        private const val CLIENT_UUID_KEY = "client_id"
        private const val CLIENTS_SERVER_ID_KEY = "server_id"
        private const val CLIENTS_INBOUND_ID_KEY = "Inbound_id"
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
        _binding = FragmentAddEditClientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragmentResultListener()

        binding.btnCancel.setOnClickListener {
            viewModel.onAction(AddEditClientIntent.NavigateToClients)
        }

        binding.btnSave.setOnClickListener {
            saveClient()
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

    private fun handleEffect(effect: AddEditClientEffect) {
        when (effect) {
            is AddEditClientEffect.NavigateToClients -> {
                findNavController().navigateUp()
            }

            is AddEditClientEffect.ShowMessage -> {
                Toast.makeText(requireContext(), "Message: ${effect.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun renderState(state: AddEditClientState) {
        binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE

        if (state.isEditMode) {
            binding.titleTextView.text = "Редактировать клиента"
            binding.tvId.text = "ID: ${state.id}"
            binding.tvId.visibility = View.VISIBLE
        } else {
            binding.titleTextView.text = "Создать клиента"
            binding.tvId.visibility = View.GONE
        }

        if (state.email.isNotEmpty() && binding.etEmail.text.toString().isEmpty()) {
            binding.etEmail.setText(state.email)
        }
        binding.switchEnabled.isChecked = state.enable
        if (state.totalGB.isNotEmpty() && binding.etTotalGB.text.toString().isEmpty()) {
            binding.etTotalGB.setText(state.totalGB)
        }
        if (state.expiryTime.isNotEmpty() && binding.etExpiryTime.text.toString().isEmpty()) {
            binding.etExpiryTime.setText(state.expiryTime)
        }
    }

    private fun setupFragmentResultListener() {
        parentFragmentManager.setFragmentResultListener(
            REQUEST_KEY,
            viewLifecycleOwner
        ) { requestKey, bundle ->
            val serverId = bundle.getString(CLIENTS_SERVER_ID_KEY)
            val inboundId = bundle.getString(CLIENTS_INBOUND_ID_KEY)
            val clientUUID = bundle.getString(CLIENT_UUID_KEY)

            viewModel.onAction(
                AddEditClientIntent.SetClientData(
                    serverId = serverId,
                    inboundId = inboundId,
                    clientUUID = clientUUID
                )
            )
        }
    }

    private fun saveClient() {
        val email = binding.etEmail.text?.toString() ?: ""
        val enable = binding.switchEnabled.isChecked
        val totalGB = binding.etTotalGB.text?.toString()?.toLongOrNull() ?: 0L
        val expiryTime = binding.etExpiryTime.text?.toString()?.toLongOrNull() ?: 0L

        val state = viewModel.state.value
        if (email.isEmpty()) {
            return
        }

        if (state.isEditMode) {
            viewModel.onAction(
                AddEditClientIntent.UpdateClient(
                    email = email,
                    enable = enable,
                    totalGB = totalGB,
                    expiryTime = expiryTime,
                )
            )
        } else {
            val inboundId = state.inboundId ?: return
            viewModel.onAction(
                AddEditClientIntent.AddClient(
                    inboundId = inboundId,
                    email = email,
                    enable = enable,
                    totalGB = totalGB,
                    expiryTime = expiryTime,
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}