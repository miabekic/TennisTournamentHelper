package com.example.tennistournamenthelper.presentation.matches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.data.MatchRepository
import com.example.tennistournamenthelper.data.PlayerRepository
import com.example.tennistournamenthelper.data.TournamentRepository
import com.example.tennistournamenthelper.model.Gender
import com.example.tennistournamenthelper.model.Tournament
import kotlinx.coroutines.launch

class NewMatchTournamentRelatedViewModel(
    private val tournamentRepository: TournamentRepository,
    matchRepository: MatchRepository,
    private val playerRepository: PlayerRepository
) : NewMatchTournamentNoRelatedViewModel(matchRepository) {
    private val _tournamentsNames = MutableLiveData<List<String>>()
    val tournamentsNames: LiveData<List<String>> = _tournamentsNames
    private lateinit var tournaments: List<Tournament>
    private val _games = MutableLiveData<List<String>>()
    val games: LiveData<List<String>> = _games
    private val _malePlayers = MutableLiveData<List<String>?>()
    val malePlayers: LiveData<List<String>?> = _malePlayers
    private val _femalePlayers = MutableLiveData<List<String>?>()
    val femalePlayers: LiveData<List<String>?> = _femalePlayers

    init {
        getTournamentsNames()
    }

    fun getTournamentsNames() {
        viewModelScope.launch {
            when (val result = tournamentRepository.getAllCurrentTournaments()) {
                is ResultStatus.Success -> {
                    tournaments = result.data!!
                    _tournamentsNames.value = tournaments.map {
                        it.name
                    }
                }
                is ResultStatus.Failure -> {
                    _tournamentsNames.value = null
                }
                is ResultStatus.Loading -> {}
            }

        }
    }


    fun setMatchBasicInfo(tournamentIndex: Int) {
        val selectedTournament = tournaments[tournamentIndex]
        match.tournamentId = selectedTournament.id
        match.tournamentName = selectedTournament.name
        setSurface(selectedTournament.surface)
        _games.value = selectedTournament.games.map {
            it.type.toString()
        }
    }

    fun setMatchGameRules(tournamentIndex: Int, gameIndex: Int) {
        val selectedTournament = tournaments[tournamentIndex]
        setType(selectedTournament.games[gameIndex].type)
        println(selectedTournament.games[gameIndex].type)
        setSets(selectedTournament.games[gameIndex].sets)
        println(selectedTournament.games[gameIndex].sets)
        setTiebreak(selectedTournament.games[gameIndex].tiebreak)
    }

    fun setMalePlayers(tournamentIndex: Int) {
        viewModelScope.launch {
            when (val result = playerRepository.getAllPlayers(tournaments[tournamentIndex].id!!)) {
                is ResultStatus.Success -> {
                    _malePlayers.value = result.data?.filter {
                        it.gender == Gender.MALE
                    }?.map {
                        it.name
                    }
                }
                is ResultStatus.Failure -> {
                    println(result.message.toString())
                    _malePlayers.value = null
                }
                is ResultStatus.Loading -> {}
            }
        }
    }

    fun setFemalePlayers(tournamentIndex: Int) {
        viewModelScope.launch {
            when (val result = playerRepository.getAllPlayers(
                tournaments[tournamentIndex].id!!
            )) {
                is ResultStatus.Success -> {
                    _femalePlayers.value = result.data?.filter {
                        it.gender == Gender.FEMALE
                    }?.map {
                        it.name
                    }
                }
                is ResultStatus.Failure -> {
                    println(result.message.toString())
                    _femalePlayers.value = null
                }
                is ResultStatus.Loading -> {}
            }
        }
    }

    fun setRound(round: String) {
        match.round = round
    }

}