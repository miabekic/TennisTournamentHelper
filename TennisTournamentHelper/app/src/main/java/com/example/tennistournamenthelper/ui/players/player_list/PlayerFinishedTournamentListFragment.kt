package com.example.tennistournamenthelper.ui.players.player_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tennistournamenthelper.databinding.FragmentPlayerFinishedTournamentListBinding
import com.example.tennistournamenthelper.model.Player


class PlayerFinishedTournamentListFragment : BasePlayerListFragment() {
    private lateinit var binding: FragmentPlayerFinishedTournamentListBinding
    override val args: PlayerFinishedTournamentListFragmentArgs by navArgs()
    override fun setRecyclerView() {
        binding.rvPlayersFinishedTournament.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        adapter = PlayerAdapter()
        binding.rvPlayersFinishedTournament.adapter = adapter
    }

    override fun isLayoutRefreshing(): Boolean = binding.srPlayerFinishedTournamentList.isRefreshing

    override fun onSuccessfulDataArrival(players: List<Player>?) {
        players?.let { it ->
            adapter.setPlayer(it, binding.svPlayerFinishedTournamentList.query.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerFinishedTournamentListBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = this.viewModel
        viewModel.getPlayers(args.tournamentId)
        setRecyclerView()
        binding.srPlayerFinishedTournamentList.setOnRefreshListener { viewModel.getPlayers(args.tournamentId) }
        observePlayerListUiState()
        setOnQueryTextListener(binding.svPlayerFinishedTournamentList)
        return binding.root
    }


}