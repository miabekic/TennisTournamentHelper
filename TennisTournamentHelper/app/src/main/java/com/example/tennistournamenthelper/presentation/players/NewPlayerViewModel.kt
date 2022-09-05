package com.example.tennistournamenthelper.presentation.players

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.data.PlayerRepository
import com.example.tennistournamenthelper.data.TournamentRepository
import com.example.tennistournamenthelper.model.Gender
import com.example.tennistournamenthelper.model.Player
import com.example.tennistournamenthelper.model.PlayersGender
import kotlinx.coroutines.launch

class NewPlayerViewModel(
    private val playerRepository: PlayerRepository,
    private val tournamentRepository: TournamentRepository
) : ViewModel() {
    private val _newPlayerUiState = MutableLiveData<ResultStatus<Unit>>()
    val newPlayerUiState: LiveData<ResultStatus<Unit>> = _newPlayerUiState
    private val _allowedGenders = MutableLiveData<PlayersGender>()
    val allowedGenders: LiveData<PlayersGender> = _allowedGenders

    fun savePlayer(name: String, gender: Gender, tournamentId: String) {
        viewModelScope.launch {
            _newPlayerUiState.value =
                playerRepository.savePlayer(Player("", name, gender), tournamentId)
        }
    }

    fun getAllowedGenders(id: String) {
        viewModelScope.launch {
            when (val result = tournamentRepository.getCurrentTournamentById(id)) {
                is ResultStatus.Success -> {
                    _allowedGenders.value = result.data?.allowedPlayersGender
                }
                is ResultStatus.Failure -> {
                    _allowedGenders.value = null
                }
                is ResultStatus.Loading -> {}
            }
        }
    }
}