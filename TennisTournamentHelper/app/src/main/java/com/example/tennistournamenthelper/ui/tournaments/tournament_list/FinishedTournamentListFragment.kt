package com.example.tennistournamenthelper.ui.tournaments.tournament_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.tennistournamenthelper.databinding.FragmentFinishedTournamentListBinding
import com.example.tennistournamenthelper.model.Tournament
import com.example.tennistournamenthelper.presentation.tournaments.FinishedTournamentListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FinishedTournamentListFragment : BaseTournamentListFragment() {

    private lateinit var binding: FragmentFinishedTournamentListBinding
    override val viewModel: FinishedTournamentListViewModel by viewModel()
    override fun isLayoutRefreshing(): Boolean = binding.srFinishedTournamentList.isRefreshing

    override fun navigateToDetails(tournamentId: String) {
        val action =
            FinishedTournamentListFragmentDirections.actionNavFinishedTournamentsToFinishedTournamentDetailsFragment(
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
        binding = FragmentFinishedTournamentListBinding.inflate(layoutInflater)
        binding.apply {
            viewModel = this@FinishedTournamentListFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
            setupRecyclerView(rvFinishedTournament)
            setOnQueryTextListener(svFinishedTournamentList)
        }
        observeListUiState()
        observeDeleteState()
        return binding.root
    }

    override fun onSuccessfulDataArrival(tournaments: List<Tournament>?) {
        tournaments?.let { it ->
            adapter.setTournaments(it, binding.svFinishedTournamentList.query.toString())
        }
    }
}