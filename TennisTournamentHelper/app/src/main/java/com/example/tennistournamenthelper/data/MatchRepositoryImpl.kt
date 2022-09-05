package com.example.tennistournamenthelper.data

import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.model.Match
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MatchRepositoryImpl(private val database: Database) : MatchRepository {


    override suspend fun getFinishedMatchById(id: String): ResultStatus<Match?> =
        withContext(Dispatchers.IO) {
            try {
                val ref =
                    database.databaseInstance.getReference("matches/finishedMatches/${database.userUid}/$id")
                val match = ref.get().await()
                val finishedMatch = match.getValue(Match::class.java)
                ResultStatus.Success(finishedMatch)
            } catch (e: Exception) {
                ResultStatus.Failure(e.message.toString())
            }

        }

    override suspend fun getTrailMatchById(id: String): ResultStatus<Match> =
        withContext(Dispatchers.IO) {
            try {
                val ref =
                    database.databaseInstance.getReference("matches/trailMatches/${database.userUid}/$id")
                val match = ref.get().await()
                val trailMatch = match.getValue(Match::class.java)
                ResultStatus.Success(trailMatch)
            } catch (e: Exception) {
                ResultStatus.Failure(e.message.toString())
            }
        }

    override suspend fun getAllFinishedMatches(): ResultStatus<List<Match>> = withContext(
        Dispatchers.IO
    ) {
        try {
            val ref =
                database.databaseInstance.getReference("matches/finishedMatches/${database.userUid}")
            val matches = ref.get().await()
            val finishedMatches = matches.children.map {
                it.getValue(Match::class.java)!!
            }
            ResultStatus.Success(finishedMatches)
        } catch (e: Exception) {
            ResultStatus.Failure(e.message.toString())
        }

    }

    override suspend fun getAllTrailMatches(): ResultStatus<List<Match>> =
        withContext(Dispatchers.IO) {
            try {
                val ref =
                    database.databaseInstance.getReference("matches/trailMatches/${database.userUid}")
                val matches = ref.get().await()
                val trailMatches = matches.children.map {
                    it.getValue(Match::class.java)!!
                }
                ResultStatus.Success(trailMatches)
            } catch (e: Exception) {
                ResultStatus.Failure(e.message.toString())
            }
        }

    override suspend fun saveNewTrailMatch(match: Match): ResultStatus<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val ref =
                    database.databaseInstance.getReference("matches/trailMatches/${database.userUid}")
                        .push()
                match.matchId = ref.key
                ref.setValue(match).await()
                ResultStatus.Success(Unit)
            } catch (e: Exception) {
                ResultStatus.Failure(e.message.toString())
            }
        }

    override suspend fun saveFinishedMatch(match: Match): ResultStatus<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val ref =
                    database.databaseInstance.getReference("matches/finishedMatches/${database.userUid}")
                        .push()
                match.matchId = ref.key
                ref.setValue(match).await()
                ResultStatus.Success(Unit)
            } catch (e: Exception) {
                ResultStatus.Failure(e.message.toString())
            }
        }

    override suspend fun saveMatchAfterTrail(match: Match): ResultStatus<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val ref =
                    database.databaseInstance.getReference("matches/finishedMatches/${database.userUid}")
                deleteTrailMatch(match.matchId!!)
                ref.child(match.matchId!!).setValue(match).await()
                ResultStatus.Success(Unit)
            } catch (e: Exception) {
                ResultStatus.Failure(e.message.toString())
            }
        }

    override suspend fun deleteTrailMatch(matchId: String): ResultStatus<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val ref =
                    database.databaseInstance.getReference("matches/trailMatches/${database.userUid}/$matchId")
                ref.removeValue().await()
                ResultStatus.Success(Unit)
            } catch (e: Exception) {
                ResultStatus.Failure(e.message.toString())
            }
        }

    override suspend fun deleteFinishedMatch(matchId: String): ResultStatus<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val ref =
                    database.databaseInstance.getReference("matches/finishedMatches/${database.userUid}/$matchId")
                ref.removeValue().await()
                ResultStatus.Success(Unit)
            } catch (e: Exception) {
                ResultStatus.Failure(e.message.toString())
            }
        }

    override suspend fun deleteMatchesByTournamentId(tournamentId: String): ResultStatus<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val queryFinishedMatches =
                    database.databaseInstance.getReference("matches/finishedMatches/${database.userUid}")
                        .orderByChild("tournamentId").equalTo("$tournamentId")
                var dataSnapshot = queryFinishedMatches.get().await()
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        database.databaseInstance.getReference("matches/finishedMatches/${database.userUid}")
                            .child(snapshot.key!!).setValue(null).await()
                    }
                }
                val queryTrailMatches =
                    database.databaseInstance.getReference("matches/trailMatches/${database.userUid}")
                        .orderByChild("tournamentId").equalTo("$tournamentId")

                dataSnapshot = queryTrailMatches.get().await()
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        database.databaseInstance.getReference("matches/trailMatches/${database.userUid}")
                            .child(snapshot.key!!).setValue(null).await()
                    }
                }
                ResultStatus.Success(Unit)
            } catch (e: Exception) {
                if (e.message.toString().contains("Index not defined")) {
                    ResultStatus.Success(Unit)
                } else {
                    ResultStatus.Failure(e.message.toString())
                }
            }
        }

}