package com.example.tennistournamenthelper.ui.matches.match_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.tennistournamenthelper.databinding.FragmentTrailMatchListBinding
import com.example.tennistournamenthelper.model.Match
import com.example.tennistournamenthelper.presentation.matches.TrailMatchListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrailMatchesListFragment : BaseMatchListFragment() {


    override val viewModel: TrailMatchListViewModel by viewModel()
    override fun isLayoutRefreshing(): Boolean = binding.srTrailMatchList.isRefreshing
    override fun onSuccessfulDataArrival(matches: List<Match>?) {
        matches?.let { it ->
            adapter.setMatches(it, binding.svTrailMatchList.query.toString())
        }
    }

    override fun navigateToDetails(matchId: String) {
        val action =
            TrailMatchesListFragmentDirections.actionNavTrailMatchesToTrailFragment(matchId)
        findNavController().navigate(action)
    }

    override fun navigateToNewMatchTournamentNoRelated() {
        val action =
            TrailMatchesListFragmentDirections.actionNavTrailMatchesToNewMatchTournamentNoRelatedFragment(
                false
            )
        findNavController().navigate(action)
    }

    override fun navigateToNewMatchTournamentRelated() {
        val action =
            TrailMatchesListFragmentDirections.actionNavTrailMatchesToNewMatchTournamentRelatedFragment(
                false
            )
        findNavController().navigate(action)
    }


    private lateinit var binding: FragmentTrailMatchListBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrailMatchListBinding.inflate(layoutInflater)
        binding.apply {
            viewModel = this@TrailMatchesListFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
            setupRecyclerView(rvTrailMatch)
            setOnClickListenerToAddBtn(fabAddTrailMatch)
            setOnQueryTextListener(svTrailMatchList)
        }
        observeListUiState()
        observeDeleteState()
        return binding.root
    }
}