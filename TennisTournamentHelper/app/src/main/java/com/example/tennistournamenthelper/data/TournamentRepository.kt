package com.example.tennistournamenthelper.data

import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.model.Tournament

interface TournamentRepository {
    suspend fun saveFinishedTournament(tournament: Tournament): ResultStatus<Unit>
    suspend fun saveCurrentTournament(tournament: Tournament): ResultStatus<Unit>
    suspend fun deleteFinishedTournament(tournamentId: String): ResultStatus<Unit>
    suspend fun deleteCurrentTournament(tournamentId: String): ResultStatus<Unit>
    suspend fun getAllFinishedTournaments(): ResultStatus<List<Tournament>>
    suspend fun getAllCurrentTournaments(): ResultStatus<List<Tournament>>
    suspend fun getCurrentTournamentById(tournamentId: String): ResultStatus<Tournament?>
    suspend fun getFinishedTournamentById(tournamentId: String): ResultStatus<Tournament?>
}