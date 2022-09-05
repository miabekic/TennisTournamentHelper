package com.example.tennistournamenthelper.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.databinding.FragmentRegistrationBinding
import com.example.tennistournamenthelper.presentation.RegistrationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private val registerViewModel: RegistrationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        binding.apply {
            bRegister.setOnClickListener {
                registerViewModel.register(
                    etEmailRegistration.text.toString(),
                    etPasswordRegistration.text.toString(),
                    etConfirmPassword.text.toString()
                )
            }
        }

        registerViewModel.registrationUiState.observe(viewLifecycleOwner) {
            when (it) {
                is ResultStatus.Loading -> {
                    binding.bRegister.isEnabled = false
                }
                is ResultStatus.Success -> {
                    val action =
                        RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment()
                    findNavController().navigate(action)
                }
                is ResultStatus.Failure -> {
                    binding.bRegister.isEnabled = true
                    binding.tvAlertRegistration.text = it.message.toString()
                }
            }
        }

        return binding.root
    }
}