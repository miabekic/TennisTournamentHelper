package com.example.tennistournamenthelper.data

import android.util.Log
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.model.Tournament
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TournamentRepositoryImpl(private val database: Database) : TournamentRepository {
    override suspend fun saveFinishedTournament(tournament: Tournament): ResultStatus<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val ref =
                    database.databaseInstance.getReference("tournaments/finishedTournaments/${database.userUid}")
                        .child("${tournament.id}")
                ref.setValue(tournament).await()
                ResultStatus.Success(Unit)
            } catch (e: Throwable) {
                Log.d("Error", e.message.toString())
                ResultStatus.Failure(e.message.toString())
            }
        }


    override suspend fun saveCurrentTournament(tournament: Tournament): ResultStatus<Unit> =
        withContext(
            Dispatchers.IO
        ) {
            try {
                val ref =
                    database.databaseInstance.getReference("tournaments/currentTournaments/${database.userUid}")
                        .push()
                tournament.id = ref.key
                ref.setValue(tournament).await()
                ResultStatus.Success(Unit)
            } catch (e: Exception) {
                ResultStatus.Failure(e.message.toString())
            }
        }


    override suspend fun deleteFinishedTournament(tournamentId: String): ResultStatus<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val ref =
                    database.databaseInstance.getReference("tournaments/finishedTournaments/${database.userUid}/$tournamentId")
                ref.removeValue().await()
                ResultStatus.Success(Unit)
            } catch (e: Exception) {
                ResultStatus.Failure(e.message.toString())
            }

        }


    override suspend fun deleteCurrentTournament(tournamentId: String): ResultStatus<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val ref =
                    database.databaseInstance.getReference("tournaments/currentTournaments/${database.userUid}/$tournamentId")
                ref.removeValue().await()
                ResultStatus.Success(Unit)
            } catch (e: Exception) {
                ResultStatus.Failure(e.message.toString())
            }
        }


    override suspend fun getAllFinishedTournaments(): ResultStatus<List<Tournament>> = withContext(
        Dispatchers.IO
    ) {
        try {
            val ref =
                database.databaseInstance.getReference("tournaments/finishedTournaments/${database.userUid}")
            val tournaments = ref.get().await()
            val finishedTournaments = tournaments.children.map {
                it.getValue(Tournament::class.java)!!
            }
            ResultStatus.Success(finishedTournaments)
        } catch (e: Exception) {
            ResultStatus.Failure(e.message.toString())
        }

    }

    override suspend fun getAllCurrentTournaments(): ResultStatus<List<Tournament>> = withContext(
        Dispatchers.IO
    ) {
        try {
            val ref =
                database.databaseInstance.getReference("tournaments/currentTournaments/${database.userUid}")
            val tournaments = ref.get().await()
            val scheduledCurrentTournaments = tournaments.children.map {
                it.getValue(Tournament::class.java)!!
            }
            ResultStatus.Success(scheduledCurrentTournaments)
        } catch (t: Throwable) {
            Log.d("Error", t.message.toString())
            ResultStatus.Failure(t.message.toString())
        }


    }

    override suspend fun getCurrentTournamentById(tournamentId: String): ResultStatus<Tournament?> =
        withContext(
            Dispatchers.IO
        ) {
            try {
                val ref =
                    database.databaseInstance.getReference("tournaments/currentTournaments/${database.userUid}/$tournamentId")
                val tournament = ref.get().await()
                val currentTournament = tournament.getValue(Tournament::class.java)
                ResultStatus.Success(currentTournament)
            } catch (e: Exception) {
                ResultStatus.Failure(e.message.toString())
            }

        }

    override suspend fun getFinishedTournamentById(tournamentId: String): ResultStatus<Tournament?> =
        withContext(
            Dispatchers.IO
        ) {
            try {
                val ref =
                    database.databaseInstance.getReference("tournaments/finishedTournaments/${database.userUid}/$tournamentId")
                val tournament = ref.get().await()
                val finishedTournament = tournament.getValue(Tournament::class.java)
                ResultStatus.Success(finishedTournament)
            } catch (e: Exception) {
                ResultStatus.Failure(e.message.toString())
            }

        }

}