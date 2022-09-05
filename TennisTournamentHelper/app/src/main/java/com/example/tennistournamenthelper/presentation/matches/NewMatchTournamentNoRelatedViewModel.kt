package com.example.tennistournamenthelper.presentation.matches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.data.MatchRepository
import com.example.tennistournamenthelper.model.GameType
import com.example.tennistournamenthelper.model.Match
import com.example.tennistournamenthelper.model.Surface
import com.example.tennistournamenthelper.model.Tiebreak
import kotlinx.coroutines.launch

open class NewMatchTournamentNoRelatedViewModel(private val matchRepository: MatchRepository) :
    ViewModel() {

    val match = Match()
    private val _createMatchUiState = MutableLiveData<ResultStatus<Unit>>()
    val createMatchUiState: LiveData<ResultStatus<Unit>> = _createMatchUiState

    private fun setCreateMatchUiState(result: ResultStatus<Unit>) {
        _createMatchUiState.value = result
    }

    fun setSets(sets: Int) {
        match.game.sets = sets
    }

    fun setTiebreak(tiebreak: Tiebreak) {
        match.game.tiebreak = tiebreak
    }

    fun setPlayers(playersFirst: List<String>, playersSecond: List<String>) {
        match.playersOne = playersFirst
        match.playersTwo = playersSecond
    }

    fun setSurface(surface: Surface) {
        match.surface = surface
    }

    fun setType(type: GameType) {
        match.game.type = type
    }

    fun setWinner(winner: List<String>) {
        match.winners = winner
    }

    fun setResult(result: String) {
        match.result.result = result
    }

    fun setDate(date: String) {
        match.date = date
    }

    fun saveMatch(isMatchFinished: Boolean) {
        setCreateMatchUiState(ResultStatus.Loading())
        viewModelScope.launch {
            if (isMatchFinished) {
                val result = matchRepository.saveFinishedMatch(match)
                setCreateMatchUiState(result)
            } else {
                val result = matchRepository.saveNewTrailMatch(match)
                setCreateMatchUiState(result)
            }
        }

    }
}