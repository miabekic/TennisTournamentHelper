package com.example.tennistournamenthelper.model


data class Tournament(
    var id: String? = null,
    var name: String = "",
    var startDate: String = "",
    var endDate: String = "",
    var place: Place = Place(),
    var allowedPlayersGender: PlayersGender = PlayersGender.MALE,
    var games: List<Game> = listOf(),
    var surface: Surface = Surface.HARD
)

data class Game(
    var type: GameType = GameType.SINGLES_MALE,
    var sets: Int = 2,
    var tiebreak: Tiebreak = Tiebreak.TWELVE_POINTS
)

enum class Tiebreak {
    NOT_ALLOWED {
        override fun toString(): String {
            return "Not allowed"
        }
    },
    TWELVE_POINTS {
        override fun toString(): String {
            return "12 points tiebreak"
        }
    },
    TEN_POINTS {
        override fun toString(): String {
            return "10 points tiebreak"
        }
    }
}

enum class PlayersGender {
    FEMALE {
        override fun toString(): String {
            return "Female"
        }
    },
    MALE {
        override fun toString(): String {
            return "Male"
        }
    },
    BOTH {
        override fun toString(): String {
            return "Female and male"
        }
    }
}

data class Place(
    var name: String = "",
    var location: Location = Location()
)

data class Location(
    var latitude: Double? = null,
    var longitude: Double? = null
)

enum class GameType {
    SINGLES_FEMALE {
        override fun toString(): String {
            return "Female Singles"
        }
    },
    SINGLES_MALE {
        override fun toString(): String {
            return "Male Singles"
        }
    },
    DOUBLES_FEMALE {
        override fun toString(): String {
            return "Female Doubles"
        }
    },
    DOUBLES_MALE {
        override fun toString(): String {
            return "Male Doubles"
        }
    },
    MIXED_DOUBLES {
        override fun toString(): String {
            return "Mixed Doubles"
        }
    }
}

enum class Surface {
    HARD {
        override fun toString(): String {
            return "Hard"
        }
    },
    GRASS {
        override fun toString(): String {
            return "Grass"
        }
    },
    CLAY {
        override fun toString(): String {
            return "Clay"
        }
    }
}
