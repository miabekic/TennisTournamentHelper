package com.example.tennistournamenthelper.ui.players.player_list

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tennistournamenthelper.R
import com.example.tennistournamenthelper.model.Gender
import com.example.tennistournamenthelper.model.Player

class PlayerAdapter : RecyclerView.Adapter<PlayerViewHolder>() {
    private val players = mutableListOf<Player>()
    private val searchablePlayers = mutableListOf<Player>()
    var onPlayerEventListener: OnPlayerEventListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_player, parent, false)
        return PlayerViewHolder(view)
    }

    fun setPlayer(player: List<Player>, filterQuery: String) {
        this.players.clear()
        searchablePlayers.clear()
        if (player.isNotEmpty()) {
            searchablePlayers.addAll(player)
            if (filterQuery.isNotEmpty()) {
                filter(searchablePlayers, filterQuery)
            } else this.players.addAll(player)
        }
        notifyDataSetChanged()
    }

    private fun filter(players: List<Player>, filterQuery: String) {
        this.players.clear()
        this.players.addAll(players.filter {
            isPlayerEqualToQuery(it, filterQuery)
        })
    }

    fun filter(filterQuery: String?) {
        if (filterQuery != null) {
            this.players.clear()
            this.players.addAll(searchablePlayers.filter {
                isPlayerEqualToQuery(it, filterQuery)
            })
            notifyDataSetChanged()
        }
    }

    private fun isPlayerEqualToQuery(player: Player, filterQuery: String): Boolean {
        return player.name.contains(filterQuery.lowercase())
    }


    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = players[position]
        when (player.gender) {
            Gender.FEMALE -> holder.setColor(Color.parseColor("#e783e1"))
            Gender.MALE -> holder.setColor(Color.parseColor("#83cde7"))
        }
        holder.bind(player)
        onPlayerEventListener?.let { listener ->
            holder.itemView.setOnLongClickListener { listener.onPlayerLongPressed(player.id!!) }
        }
    }

    override fun getItemCount(): Int = players.count()
}