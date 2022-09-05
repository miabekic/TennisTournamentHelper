package com.example.tennistournamenthelper.ui.players.player_list

import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.model.Player
import com.example.tennistournamenthelper.presentation.players.PlayerListViewModel

import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BasePlayerListFragment : Fragment() {
    protected lateinit var adapter: PlayerAdapter
    protected val viewModel: PlayerListViewModel by viewModel()
    protected abstract val args: NavArgs

    protected fun setOnQueryTextListener(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener,
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!isLayoutRefreshing()) {
                    adapter.filter(newText)
                }
                return false
            }

        })
    }

    protected abstract fun setRecyclerView()
    protected abstract fun isLayoutRefreshing(): Boolean

    protected fun observePlayerListUiState() {
        viewModel.playerListUiState.observe(viewLifecycleOwner) {
            when (it) {
                is ResultStatus.Success -> {
                    onSuccessfulDataArrival(it.data)
                }
                is ResultStatus.Loading -> {}
                is ResultStatus.Failure -> Toast.makeText(
                    context,
                    it.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    protected abstract fun onSuccessfulDataArrival(players: List<Player>?)
}