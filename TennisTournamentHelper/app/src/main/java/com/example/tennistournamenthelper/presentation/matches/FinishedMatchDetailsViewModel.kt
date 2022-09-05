package com.example.tennistournamenthelper.presentation.matches

import androidx.lifecycle.viewModelScope
import com.example.tennistournamenthelper.data.MatchRepository
import com.example.tennistournamenthelper.model.Match
import com.example.tennistournamenthelper.presentation.BaseDetailsViewModel
import kotlinx.coroutines.launch

class FinishedMatchDetailsViewModel(private val matchRepository: MatchRepository) :
    BaseDetailsViewModel<Match>() {

    override fun getDetails(id: String) {
        viewModelScope.launch {
            setDetailsUiState(matchRepository.getFinishedMatchById(id))
        }
    }
}