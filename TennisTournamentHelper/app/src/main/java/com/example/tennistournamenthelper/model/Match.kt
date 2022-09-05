package com.example.tennistournamenthelper.model

data class Match(
    var matchId: String? = null,
    var game: Game = Game(),
    var round: String? = null,
    var surface: Surface = Surface.HARD,
    var date: String = "",
    var playersOne: List<String> = listOf(),
    var playersTwo: List<String> = listOf(),
    var winners: List<String> = listOf(),
    var result: Result = Result(),
    var tournamentId: String? = null,
    var tournamentName: String? = null
)

data class Result(
    var result: String? = null,
    var statistic: List<Statistic>? = null
)

enum class Point {
    LOVE {
        override fun toString(): String {
            return "0"
        }
    },
    FIFTEEN {
        override fun toString(): String {
            return "15"
        }
    },
    THIRTY {
        override fun toString(): String {
            return "30"
        }
    },
    FORTY {
        override fun toString(): String {
            return "40"
        }
    },
    ADVANTAGE {
        override fun toString(): String {
            return "AD"
        }
    }
}

data class Statistic(
    var ace: Int = 0,
    var doubleFault: Int = 0,
    var winner: Int = 0,
    var forcedError: Int = 0,
    var unforcedError: Int = 0
)


