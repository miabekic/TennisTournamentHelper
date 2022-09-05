package com.example.tennistournamenthelper.ui.matches.match_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tennistournamenthelper.PlayersNamesStringBuilderHelper
import com.example.tennistournamenthelper.R
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.databinding.FragmentFinishedMatchDetailsBinding
import com.example.tennistournamenthelper.model.Match
import com.example.tennistournamenthelper.model.Statistic
import com.example.tennistournamenthelper.presentation.matches.FinishedMatchDetailsViewModel
import com.example.tennistournamenthelper.DialogFactory
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FinishedMatchDetailsFragment : Fragment() {

    private lateinit var binding: FragmentFinishedMatchDetailsBinding
    private val viewModel: FinishedMatchDetailsViewModel by viewModel()
    private val args: FinishedMatchDetailsFragmentArgs by navArgs()
    private val dialogFactory: DialogFactory by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getDetails(args.matchId)
        binding = FragmentFinishedMatchDetailsBinding.inflate(layoutInflater)
        viewModel.detailsUiState.observe(viewLifecycleOwner) {
            when (it) {
                is ResultStatus.Loading -> {
                    Toast.makeText(context, getString(R.string.loading_details), Toast.LENGTH_SHORT)
                        .show()
                }
                is ResultStatus.Success -> {
                    display(it.data)
                }
                is ResultStatus.Failure -> Toast.makeText(
                    context,
                    it.message.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        return binding.root
    }

    private fun display(match: Match?) {
        match?.let {
            val matchDetails = StringBuilder().append("Players: ")
            val playersOneStatistic = StringBuilder()
            val playersTwoStatistic = StringBuilder()
            val players = PlayersNamesStringBuilderHelper().createBothPlayersNamesForDisplay(
                match.playersOne,
                match.playersTwo
            )
            matchDetails.append("${players[0]} vs. ${players[1]}")
            playersOneStatistic.append("${players[0]}")
            playersTwoStatistic.append("${players[1]}")
            matchDetails.append(
                "\nRound: ${it.round ?: "/"}\nResult: ${it.result.result}" +
                        "\nDate: ${it.date}\nMatch rules: ${it.game.type}, ${it.game.sets} sets, ${it.game.tiebreak}" +
                        "\nSurface: ${it.surface}\nTournament: ${it.tournamentName ?: "/"}\n"
            )
            binding.tvFinishedMatchDetails.text = matchDetails
            if (it.result.statistic?.get(0) != null) {
                binding.tvStatisticPlayerOne.text = playersOneStatistic.append(
                    buildStatisticString(
                        it.result.statistic!![0]
                    )
                )
                binding.tvStatisticPlayerTwo.text = playersTwoStatistic.append(
                    buildStatisticString(
                        it.result.statistic!![1]
                    )
                )
            }
        } ?: dialogFactory.createNoSuchItemDialog(context!!) {
            val action =
                FinishedMatchDetailsFragmentDirections.actionFinishedMatchDetailsFragmentToNavFinishedMatches()
            findNavController().navigate(action)
        }.show()
    }

    private fun buildStatisticString(statistic: Statistic): String {
        return "\nAce: ${statistic.ace}\nDouble Fault: ${statistic.doubleFault}\nWinner: ${statistic.winner}\nUnforced Error: ${statistic.unforcedError}\nForced Error: ${statistic.forcedError}"
    }
}