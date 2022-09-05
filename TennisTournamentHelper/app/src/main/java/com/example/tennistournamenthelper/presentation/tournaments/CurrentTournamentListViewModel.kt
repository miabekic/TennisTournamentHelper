package com.example.tennistournamenthelper.presentation.tournaments

import androidx.lifecycle.viewModelScope
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.data.TournamentRepository
import com.example.tennistournamenthelper.model.Tournament
import com.example.tennistournamenthelper.presentation.BaseListViewModel
import kotlinx.coroutines.launch

class CurrentTournamentListViewModel(private val tournamentRepository: TournamentRepository) :
    BaseListViewModel<Tournament>() {

    init {
        getContent()
    }


    override fun getContent() {
        setListUiState(ResultStatus.Loading())
        viewModelScope.launch {
            setListUiState(tournamentRepository.getAllCurrentTournaments())
        }
    }

    override fun delete(id: String) {
        viewModelScope.launch {
            when (tournamentRepository.deleteCurrentTournament(id)) {
                is ResultStatus.Success -> setDeleteState(true)
                is ResultStatus.Failure -> setDeleteState(false)
                is ResultStatus.Loading -> {}
            }
        }
    }

}