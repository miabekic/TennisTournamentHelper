package com.example.tennistournamenthelper.ui.matches.new_match

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tennistournamenthelper.R
import com.example.tennistournamenthelper.model.Match
import com.example.tennistournamenthelper.ui.OnItemEventListener
import com.example.tennistournamenthelper.ui.matches.match_list.MatchViewHolder

class MatchAdapter : RecyclerView.Adapter<MatchViewHolder>() {
    private val matches = mutableListOf<Match>()
    private val searchableMatches = mutableListOf<Match>()
    var onMatchEventListener: OnItemEventListener? = null

    fun setMatches(matches: List<Match>, filterQuery: String) {
        this.matches.clear()
        searchableMatches.clear()
        if (matches.isNotEmpty()) {
            searchableMatches.addAll(matches)
            if (filterQuery.isNotEmpty()) {
                filter(searchableMatches, filterQuery)
            } else this.matches.addAll(matches)
        }
        notifyDataSetChanged()
    }

    private fun filter(matches: List<Match>, filterQuery: String) {
        this.matches.clear()
        this.matches.addAll(matches.filter {
            isMatchEqualToQuery(it, filterQuery)
        })
    }

    fun filter(filterQuery: String?) {
        if (filterQuery != null) {
            this.matches.clear()
            this.matches.addAll(searchableMatches.filter {
                isMatchEqualToQuery(it, filterQuery)
            })
            notifyDataSetChanged()
        }
    }

    private fun isMatchEqualToQuery(match: Match, filterQuery: String): Boolean {
        return if (match.playersOne.size > 1) {
            if (match.playersTwo.size > 1) {
                match.tournamentName?.lowercase()
                    ?.contains(filterQuery.lowercase()) == true || match.playersOne[0].contains(
                    filterQuery.lowercase()
                ) || match.playersOne[1].contains(filterQuery.lowercase()) || match.playersTwo[0].contains(
                    filterQuery.lowercase()
                ) || match.playersTwo[1].contains(filterQuery.lowercase())
            } else {
                match.tournamentName?.lowercase()
                    ?.contains(filterQuery.lowercase()) == true || match.playersOne[0].contains(
                    filterQuery.lowercase()
                ) || match.playersOne[1].contains(filterQuery.lowercase()) || match.playersTwo[0].contains(
                    filterQuery.lowercase()
                )
            }
        } else {
            if (match.playersTwo.size > 1) {
                match.tournamentName?.lowercase()
                    ?.contains(filterQuery.lowercase()) == true || match.playersOne[0].contains(
                    filterQuery.lowercase()
                ) || match.playersTwo[0].contains(filterQuery.lowercase()) || match.playersTwo[1].contains(
                    filterQuery.lowercase()
                )
            } else {
                match.tournamentName?.lowercase()
                    ?.contains(filterQuery.lowercase()) == true || match.playersOne[0].contains(
                    filterQuery.lowercase()
                ) || match.playersTwo[0].contains(filterQuery.lowercase())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_match, parent, false)
        return MatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val match = matches[position]
        holder.bind(match)
        if (!match.tournamentName.isNullOrEmpty()) {
            holder.setColor(Color.YELLOW)
        } else holder.setColor(Color.LTGRAY)
        onMatchEventListener?.let { listener ->
            holder.itemView.setOnClickListener { listener.onItemSelected(match.matchId!!) }
            holder.itemView.setOnLongClickListener { listener.onItemLongPress(match.matchId!!) }
        }
    }

    override fun getItemCount(): Int = matches.count()
}