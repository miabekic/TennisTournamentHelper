package com.example.tennistournamenthelper.data

import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.model.Gender
import com.example.tennistournamenthelper.model.Player

interface PlayerRepository {
    suspend fun getAllPlayers(tournamentId: String): ResultStatus<List<Player>>
    suspend fun getPlayersByGender(
        tournamentId: String,
        gender: Gender
    ): ResultStatus<List<Player>>

    suspend fun savePlayer(player: Player, tournamentId: String): ResultStatus<Unit>
    suspend fun deletePlayer(playerId: String, tournamentId: String): ResultStatus<Unit>
    suspend fun deletePlayersByTournamentId(tournamentId: String): ResultStatus<Unit>
}