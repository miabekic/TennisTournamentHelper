package com.example.tennistournamenthelper.presentation.matches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.data.MatchRepository
import com.example.tennistournamenthelper.model.Match
import com.example.tennistournamenthelper.model.Point
import com.example.tennistournamenthelper.model.Statistic
import com.example.tennistournamenthelper.model.Tiebreak
import kotlinx.coroutines.launch

class TrailViewModel(private val matchRepository: MatchRepository) : ViewModel() {

    private val _setupStatus = MutableLiveData<ResultStatus<Match>>()
    val setupStatus: LiveData<ResultStatus<Match>> = _setupStatus
    private val _trailUiStatus = MutableLiveData<ResultStatus<Unit>>()
    val trailUiStatus: LiveData<ResultStatus<Unit>> = _trailUiStatus
    lateinit var match: Match
    private val result = StringBuilder()

    fun initializeMatch(match: Match) {
        this.match = match
    }

    fun saveSetResult(set: Int) {
        when (set) {
            1 -> {
                result.append(" ${_firstSetPlayersOne.value} - ${_firstSetPlayersTwo.value} ")
            }
            2 -> {
                result.append(" ${_secondSetPlayersOne.value} - ${_secondSetPlayersTwo.value} ")
            }
            3 -> {
                result.append(" ${_thirdSetPlayersOne.value} - ${_thirdSetPlayersTwo.value} ")
            }
            4 -> {
                result.append(" ${_fourthSetPlayersOne.value} - ${_fourthSetPlayersTwo.value} ")
            }
            5 -> {
                result.append(" ${_fifthSetPlayersOne.value} - ${_fifthSetPlayersTwo.value} ")
            }
        }
    }

    fun getMatch(id: String) {
        _setupStatus.value = ResultStatus.Loading()
        viewModelScope.launch {
            _setupStatus.value = matchRepository.getTrailMatchById(id)
        }
    }

    private val _firstSetPlayersOne = MutableLiveData(0)
    val firstSetPlayersOne: LiveData<Int> = _firstSetPlayersOne
    private val _firstSetPlayersTwo = MutableLiveData(0)
    val firstSetPlayersTwo: LiveData<Int> = _firstSetPlayersTwo

    private val _secondSetPlayersTwo = MutableLiveData(0)
    val secondSetPlayersTwo: LiveData<Int> = _secondSetPlayersTwo
    private val _secondSetPlayersOne = MutableLiveData(0)
    val secondSetPlayersOne: LiveData<Int> = _secondSetPlayersOne

    private val _thirdSetPlayersTwo = MutableLiveData(0)
    val thirdSetPlayersTwo: LiveData<Int> = _thirdSetPlayersTwo
    private val _thirdSetPlayersOne = MutableLiveData(0)
    val thirdSetPlayersOne: LiveData<Int> = _thirdSetPlayersOne

    private val _fourthSetPlayersTwo = MutableLiveData(0)
    val fourthSetPlayersTwo: LiveData<Int> = _fourthSetPlayersTwo
    private val _fourthSetPlayersOne = MutableLiveData(0)
    val fourthSetPlayersOne: LiveData<Int> = _fourthSetPlayersOne

    private val _fifthSetPlayersTwo = MutableLiveData(0)
    val fifthSetPlayersTwo: LiveData<Int> = _fifthSetPlayersTwo
    private val _fifthSetPlayersOne = MutableLiveData(0)
    val fifthSetPlayersOne: LiveData<Int> = _fifthSetPlayersOne

    private val _pointsPlayerOne = MutableLiveData(Point.LOVE.toString())
    val pointsPlayerOne: LiveData<String> = _pointsPlayerOne
    private val _pointsPlayersTwo = MutableLiveData(Point.LOVE.toString())
    val pointsPlayerTwo: LiveData<String> = _pointsPlayersTwo


    private val _setsWonPlayersOne = MutableLiveData(0)
    val setsWonPlayersOne: LiveData<Int> = _setsWonPlayersOne
    private val _setsWonPlayersTwo = MutableLiveData(0)
    val setsWonPlayersTwo: LiveData<Int> = _setsWonPlayersTwo

    private val statisticPlayersOne: Statistic = Statistic()
    private val statisticPlayersTwo: Statistic = Statistic()

    private val _tiebreakPointsPlayersOne = MutableLiveData(0)
    val tiebreakPointsPlayersOne: LiveData<Int> = _tiebreakPointsPlayersOne

    private val _tiebreakPointsPlayersTwo = MutableLiveData(0)
    val tiebreakPointsPlayersTwo: LiveData<Int> = _tiebreakPointsPlayersTwo

    fun addAcePlayersOne() {
        statisticPlayersOne.ace++
    }

    private fun resetPoints() {
        _pointsPlayerOne.value = Point.LOVE.toString()
        _pointsPlayersTwo.value = Point.LOVE.toString()
    }

    fun changePointsPlayerOne() {
        if (match.game.tiebreak != Tiebreak.NOT_ALLOWED && ((match.game.sets == 2 && _setsWonPlayersOne.value?.plus(
                _setsWonPlayersTwo.value!!
            ) == 2 && (_thirdSetPlayersOne.value == 6 && _thirdSetPlayersTwo.value == 6)) || ((match.game.sets == 3 && _setsWonPlayersOne.value?.plus(
                _setsWonPlayersTwo.value!!
            ) == 4 && (_fifthSetPlayersOne.value == 6 && _fifthSetPlayersTwo.value == 6))))
        ) {
            _tiebreakPointsPlayersOne.value = _tiebreakPointsPlayersOne.value?.plus(1)
        } else {
            when (_pointsPlayerOne.value) {
                Point.LOVE.toString() -> _pointsPlayerOne.value = Point.FIFTEEN.toString()
                Point.FIFTEEN.toString() -> _pointsPlayerOne.value = Point.THIRTY.toString()
                Point.THIRTY.toString() -> _pointsPlayerOne.value = Point.FORTY.toString()
                Point.FORTY.toString() -> if (_pointsPlayersTwo.value != Point.FORTY.toString()) {
                    addGameToPlayersOne()
                    resetPoints()
                } else {
                    _pointsPlayerOne.value = Point.ADVANTAGE.toString()
                    _pointsPlayersTwo.value = ""
                }
                Point.ADVANTAGE.toString() -> {
                    addGameToPlayersOne()
                    resetPoints()
                }
                "" -> {
                    _pointsPlayerOne.value = Point.FORTY.toString()
                    _pointsPlayersTwo.value = Point.FORTY.toString()
                }
            }
        }
    }

    fun changePointsPlayerTwo() {
        if (match.game.tiebreak != Tiebreak.NOT_ALLOWED && ((match.game.sets == 2 && _setsWonPlayersOne.value?.plus(
                _setsWonPlayersTwo.value!!
            ) == 2 && (_thirdSetPlayersOne.value == 6 && _thirdSetPlayersTwo.value == 6)) || ((match.game.sets == 3 && _setsWonPlayersOne.value?.plus(
                _setsWonPlayersTwo.value!!
            ) == 4) && (_fifthSetPlayersOne.value == 6 && _fifthSetPlayersTwo.value == 6)))
        ) {
            _tiebreakPointsPlayersTwo.value = _tiebreakPointsPlayersTwo.value?.plus(1)
        } else {
            when (_pointsPlayersTwo.value) {
                Point.LOVE.toString() -> _pointsPlayersTwo.value = Point.FIFTEEN.toString()
                Point.FIFTEEN.toString() -> _pointsPlayersTwo.value = Point.THIRTY.toString()
                Point.THIRTY.toString() -> _pointsPlayersTwo.value = Point.FORTY.toString()
                Point.FORTY.toString() -> if (_pointsPlayerOne.value != Point.FORTY.toString()) {
                    addGameToPlayersTwo()
                    resetPoints()
                } else {
                    _pointsPlayersTwo.value = Point.ADVANTAGE.toString()
                    _pointsPlayerOne.value = ""
                }
                Point.ADVANTAGE.toString() -> {
                    addGameToPlayersTwo()
                    resetPoints()
                }
                "" -> {
                    _pointsPlayerOne.value = Point.FORTY.toString()
                    _pointsPlayersTwo.value = Point.FORTY.toString()
                }
            }
        }
    }

    fun addGameToPlayersOne() {
        when (_setsWonPlayersOne.value?.plus(_setsWonPlayersTwo.value!!)) {
            0 -> _firstSetPlayersOne.value = _firstSetPlayersOne.value?.plus(1)
            1 -> _secondSetPlayersOne.value = _secondSetPlayersOne.value?.plus(1)
            2 -> _thirdSetPlayersOne.value = _thirdSetPlayersOne.value?.plus(1)
            3 -> _fourthSetPlayersOne.value = _fourthSetPlayersOne.value?.plus(1)
            4 -> _fifthSetPlayersOne.value = _fifthSetPlayersOne.value?.plus(1)
        }
    }

    fun addGameToPlayersTwo() {
        when (_setsWonPlayersOne.value?.plus(_setsWonPlayersTwo.value!!)) {
            0 -> _firstSetPlayersTwo.value = _firstSetPlayersTwo.value?.plus(1)
            1 -> _secondSetPlayersTwo.value = _secondSetPlayersTwo.value?.plus(1)
            2 -> _thirdSetPlayersTwo.value = _thirdSetPlayersTwo.value?.plus(1)
            3 -> _fourthSetPlayersTwo.value = _fourthSetPlayersTwo.value?.plus(1)
            4 -> _fifthSetPlayersTwo.value = _fifthSetPlayersTwo.value?.plus(1)
        }
    }

    fun setMatchFinished() {
        val match = match
        match.result.result = result.toString()
        match.result.statistic = listOf(statisticPlayersOne, statisticPlayersTwo)

        if (_setsWonPlayersOne.value!! > _setsWonPlayersTwo.value!!) {
            match.winners = match.playersOne
        } else match.winners = match.playersTwo
        _trailUiStatus.value = ResultStatus.Loading()
        viewModelScope.launch {
            _trailUiStatus.value = matchRepository.saveMatchAfterTrail(match)
        }
    }

    fun addDoubleFaultPlayersOne() {
        statisticPlayersOne.doubleFault++
    }

    fun addWinnerPlayersOne() {
        statisticPlayersOne.winner++
    }

    fun addForcedErrorPlayersOne() {
        statisticPlayersOne.forcedError++
    }

    fun addUnforcedErrorPlayersOne() {
        statisticPlayersOne.unforcedError++
    }

    fun addAcePlayersTwo() {
        statisticPlayersTwo.ace++
    }

    fun addDoubleFaultPlayersTwo() {
        statisticPlayersTwo.doubleFault++
    }

    fun addWinnerPlayersTwo() {
        statisticPlayersTwo.winner++
    }

    fun addForcedError() {
        statisticPlayersTwo.forcedError++
    }

    fun addUnforcedErrorPlayersTwo() {
        statisticPlayersTwo.unforcedError++
    }

    fun addSetWonByPlayerOne() {
        _setsWonPlayersOne.value = _setsWonPlayersOne.value?.plus(1)
    }

    fun addSetWonByPlayerTwo() {
        _setsWonPlayersTwo.value = _setsWonPlayersTwo.value?.plus(1)
    }

}