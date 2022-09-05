package com.example.tennistournamenthelper.ui.tournaments.tournament_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tennistournamenthelper.R
import com.example.tennistournamenthelper.databinding.FragmentCurrentTournamentDetailsBinding
import com.example.tennistournamenthelper.presentation.tournaments.CurrentTournamentDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrentTournamentDetailsFragment :
    BaseTournamentDetailsFragment<FragmentCurrentTournamentDetailsBinding>() {
    override lateinit var binding: FragmentCurrentTournamentDetailsBinding
    override val args: CurrentTournamentDetailsFragmentArgs by navArgs()
    override fun navigateToPlayersList() {
        val action =
            CurrentTournamentDetailsFragmentDirections.actionCurrentTournamentDetailsFragmentToPlayerListFragment(
                args.tournamentId
            )
        findNavController().navigate(action)
    }

    override val viewModel: CurrentTournamentDetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getDetails(args.tournamentId)
        binding = FragmentCurrentTournamentDetailsBinding.inflate(layoutInflater)
        findMapFragmentView(R.id.map_current_tournament_details)
        setMapFragmentVisibility(View.GONE)

        binding.apply {
            viewModel = this.viewModel
            lifecycleOwner = viewLifecycleOwner
            bSetTournamentAsFinished.setOnClickListener {
                this@CurrentTournamentDetailsFragment.viewModel.setTournamentFinished(this@CurrentTournamentDetailsFragment.viewModel.detailsUiState.value?.data!!)
            }
            addClickListenerOnDisplayPlayersButton(bDisplayPlayersCurrentTournamentDetails)
        }
        observeDetailsUiState()

        viewModel.finishedState.observe(viewLifecycleOwner) {
            when (it) {
                true -> navigateBack()
                false -> Toast.makeText(
                    context,
                    getString(R.string.setting_tournament_to_finished_failed),
                    Toast.LENGTH_LONG
                ).show()
                else -> {}
            }
        }
        return binding.root
    }


    override fun navigateBack() {
        val action =
            CurrentTournamentDetailsFragmentDirections.actionCurrentTournamentDetailsFragmentToNavCurrentTournaments()
        findNavController().navigate(action)
    }

    override fun displayDetails(details: String) {
        binding.tvCurrentTournamentDetails.text = details
    }
}