package com.example.tennistournamenthelper.ui.matches.match_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.tennistournamenthelper.databinding.FragmentFinishedMatchListBinding
import com.example.tennistournamenthelper.model.Match
import com.example.tennistournamenthelper.presentation.matches.FinishedMatchesListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FinishedMatchesListFragment : BaseMatchListFragment() {


    private lateinit var binding: FragmentFinishedMatchListBinding
    override val viewModel: FinishedMatchesListViewModel by viewModel()
    override fun isLayoutRefreshing(): Boolean = binding.srFinishedMatchList.isRefreshing

    override fun navigateToDetails(matchId: String) {
        val action =
            FinishedMatchesListFragmentDirections.actionNavFinishedMatchesToFinishedMatchDetailsFragment(
                matchId
            )
        findNavController().navigate(action)
    }

    override fun navigateToNewMatchTournamentNoRelated() {
        val action =
            FinishedMatchesListFragmentDirections.actionNavFinishedMatchesToNewMatchTournamentNoRelatedFragment(
                true
            )
        findNavController().navigate(action)
    }

    override fun navigateToNewMatchTournamentRelated() {
        val action =
            FinishedMatchesListFragmentDirections.actionNavFinishedMatchesToNewMatchTournamentRelatedFragment(
                true
            )
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getContent()
        binding = FragmentFinishedMatchListBinding.inflate(layoutInflater)
        binding.apply {
            viewModel = this@FinishedMatchesListFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
            setupRecyclerView(rvFinishedMatch)
            setOnClickListenerToAddBtn(fabAddFinishedMatch)
            setOnQueryTextListener(svFinishedMatchList)
        }
        observeListUiState()
        observeDeleteState()
        return binding.root
    }

    override fun onItemSelected(id: String) {
        navigateToDetails(id)
    }

    override fun onSuccessfulDataArrival(matches: List<Match>?) {
        matches?.let { it ->
            adapter.setMatches(it, binding.svFinishedMatchList.query.toString())
        }
    }
}