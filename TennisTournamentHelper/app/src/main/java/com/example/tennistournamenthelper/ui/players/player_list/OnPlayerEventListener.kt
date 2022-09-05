package com.example.tennistournamenthelper.ui.players.player_list

interface OnPlayerEventListener {
    fun onPlayerLongPressed(playerId: String): Boolean
}