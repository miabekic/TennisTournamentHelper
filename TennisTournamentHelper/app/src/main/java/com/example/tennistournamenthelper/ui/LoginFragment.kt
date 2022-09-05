package com.example.tennistournamenthelper.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tennistournamenthelper.MainActivity
import com.example.tennistournamenthelper.R
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.databinding.FragmentLoginBinding
import com.example.tennistournamenthelper.presentation.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        var alertDialog: AlertDialog? = null
        binding.apply {
            bLogin.setOnClickListener {
                viewModel.login(etEmailLogin.text.toString(), etPasswordLogin.text.toString())
            }
            tvDoNotHaveAccount.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
                findNavController().navigate(action)
            }

            tvForgotPassword.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                val view = layoutInflater.inflate(R.layout.custom_dialog, null)
                val etEmail = view.findViewById<EditText>(R.id.etEmailForgetPassword)
                builder.setTitle(
                    context?.getString(R.string.send_reset_password_email)
                )
                    .setCancelable(false)
                    .setPositiveButton(context?.getString(R.string.send), null)
                    .setNegativeButton(
                        context?.getString(R.string.cancel)
                    ) { dialog, _ ->
                        dialog.dismiss()
                    }
                builder.setView(view)
                alertDialog = builder.create()
                alertDialog?.show()
                alertDialog?.getButton(DialogInterface.BUTTON_POSITIVE)?.setOnClickListener {
                    if (etEmail.text.toString().isNotEmpty()) {
                        viewModel.sendResetPasswordEmail(etEmail.text.toString())
                    } else Toast.makeText(
                        context,
                        Resources.getSystem().getString(R.string.missing_email),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            tvAlertLogin.setOnClickListener {
                if (tvAlertLogin.text.equals(
                        Resources.getSystem().getString(R.string.email_not_verified)
                    )
                ) {
                    viewModel.sendVerificationEmail()
                }
            }
        }
        viewModel.loginUiState.observe(viewLifecycleOwner) { login ->
            when (login) {
                is ResultStatus.Loading -> {
                    setEnabledButtons(false)
                }
                is ResultStatus.Success -> {
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    this.activity?.finish()
                }
                is ResultStatus.Failure -> {
                    setEnabledButtons(true)
                    binding.tvAlertLogin.text = login.message.toString()
                }
            }
        }
        viewModel.forgotPasswordState.observe(viewLifecycleOwner) {
            when (it) {
                is ResultStatus.Loading -> {
                    alertDialog?.getButton(DialogInterface.BUTTON_POSITIVE)?.isEnabled = false
                }
                is ResultStatus.Success -> alertDialog?.dismiss()
                is ResultStatus.Failure -> {
                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()
                    alertDialog?.getButton(DialogInterface.BUTTON_POSITIVE)?.isEnabled = true
                }
            }
        }
        return binding.root
    }

    private fun setEnabledButtons(enabled: Boolean) {
        binding.bLogin.isEnabled = enabled
        binding.tvDoNotHaveAccount.isEnabled = enabled
    }
}