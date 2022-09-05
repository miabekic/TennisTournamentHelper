package com.example.tennistournamenthelper.ui.players.player_list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tennistournamenthelper.databinding.ItemPlayerBinding
import com.example.tennistournamenthelper.model.Player

class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemPlayerBinding.bind(itemView)
    fun bind(player: Player) {
        binding.apply {
            itemPlayerName.text = player.name
        }
    }

    fun setColor(color: Int) {
        binding.cvPlayer.setCardBackgroundColor(color)
    }
}