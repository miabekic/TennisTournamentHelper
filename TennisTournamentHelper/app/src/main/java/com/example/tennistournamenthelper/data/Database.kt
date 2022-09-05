package com.example.tennistournamenthelper.data

import com.google.firebase.database.FirebaseDatabase

class Database {
    val databaseInstance = FirebaseDatabase.getInstance()
    val userUid = FirebaseAuthManager().getUserUid()

    init {
        databaseInstance.setPersistenceEnabled(true)
    }

}