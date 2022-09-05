package com.example.tennistournamenthelper

class PlayersNamesStringBuilderHelper {
    fun createBothPlayersNamesForDisplay(
        playersOne: List<String>,
        playersTwo: List<String>
    ): List<StringBuilder> {
        return listOf(
            createPlayersNamesForDisplay(playersOne),
            createPlayersNamesForDisplay(playersTwo)
        )
    }

    fun createPlayersNamesForDisplay(players: List<String>): StringBuilder {
        val builder = StringBuilder()
        for (item in players) {
            builder.append(item).append("/")
        }
        builder.setLength(builder.length - 1)
        return builder
    }
}