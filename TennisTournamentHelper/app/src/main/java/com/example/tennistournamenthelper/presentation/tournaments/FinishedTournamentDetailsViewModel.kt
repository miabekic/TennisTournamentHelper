package com.example.tennistournamenthelper.presentation.tournaments

import androidx.lifecycle.viewModelScope
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.data.TournamentRepository
import com.example.tennistournamenthelper.model.Tournament
import com.example.tennistournamenthelper.presentation.BaseDetailsViewModel
import kotlinx.coroutines.launch

class FinishedTournamentDetailsViewModel(private val tournamentRepository: TournamentRepository) :
    BaseDetailsViewModel<Tournament>() {

    override fun getDetails(id: String) {
        setDetailsUiState(ResultStatus.Loading())
        viewModelScope.launch {
            setDetailsUiState(tournamentRepository.getFinishedTournamentById(id))
        }
    }
}