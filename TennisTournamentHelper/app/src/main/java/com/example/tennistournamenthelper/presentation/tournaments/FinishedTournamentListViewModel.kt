package com.example.tennistournamenthelper.presentation.tournaments

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.data.MatchRepository
import com.example.tennistournamenthelper.data.PlayerRepository

import com.example.tennistournamenthelper.data.TournamentRepository
import com.example.tennistournamenthelper.model.Tournament
import com.example.tennistournamenthelper.presentation.BaseListViewModel
import kotlinx.coroutines.launch

class FinishedTournamentListViewModel(
    private val tournamentRepository: TournamentRepository,
    private val playerRepository: PlayerRepository,
    private val matchRepository: MatchRepository
) : BaseListViewModel<Tournament>() {

    init {
        getContent()
    }

    override fun getContent() {
        setListUiState(ResultStatus.Loading())
        viewModelScope.launch {
            setListUiState(tournamentRepository.getAllFinishedTournaments())
        }
    }

    override fun delete(id: String) {
        viewModelScope.launch {
            when (playerRepository.deletePlayersByTournamentId(id)) {
                is ResultStatus.Success -> {
                    when (matchRepository.deleteMatchesByTournamentId(id)) {
                        is ResultStatus.Success -> {
                            when (tournamentRepository.deleteFinishedTournament(id)) {
                                is ResultStatus.Success -> setDeleteState(true)
                                is ResultStatus.Failure -> setDeleteState(false)
                                is ResultStatus.Loading -> {}
                            }
                        }
                        is ResultStatus.Failure -> setDeleteState(false)
                        is ResultStatus.Loading -> {}
                    }
                }
                is ResultStatus.Failure -> setDeleteState(false)
                is ResultStatus.Loading -> {}
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        Log.d("Unistenje", "FinishedTournamentListViewModel je unisten")
    }


}