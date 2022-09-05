package com.example.tennistournamenthelper.presentation.matches

import androidx.lifecycle.viewModelScope
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.data.MatchRepository
import com.example.tennistournamenthelper.model.Match
import com.example.tennistournamenthelper.presentation.BaseListViewModel
import kotlinx.coroutines.launch


class TrailMatchListViewModel(private val matchRepository: MatchRepository) :
    BaseListViewModel<Match>() {

    init {
        getContent()
    }

    override fun getContent() {
        setListUiState(ResultStatus.Loading())
        viewModelScope.launch {
            setListUiState(matchRepository.getAllTrailMatches())
        }
    }

    override fun delete(id: String) {
        viewModelScope.launch {
            when (matchRepository.deleteTrailMatch(id)) {
                is ResultStatus.Success -> setDeleteState(true)
                is ResultStatus.Failure -> setDeleteState(false)
                is ResultStatus.Loading -> {}
            }
        }
    }

}