package com.example.tennistournamenthelper.data

import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.model.Match

interface MatchRepository {
    suspend fun getFinishedMatchById(id: String): ResultStatus<Match?>
    suspend fun getTrailMatchById(id: String): ResultStatus<Match>
    suspend fun getAllFinishedMatches(): ResultStatus<List<Match>>
    suspend fun getAllTrailMatches(): ResultStatus<List<Match>>
    suspend fun saveNewTrailMatch(match: Match): ResultStatus<Unit>
    suspend fun saveFinishedMatch(match: Match): ResultStatus<Unit>
    suspend fun saveMatchAfterTrail(match: Match): ResultStatus<Unit>
    suspend fun deleteTrailMatch(matchId: String): ResultStatus<Unit>
    suspend fun deleteFinishedMatch(matchId: String): ResultStatus<Unit>
    suspend fun deleteMatchesByTournamentId(tournamentId: String): ResultStatus<Unit>
}