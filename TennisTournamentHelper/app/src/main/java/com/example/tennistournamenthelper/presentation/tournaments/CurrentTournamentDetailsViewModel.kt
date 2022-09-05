package com.example.tennistournamenthelper.presentation.tournaments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.data.TournamentRepository
import com.example.tennistournamenthelper.model.Tournament
import com.example.tennistournamenthelper.presentation.BaseDetailsViewModel
import kotlinx.coroutines.launch

class CurrentTournamentDetailsViewModel(private val tournamentRepository: TournamentRepository) :
    BaseDetailsViewModel<Tournament>() {
    private val _finishedState = MutableLiveData<Boolean?>()
    val finishedState: LiveData<Boolean?> = _finishedState

    fun setTournamentFinished(tournament: Tournament) {
        viewModelScope.launch {
            when (tournamentRepository.deleteCurrentTournament(tournament.id!!)) {
                is ResultStatus.Success -> {
                    when (tournamentRepository.saveFinishedTournament(tournament)) {
                        is ResultStatus.Success -> _finishedState.value = true
                        is ResultStatus.Failure -> {
                            tournamentRepository.saveCurrentTournament(tournament)
                            _finishedState.value = false
                        }
                        is ResultStatus.Loading -> {}
                    }
                }
                is ResultStatus.Failure -> {
                    _finishedState.value = false
                }
                is ResultStatus.Loading -> {}
            }
        }
    }

    override fun getDetails(id: String) {
        setDetailsUiState(ResultStatus.Loading())
        viewModelScope.launch {
            setDetailsUiState(tournamentRepository.getCurrentTournamentById(id))
        }
    }
}