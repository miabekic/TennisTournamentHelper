package com.example.tennistournamenthelper.ui.players.player_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tennistournamenthelper.R
import com.example.tennistournamenthelper.databinding.FragmentPlayersCurrentTournamentListBinding
import com.example.tennistournamenthelper.model.Player
import com.example.tennistournamenthelper.DialogFactory
import org.koin.android.ext.android.inject

class PlayerCurrentTournamentListFragment : BasePlayerListFragment(), OnPlayerEventListener {

    private lateinit var binding: FragmentPlayersCurrentTournamentListBinding
    override val args: PlayerCurrentTournamentListFragmentArgs by navArgs()
    private val dialogFactory: DialogFactory by inject()
    override fun setRecyclerView() {
        binding.rvPlayersCurrentTournament.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        adapter = PlayerAdapter()
        adapter.onPlayerEventListener = this@PlayerCurrentTournamentListFragment
        binding.rvPlayersCurrentTournament.adapter = adapter
    }

    override fun isLayoutRefreshing(): Boolean = binding.srPlayersCurrentTournamentList.isRefreshing

    override fun onSuccessfulDataArrival(players: List<Player>?) {
        players?.let { it ->
            adapter.setPlayer(it, binding.svPlayersCurrentTournamentList.query.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayersCurrentTournamentListBinding.inflate(layoutInflater)
        viewModel.getPlayers(args.tournamentId)
        binding.viewModel = this.viewModel
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            setRecyclerView()
            srPlayersCurrentTournamentList.setOnRefreshListener {
                this@PlayerCurrentTournamentListFragment.viewModel.getPlayers(
                    args.tournamentId
                )
            }
            fabAddPlayer.setOnClickListener {
                val action =
                    PlayerCurrentTournamentListFragmentDirections.actionPlayerListFragmentToNewPlayerFragment(
                        args.tournamentId
                    )
                findNavController().navigate(action)
            }
        }
        observePlayerListUiState()
        viewModel.deleteState.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    viewModel.getPlayers(args.tournamentId)
                    viewModel.setDeleteState(null)
                }
                false -> {
                    Toast.makeText(
                        context,
                        getString(R.string.deletation_faild),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        setOnQueryTextListener(binding.svPlayersCurrentTournamentList)
        return binding.root
    }


    override fun onPlayerLongPressed(playerId: String): Boolean {
        dialogFactory.createDeleteDialog(context!!) {
            viewModel.deletePlayer(playerId, args.tournamentId)
        }.show()
        return true
    }

}