package com.example.tennistournamenthelper.presentation.players

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.data.PlayerRepository
import com.example.tennistournamenthelper.model.Player
import kotlinx.coroutines.launch

class PlayerListViewModel(private val playerRepository: PlayerRepository) : ViewModel() {
    private val _playerListUiState = MutableLiveData<ResultStatus<List<Player>>>()
    val playerListUiState: LiveData<ResultStatus<List<Player>>> = _playerListUiState

    private val _deleteState = MutableLiveData<Boolean>()
    val deleteState: LiveData<Boolean> = _deleteState

    fun getPlayers(tournamentId: String) {
        _playerListUiState.value = ResultStatus.Loading()
        viewModelScope.launch {
            _playerListUiState.value = playerRepository.getAllPlayers(tournamentId)
        }
    }

    fun setDeleteState(state: Boolean?) {
        _deleteState.value = state
    }

    fun deletePlayer(playerId: String, tournamentId: String) {
        viewModelScope.launch {
            when (playerRepository.deletePlayer(playerId, tournamentId)) {
                is ResultStatus.Success -> setDeleteState(true)
                is ResultStatus.Failure -> setDeleteState(false)
                is ResultStatus.Loading -> {}
            }
        }
    }

}