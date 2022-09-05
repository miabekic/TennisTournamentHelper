package com.example.tennistournamenthelper.ui.tournaments.tournament_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tennistournamenthelper.R
import com.example.tennistournamenthelper.databinding.FragmentFinishedTournamentDetailsBinding
import com.example.tennistournamenthelper.presentation.tournaments.FinishedTournamentDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FinishedTournamentDetailsFragment :
    BaseTournamentDetailsFragment<FragmentFinishedTournamentDetailsBinding>() {
    override lateinit var binding: FragmentFinishedTournamentDetailsBinding
    override val viewModel: FinishedTournamentDetailsViewModel by viewModel()
    override val args: FinishedTournamentDetailsFragmentArgs by navArgs()
    override fun navigateToPlayersList() {
        val action =
            FinishedTournamentDetailsFragmentDirections.actionFinishedTournamentDetailsFragmentToPlayerFinishedTournamentListFragment(
                args.tournamentId
            )
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getDetails(args.tournamentId)
        binding = FragmentFinishedTournamentDetailsBinding.inflate(layoutInflater)
        findMapFragmentView(R.id.map_finished_tournament_details)
        setMapFragmentVisibility(View.GONE)
        binding.apply {
            viewModel = this@FinishedTournamentDetailsFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        addClickListenerOnDisplayPlayersButton(binding.bDisplayPlayersFinishedTournamentDetails)
        observeDetailsUiState()
        return binding.root
    }

    override fun displayDetails(details: String) {
        binding.tvFinishedTournamentDetails.text = details
    }

    override fun navigateBack() {
        val action =
            FinishedTournamentDetailsFragmentDirections.actionFinishedTournamentDetailsFragmentToNavFinishedTournaments()
        findNavController().navigate(action)
    }

}