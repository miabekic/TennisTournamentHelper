package com.example.tennistournamenthelper.data

import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.model.Gender
import com.example.tennistournamenthelper.model.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PlayerRepositoryImpl(private val database: Database) : PlayerRepository {
    override suspend fun getAllPlayers(tournamentId: String): ResultStatus<List<Player>> =
        withContext(Dispatchers.IO) {
            try {
                val ref =
                    database.databaseInstance.getReference("players/${database.userUid}/$tournamentId")
                val players = ref.get().await()
                val allPlayers = players.children.map {
                    it.getValue(Player::class.java)!!
                }
                ResultStatus.Success(allPlayers)
            } catch (e: Exception) {
                ResultStatus.Failure(e.message.toString())
            }
        }

    override suspend fun getPlayersByGender(
        tournamentId: String,
        gender: Gender
    ): ResultStatus<List<Player>> = withContext(Dispatchers.IO) {
        try {
            val queryPlayers =
                database.databaseInstance.getReference("players/${database.userUid}/$tournamentId")
                    .orderByChild("gender").equalTo(gender.toString())
            val dataSnapshot = queryPlayers.get().await()
            var players: List<Player> = listOf()
            if (dataSnapshot.exists()) {
                players = dataSnapshot.children.map {
                    it.getValue(Player::class.java)!!
                }
            }
            ResultStatus.Success(players)
        } catch (e: Exception) {
            ResultStatus.Failure(e.message.toString())
        }

    }

    override suspend fun savePlayer(player: Player, tournamentId: String): ResultStatus<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val ref =
                    database.databaseInstance.getReference("players/${database.userUid}/$tournamentId")
                        .push()
                player.id = ref.key
                ref.setValue(player).await()
                ResultStatus.Success(Unit)
            } catch (e: Exception) {
                ResultStatus.Failure(e.message.toString())
            }
        }

    override suspend fun deletePlayer(playerId: String, tournamentId: String): ResultStatus<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val ref =
                    database.databaseInstance.getReference("players/${database.userUid}/$tournamentId/$playerId")
                ref.removeValue().await()
                ResultStatus.Success(Unit)
            } catch (e: Exception) {
                ResultStatus.Failure(e.message.toString())
            }
        }

    override suspend fun deletePlayersByTournamentId(tournamentId: String): ResultStatus<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val ref =
                    database.databaseInstance.getReference("players/${database.userUid}/$tournamentId")
                ref.setValue(null)
                ResultStatus.Success(Unit)
            } catch (e: Exception) {
                ResultStatus.Failure(e.message.toString())
            }
        }
}