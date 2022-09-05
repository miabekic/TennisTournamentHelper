package com.example.tennistournamenthelper.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tennistournamenthelper.ResultStatus

abstract class BaseListViewModel<T> : ViewModel() {
    private val _listUiState = MutableLiveData<ResultStatus<List<T>>>()
    val listUiState: LiveData<ResultStatus<List<T>>> = _listUiState

    private val _deleteState = MutableLiveData<Boolean>()
    val deleteState: LiveData<Boolean> = _deleteState
    fun setListUiState(state: ResultStatus<List<T>>) {
        _listUiState.value = state
    }

    fun setDeleteState(state: Boolean) {
        _deleteState.value = state
    }

    fun resetDeleteState() {
        _deleteState.value = null
    }

    abstract fun getContent()
    abstract fun delete(id: String)
}