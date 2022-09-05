package com.example.tennistournamenthelper.ui.tournaments.tournament_list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tennistournamenthelper.databinding.ItemTournamentBinding

import com.example.tennistournamenthelper.model.Tournament

class TournamentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemTournamentBinding.bind(itemView)
    fun bind(tournament: Tournament) {
        binding.apply {
            itemTournamentName.text = "Name: ${tournament.name}"
            itemTournamentDate.text = "Date: ${tournament.startDate} - ${tournament.endDate}"
            itemTournamentAllowedPlayersGender.text = "Players: ${tournament.allowedPlayersGender}"
            itemTournamentSurface.text = "Surface: ${tournament.surface}"
            itemTournamentPlace.text = "Place of helding: ${tournament.place.name}"
        }
    }

    fun setColor(color: Int) {
        binding.cvTournament.setCardBackgroundColor(color)
    }
}