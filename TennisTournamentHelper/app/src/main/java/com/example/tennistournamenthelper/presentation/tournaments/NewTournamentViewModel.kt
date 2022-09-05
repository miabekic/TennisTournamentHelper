package com.example.tennistournamenthelper.presentation.tournaments

import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.data.TournamentRepository
import com.example.tennistournamenthelper.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


class NewTournamentViewModel(private val tournamentRepository: TournamentRepository) : ViewModel() {

    private var _tournament = Tournament()
    val tournament: Tournament = _tournament

    private val _gamesRules = mutableListOf<Game>()
    val gamesRules: MutableList<Game> = _gamesRules

    private val _checkedFemaleSingles = MutableLiveData(false)
    val checkedFemaleSingles: LiveData<Boolean> = _checkedFemaleSingles
    private val _checkedFemaleDoubles = MutableLiveData(false)
    val checkedFemaleDoubles: LiveData<Boolean> = _checkedFemaleDoubles
    private val _checkedMaleSingles = MutableLiveData(false)
    val checkedMaleSingles: LiveData<Boolean> = _checkedMaleSingles
    private val _checkedMaleDoubles = MutableLiveData(false)
    val checkedMaleDoubles: LiveData<Boolean> = _checkedMaleDoubles
    private val _checkedMixedDoubles = MutableLiveData(false)
    val checkedMixedDoubles: LiveData<Boolean> = _checkedMixedDoubles

    private val _createNewTournamentUiState = MutableLiveData<ResultStatus<Unit>>()
    val createNewTournamentUiState: LiveData<ResultStatus<Unit>> = _createNewTournamentUiState

    fun reset() {
        _tournament = Tournament()
        _gamesRules.clear()
        _checkedFemaleSingles.value = false
        _checkedMaleSingles.value = false
        _checkedFemaleDoubles.value = false
        _checkedMaleDoubles.value = false
        _checkedMixedDoubles.value = false
        _createNewTournamentUiState.value = null
    }

    suspend fun getLocation(geocoder: Geocoder, location: String): ResultStatus<Address> =
        withContext(Dispatchers.IO) {
            try {
                val result = geocoder.getFromLocationName(location, 1)
                if (result.isNotEmpty()) {
                    ResultStatus.Success(result[0])
                } else {
                    ResultStatus.Failure("No such place")
                }
            } catch (e: IOException) {
                ResultStatus.Failure(e.message.toString())
            }

        }


    fun setSets(gameType: GameType, sets: Int) {
        _gamesRules[getIndexOfGameRulesByGameType(gameType)].sets = sets
    }

    fun setTiebreak(gameType: GameType, tiebreak: Tiebreak) {
        _gamesRules[getIndexOfGameRulesByGameType(gameType)].tiebreak = tiebreak
    }

    fun addRemoveFemaleSingles(add: Boolean) {
        if (add) {
            _gamesRules.add(_gamesRules.size, Game(GameType.SINGLES_FEMALE))
        } else {
            _gamesRules.removeAt(getIndexOfGameRulesByGameType(GameType.SINGLES_FEMALE))
        }
    }

    private fun removeFemaleGames() {
        if (_gamesRules.isNotEmpty()) {
            val femaleSinglesIndex = getIndexOfGameRulesByGameType(GameType.SINGLES_FEMALE)
            if (femaleSinglesIndex != -1) {
                _gamesRules.removeAt(femaleSinglesIndex)
            }
            val femaleDoublesIndex = getIndexOfGameRulesByGameType(GameType.DOUBLES_FEMALE)
            if (femaleDoublesIndex != -1) {
                _gamesRules.removeAt(femaleDoublesIndex)
            }
        }
    }

    private fun removeMaleGames() {
        if (_gamesRules.isNotEmpty()) {
            val maleSinglesIndex = getIndexOfGameRulesByGameType(GameType.SINGLES_MALE)
            if (maleSinglesIndex != -1) {
                _gamesRules.removeAt(maleSinglesIndex)
            }
            val maleDoublesIndex = getIndexOfGameRulesByGameType(GameType.DOUBLES_MALE)
            if (maleDoublesIndex != -1) {
                _gamesRules.removeAt(maleDoublesIndex)
            }
        }
    }

    private fun removeMixedGames() {
        if (_gamesRules.isNotEmpty()) {
            val mixedDoublesIndex = getIndexOfGameRulesByGameType(GameType.MIXED_DOUBLES)
            if (mixedDoublesIndex != -1) {
                _gamesRules.removeAt(mixedDoublesIndex)
            }
        }
    }


    fun getIndexOfGameRulesByGameType(gameType: GameType): Int {
        return when (gameType) {
            GameType.SINGLES_MALE -> _gamesRules.indexOfFirst { it.type == gameType }
            GameType.DOUBLES_MALE -> _gamesRules.indexOfFirst { it.type == gameType }
            GameType.SINGLES_FEMALE -> _gamesRules.indexOfFirst { it.type == gameType }
            GameType.DOUBLES_FEMALE -> _gamesRules.indexOfFirst { it.type == gameType }
            GameType.MIXED_DOUBLES -> _gamesRules.indexOfFirst { it.type == gameType }
        }
    }

    fun addRemoveMaleSingles(add: Boolean) {
        if (add) {
            _gamesRules.add(_gamesRules.size, Game(GameType.SINGLES_MALE))
        } else {
            _gamesRules.removeAt(getIndexOfGameRulesByGameType(GameType.SINGLES_MALE))
        }
    }


    fun addRemoveFemaleDoubles(add: Boolean) {
        if (add) {
            _gamesRules.add(_gamesRules.size, Game(GameType.DOUBLES_FEMALE))
        } else {
            _gamesRules.removeAt(getIndexOfGameRulesByGameType(GameType.DOUBLES_FEMALE))
        }
    }


    fun addRemoveMaleDoubles(add: Boolean) {
        if (add) {
            _gamesRules.add(_gamesRules.size, Game(GameType.DOUBLES_MALE))
        } else {
            _gamesRules.removeAt(getIndexOfGameRulesByGameType(GameType.DOUBLES_MALE))
        }
    }

    fun addRemoveMixedDoubles(add: Boolean) {
        if (add) {
            _gamesRules.add(_gamesRules.size, Game(GameType.MIXED_DOUBLES))
        } else {
            removeMixedGames()
        }
    }

    fun setCheckedFemaleSingles(checked: Boolean) {
        _checkedFemaleSingles.value = checked
    }

    fun setCheckedFemaleDoubles(checked: Boolean) {
        _checkedFemaleDoubles.value = checked
    }

    fun setCheckedMaleSingles(checked: Boolean) {
        _checkedMaleSingles.value = checked
    }

    fun setCheckedMaleDoubles(checked: Boolean) {
        _checkedMaleDoubles.value = checked
    }

    fun setCheckedMixedDoubles(checked: Boolean) {
        _checkedMixedDoubles.value = checked
    }

    fun resetMaleGames() {
        _checkedMaleDoubles.value = false
        _checkedMaleSingles.value = false
        removeMaleGames()
    }

    fun resetFemaleGames() {
        _checkedFemaleDoubles.value = false
        _checkedFemaleSingles.value = false
        removeFemaleGames()
    }

    fun resetMixedDoublesGame() {
        _checkedMixedDoubles.value = false
        removeMixedGames()
    }

    fun setSurface(surface: Surface) {
        _tournament.surface = surface
    }

    fun setPlayersGender(playersGender: PlayersGender) {
        _tournament.allowedPlayersGender = playersGender
    }

    fun setBasicInfo(name: String, startDate: String, finishDate: String) {
        _tournament.name = name
        _tournament.startDate = startDate
        _tournament.endDate = finishDate
    }

    fun setPlace(name: String, location: Location) {
        _tournament.place.name = name
        _tournament.place.location = location
    }

    fun setGamesRules() {
        _tournament.games = _gamesRules
    }

    fun saveTournament() {
        _createNewTournamentUiState.value = ResultStatus.Loading()
        viewModelScope.launch {
            _createNewTournamentUiState.value =
                tournamentRepository.saveCurrentTournament(_tournament)
        }
    }
}



