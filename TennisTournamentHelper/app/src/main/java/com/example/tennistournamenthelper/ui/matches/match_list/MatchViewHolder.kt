package com.example.tennistournamenthelper.ui.matches.match_list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tennistournamenthelper.PlayersNamesStringBuilderHelper
import com.example.tennistournamenthelper.databinding.ItemMatchBinding
import com.example.tennistournamenthelper.model.Match

class MatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = ItemMatchBinding.bind(itemView)
    fun bind(match: Match) {
        binding.apply {
            val players = PlayersNamesStringBuilderHelper().createBothPlayersNamesForDisplay(
                match.playersOne,
                match.playersTwo
            )
            itemMatchPlayers.text = "Players: ${players[0]} vs. ${players[1]}"
            itemMatchRound.text = "Round: ${match.round ?: "/"}"
            itemMatchDate.text = "Date: ${match.date}"
            itemMatchResult.text = "Result: ${match.result.result ?: "Waiting for trail"}"
            itemMatchRules.text =
                "Game: ${match.game.type}, ${match.game.sets} sets, ${match.game.tiebreak}"
            itemMatchTournamentName.text = "Tournament: ${match.tournamentName ?: "/"}"
        }
    }

    fun setColor(color: Int) {
        binding.cvMatch.setCardBackgroundColor(color)
    }
}