package com.example.tennistournamenthelper.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tennistournamenthelper.ResultStatus

abstract class BaseDetailsViewModel<T> : ViewModel() {
    private val _detailsUiState = MutableLiveData<ResultStatus<T?>>()
    val detailsUiState: LiveData<ResultStatus<T?>> = _detailsUiState

    fun setDetailsUiState(state: ResultStatus<T?>) {
        _detailsUiState.value = state
    }

    abstract fun getDetails(id: String)
}