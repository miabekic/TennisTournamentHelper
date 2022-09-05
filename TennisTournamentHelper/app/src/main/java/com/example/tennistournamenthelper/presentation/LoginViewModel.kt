package com.example.tennistournamenthelper.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.data.FirebaseAuthManager
import kotlinx.coroutines.launch

class LoginViewModel(private val auth: FirebaseAuthManager) : ViewModel() {

    private val _loginUiState = MutableLiveData<ResultStatus<String>>()
    val loginUiState: LiveData<ResultStatus<String>> = _loginUiState
    private val _forgotPasswordState = MutableLiveData<ResultStatus<Unit>>()
    val forgotPasswordState: LiveData<ResultStatus<Unit>> = _forgotPasswordState

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _loginUiState.value = ResultStatus.Failure("Missing email and/or password.")
        } else {
            _loginUiState.value = ResultStatus.Loading()
            viewModelScope.launch {
                _loginUiState.value = auth.login(email, password)
            }
        }
    }

    fun sendResetPasswordEmail(email: String) {
        _forgotPasswordState.value = ResultStatus.Loading()
        viewModelScope.launch {
            _forgotPasswordState.value = auth.sendResetPasswordEmail(email)
        }
    }

    fun sendVerificationEmail() {
        viewModelScope.launch {
            auth.sendVerificationEmail()
        }
    }
}