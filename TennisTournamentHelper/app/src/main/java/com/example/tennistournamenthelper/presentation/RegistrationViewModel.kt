package com.example.tennistournamenthelper.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.data.FirebaseAuthManager
import kotlinx.coroutines.launch

class RegistrationViewModel(private val auth: FirebaseAuthManager) : ViewModel() {

    private val _registrationUiState = MutableLiveData<ResultStatus<Unit>>()
    val registrationUiState: LiveData<ResultStatus<Unit>> = _registrationUiState

    fun register(email: String, password: String, confirmPassword: String) {
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            _registrationUiState.value = ResultStatus.Failure("Please enter email and password.")
        } else if (password.length < 6) {
            _registrationUiState.value =
                ResultStatus.Failure("Password must contain min. 6 characters")
        } else if (confirmPassword != password) {
            _registrationUiState.value = ResultStatus.Failure("Passwords are not matching.")
        } else {
            _registrationUiState.value = ResultStatus.Loading()
            viewModelScope.launch {
                _registrationUiState.value = auth.register(email, password)
            }
        }
    }

}