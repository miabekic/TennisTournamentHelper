package com.example.tennistournamenthelper.ui.tournaments.tournament_list

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tennistournamenthelper.R
import com.example.tennistournamenthelper.model.Surface
import com.example.tennistournamenthelper.model.Tournament
import com.example.tennistournamenthelper.ui.OnItemEventListener

class TournamentAdapter : RecyclerView.Adapter<TournamentViewHolder>() {
    private val tournaments = mutableListOf<Tournament>()
    private val searchableTournaments: MutableList<Tournament> = mutableListOf()
    var onTournamentSelectedListener: OnItemEventListener? = null

    fun setTournaments(tournaments: List<Tournament>, filterQuery: String) {
        this.tournaments.clear()
        searchableTournaments.clear()
        if (tournaments.isNotEmpty()) {
            searchableTournaments.addAll(tournaments)
            if (filterQuery.isNotEmpty()) {
                filter(searchableTournaments, filterQuery)
            } else this.tournaments.addAll(tournaments)
        }
        notifyDataSetChanged()
    }

    private fun filter(tournaments: List<Tournament>, filterQuery: String) {
        this.tournaments.clear()
        this.tournaments.addAll(tournaments.filter {
            isTournamentEqualToQuery(it, filterQuery)
        })
    }

    fun filter(filterQuery: String?) {
        if (filterQuery != null) {
            this.tournaments.clear()
            this.tournaments.addAll(searchableTournaments.filter {
                isTournamentEqualToQuery(it, filterQuery)
            })
            notifyDataSetChanged()
        }
    }

    private fun isTournamentEqualToQuery(tournament: Tournament, filterQuery: String): Boolean {
        return tournament.name.lowercase().contains(filterQuery)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tournament, parent, false)
        return TournamentViewHolder(view)
    }

    override fun onBindViewHolder(holder: TournamentViewHolder, position: Int) {
        val tournament = tournaments[position]
        when (tournament.surface) {
            Surface.HARD -> holder.setColor(Color.parseColor("#1884F8"))
            Surface.GRASS -> holder.setColor(Color.parseColor("#77D33F"))
            Surface.CLAY -> holder.setColor(Color.parseColor("#ED7B22"))
        }
        holder.bind(tournament)
        onTournamentSelectedListener?.let { listener ->
            holder.itemView.setOnClickListener { listener.onItemSelected(tournament.id!!) }
            holder.itemView.setOnLongClickListener { listener.onItemLongPress(tournament.id!!) }
        }
    }

    override fun getItemCount(): Int = tournaments.count()
}