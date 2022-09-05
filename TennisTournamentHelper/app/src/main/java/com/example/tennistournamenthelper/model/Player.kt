package com.example.tennistournamenthelper.model

data class Player(
    var id: String? = null,
    var name: String = "",
    var gender: Gender = Gender.FEMALE
)

enum class Gender {
    FEMALE,
    MALE
}

