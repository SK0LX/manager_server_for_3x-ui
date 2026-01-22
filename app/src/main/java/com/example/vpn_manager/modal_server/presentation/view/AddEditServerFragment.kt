package com.example.vpn_manager.modal_server.presentation.view

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
import com.example.vpn_manager.MainScreen.Model.main_screen.AddEditServerEffect
import com.example.vpn_manager.MainScreen.Model.main_screen.AddEditServerIntent
import com.example.vpn_manager.MainScreen.Model.main_screen.AddEditServerViewModel
import com.example.vpn_manager.app.App
import com.example.vpn_manager.databinding.ModalAddServerBinding
import com.example.vpn_manager.modal_server.di.AddEditServerViewModelFactory
import com.example.vpn_manager.modal_server.presentation.model.AddEditServerState
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddEditServerFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: AddEditServerViewModelFactory

    private var _binding: ModalAddServerBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel: AddEditServerViewModel by viewModels { viewModelFactory }

    companion object {
        const val REQUEST_KEY = "server_request_key"
        private const val SERVER_ID_KEY = "serverId"
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
        _binding = ModalAddServerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragmentResultListener()

        binding.btnAddServer.setOnClickListener {
            saveServer()
        }

        binding.btnCancelServer.setOnClickListener {
            viewModel.onAction(AddEditServerIntent.NavigateBack)
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

    private fun handleEffect(effect: AddEditServerEffect) {
        when (effect) {
            is AddEditServerEffect.NavigateBack -> {
                findNavController().navigateUp()
            }

            is AddEditServerEffect.ShowMessage -> {
                Toast.makeText(requireContext(), effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun renderState(state: AddEditServerState) {

        if (state.isEditMode) {
            binding.title.text = "Edit Server"
            binding.btnAddServer.text = "Update Server"
        } else {
            binding.title.text = "Add New Server"
            binding.btnAddServer.text = "Add Server"
        }
        if (state.name.isNotEmpty() && binding.etServerName.text.toString().isEmpty()) {
            binding.etServerName.setText(state.name)
        }
        if (state.url.isNotEmpty() && binding.etServerIp.text.toString().isEmpty()) {
            binding.etServerIp.setText(state.url)
        }
        if (state.username.isNotEmpty() && binding.etUsername.text.toString().isEmpty()) {
            binding.etUsername.setText(state.username)
        }
        if (state.password.isNotEmpty() && binding.etPassword.text.toString().isEmpty()) {
            binding.etPassword.setText(state.password)
        }
    }

    private fun setupFragmentResultListener() {
        parentFragmentManager.setFragmentResultListener(
            REQUEST_KEY,
            viewLifecycleOwner
        ) { requestKey, bundle ->
            val serverId = bundle.getString(SERVER_ID_KEY)
            viewModel.onAction(AddEditServerIntent.SetServerId(serverId))
        }
    }

    private fun saveServer() {
        val name = binding.etServerName.text?.toString()?.trim() ?: ""
        val ip = binding.etServerIp.text?.toString()?.trim() ?: ""
        val username = binding.etUsername.text?.toString()?.trim() ?: ""
        val password = binding.etPassword.text?.toString()?.trim() ?: ""

        val state = viewModel.state.value
        if (state.isEditMode) {
            viewModel.onAction(
                AddEditServerIntent.UpdateServer(
                    name = name,
                    ip = ip,
                    username = username,
                    password = password
                )
            )
        } else {
            viewModel.onAction(
                AddEditServerIntent.AddServer(
                    name = name,
                    ip = ip,
                    username = username,
                    password = password
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}