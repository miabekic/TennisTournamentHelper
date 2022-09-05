package com.example.tennistournamenthelper.ui.tournaments.tournament_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.tennistournamenthelper.databinding.FragmentCurrentTournamentListBinding
import com.example.tennistournamenthelper.model.Tournament
import com.example.tennistournamenthelper.presentation.tournaments.CurrentTournamentListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrentTournamentListFragment : BaseTournamentListFragment() {

    private lateinit var binding: FragmentCurrentTournamentListBinding
    override val viewModel: CurrentTournamentListViewModel by viewModel()
    override fun isLayoutRefreshing(): Boolean = binding.srCurrentTournamentList.isRefreshing

    override fun navigateToDetails(tournamentId: String) {
        val action =
            CurrentTournamentListFragmentDirections.actionNavCurrentTournamentsToCurrentTournamentDetailsFragment(
                tournamentId
            )
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getContent()
        binding = FragmentCurrentTournamentListBinding.inflate(layoutInflater)
        binding.apply {
            viewModel = this@CurrentTournamentListFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
            setupRecyclerView(rvCurrentTournament)
            fabAddCurrentTournament.setOnClickListener {
                val action =
                    CurrentTournamentListFragmentDirections.actionNavCurrentTournamentsToNewTournamentFragment()
                findNavController().navigate(action)
            }
            setOnQueryTextListener(svCurrentTournamentList)
        }
        observeListUiState()
        observeDeleteState()
        return binding.root
    }

    override fun onSuccessfulDataArrival(tournaments: List<Tournament>?) {
        tournaments?.let { it ->
            adapter.setTournaments(it, binding.svCurrentTournamentList.query.toString())
        }
    }

}