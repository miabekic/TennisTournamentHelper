package com.example.tennistournamenthelper.ui.matches.trail

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
import com.example.tennistournamenthelper.databinding.FragmentTrailBinding
import com.example.tennistournamenthelper.model.Tiebreak
import com.example.tennistournamenthelper.presentation.matches.TrailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrailFragment : Fragment() {
    private lateinit var binding: FragmentTrailBinding
    private val viewModel: TrailViewModel by viewModel()
    private val args: TrailFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getMatch(args.matchId)
        binding = FragmentTrailBinding.inflate(layoutInflater)
        binding.apply {
            viewModel.setupStatus.observe(viewLifecycleOwner) { resultStatus ->
                when (resultStatus) {
                    is ResultStatus.Loading -> {
                        enableButtons(false)
                    }
                    is ResultStatus.Success -> {
                        if (resultStatus.data == null) {
                            Toast.makeText(
                                context,
                                getString(R.string.selected_item_is_probably_deleted),
                                Toast.LENGTH_LONG
                            ).show()
                            val action =
                                TrailFragmentDirections.actionTrailFragmentToNavTrailMatches()
                            findNavController().navigate(action)
                        } else {
                            viewModel.initializeMatch(resultStatus.data)
                            enableButtons(true)
                            val players = PlayersNamesStringBuilderHelper().createBothPlayersNamesForDisplay(
                                viewModel.match.playersOne,
                                viewModel.match.playersTwo
                            )
                            tvPlayersOne.text = "${players[0]}"
                            tvPlayersTwo.text = "${players[1]}"
                            tvPlayersNamesOneResult.text = tvPlayersOne.text
                            tvPlayersNamesTwoResult.text = tvPlayersTwo.text
                        }

                        viewModel.setsWonPlayersOne.observe(viewLifecycleOwner) { wonSets ->
                            viewModel.saveSetResult(
                                viewModel.setsWonPlayersOne.value!!.plus(
                                    viewModel.setsWonPlayersTwo.value!!
                                )
                            )
                            if (wonSets == viewModel.match.game.sets) {
                                viewModel.setMatchFinished()
                            } else {
                                startSet(wonSets.plus(viewModel.setsWonPlayersTwo.value!!))
                            }
                        }
                        viewModel.setsWonPlayersTwo.observe(viewLifecycleOwner) { wonSets ->
                            viewModel.saveSetResult(
                                viewModel.setsWonPlayersOne.value!!.plus(
                                    viewModel.setsWonPlayersTwo.value!!
                                )
                            )
                            if (wonSets == viewModel.match.game.sets) {
                                viewModel.setMatchFinished()
                            } else {
                                startSet(wonSets.plus(viewModel.setsWonPlayersOne.value!!))
                            }
                        }
                        viewModel.tiebreakPointsPlayersOne.observe(viewLifecycleOwner) { tiebreakPoints ->
                            tvGameResultPlayerOne.text = tiebreakPoints.toString()
                            if (viewModel.match.game.tiebreak == Tiebreak.TWELVE_POINTS) {
                                if (tiebreakPoints >= 7 && tiebreakPoints.minus(viewModel.tiebreakPointsPlayersTwo.value!!) >= 2) {
                                    viewModel.addGameToPlayersOne()
                                }
                            } else if (viewModel.match.game.tiebreak == Tiebreak.TEN_POINTS && tiebreakPoints == 10) {
                                viewModel.addGameToPlayersOne()
                            }
                        }
                        viewModel.tiebreakPointsPlayersTwo.observe(viewLifecycleOwner) { tiebreakPoints ->
                            tvGameResultPlayerTwo.text = tiebreakPoints.toString()
                            if (viewModel.match.game.tiebreak == Tiebreak.TWELVE_POINTS) {
                                if (tiebreakPoints >= 7 && tiebreakPoints.minus(viewModel.tiebreakPointsPlayersTwo.value!!) >= 2) {
                                    viewModel.addGameToPlayersTwo()
                                }
                            } else if (viewModel.match.game.tiebreak == Tiebreak.TEN_POINTS && tiebreakPoints == 10) {
                                viewModel.addGameToPlayersTwo()
                            }
                        }
                    }
                    is ResultStatus.Failure -> {
                        Toast.makeText(context, resultStatus.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
            viewModel.trailUiStatus.observe(viewLifecycleOwner) {
                when (it) {
                    is ResultStatus.Loading -> {
                        enableButtons(false)
                        Toast.makeText(
                            context,
                            "Winner is ${
                                PlayersNamesStringBuilderHelper().createPlayersNamesForDisplay(
                                    viewModel.match.winners
                                )
                            }",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is ResultStatus.Success -> {
                        enableButtons(true)
                        val action = TrailFragmentDirections.actionTrailFragmentToNavTrailMatches()
                        findNavController().navigate(action)
                    }
                    is ResultStatus.Failure -> {
                        Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
            bAceFirst.setOnClickListener {
                viewModel.addAcePlayersOne()
                viewModel.changePointsPlayerOne()
            }
            bAceSecond.setOnClickListener {
                viewModel.addAcePlayersTwo()
                viewModel.changePointsPlayerTwo()
            }
            bDoubleFaultFirst.setOnClickListener {
                viewModel.addDoubleFaultPlayersOne()
                viewModel.changePointsPlayerTwo()
            }
            bDoubleFaultSecond.setOnClickListener {
                viewModel.addDoubleFaultPlayersTwo()
                viewModel.changePointsPlayerOne()
            }
            bWinnerFirst.setOnClickListener {
                viewModel.addWinnerPlayersOne()
                viewModel.changePointsPlayerOne()
            }
            bWinnerSecond.setOnClickListener {
                viewModel.addWinnerPlayersTwo()
                viewModel.changePointsPlayerTwo()
            }
            bForcedErrorFirst.setOnClickListener {
                viewModel.addForcedErrorPlayersOne()
                viewModel.changePointsPlayerTwo()
            }
            bForcedErrorSecond.setOnClickListener {
                viewModel.addForcedError()
                viewModel.changePointsPlayerOne()
            }
            bUnforcedErrorFirst.setOnClickListener {
                viewModel.addUnforcedErrorPlayersOne()
                viewModel.changePointsPlayerTwo()
            }
            bUnforcedErrorSecond.setOnClickListener {
                viewModel.addUnforcedErrorPlayersTwo()
                viewModel.changePointsPlayerOne()
            }
            viewModel.firstSetPlayersOne.observe(viewLifecycleOwner) {
                tvFirstSetPlayerOne.text = it.toString()
                if (isSetWonNoTiebreak(it, viewModel.firstSetPlayersTwo.value!!)) {
                    viewModel.addSetWonByPlayerOne()
                }
            }
            viewModel.firstSetPlayersTwo.observe(viewLifecycleOwner) {
                tvFirstSetPlayerTwo.text = it.toString()
                if (isSetWonNoTiebreak(it, viewModel.firstSetPlayersOne.value!!)) {
                    viewModel.addSetWonByPlayerTwo()
                }
            }
            viewModel.secondSetPlayersOne.observe(viewLifecycleOwner) {
                tvSecondSetPlayerOne.text = it.toString()
                if (isSetWonNoTiebreak(it, viewModel.secondSetPlayersTwo.value!!)) {
                    viewModel.addSetWonByPlayerOne()
                }
            }
            viewModel.secondSetPlayersTwo.observe(viewLifecycleOwner) {
                tvSecondSetPlayerTwo.text = it.toString()
                if (isSetWonNoTiebreak(it, viewModel.secondSetPlayersOne.value!!)) {
                    viewModel.addSetWonByPlayerTwo()
                }
            }
            viewModel.thirdSetPlayersOne.observe(viewLifecycleOwner) {
                tvThirdSetPlayerOne.text = it.toString()
                if (isSetWonTieBreakAllowed(it, viewModel.thirdSetPlayersTwo.value!!)) {
                    viewModel.addSetWonByPlayerOne()
                }
            }
            viewModel.thirdSetPlayersTwo.observe(viewLifecycleOwner) {
                tvThirdSetPayerTwo.text = it.toString()
                if (isSetWonTieBreakAllowed(it, viewModel.thirdSetPlayersOne.value!!)) {
                    viewModel.addSetWonByPlayerTwo()
                }
            }
            viewModel.fourthSetPlayersOne.observe(viewLifecycleOwner) {
                tvFourthSetPlayerOne.text = it.toString()
                if (isSetWonNoTiebreak(it, viewModel.fourthSetPlayersTwo.value!!)) {
                    viewModel.addSetWonByPlayerOne()
                }
            }
            viewModel.fourthSetPlayersTwo.observe(viewLifecycleOwner) {
                tvFourthSetPlayerTwo.text = it.toString()
                if (isSetWonNoTiebreak(it, viewModel.fourthSetPlayersOne.value!!)) {
                    viewModel.addSetWonByPlayerTwo()
                }
            }
            viewModel.fifthSetPlayersOne.observe(viewLifecycleOwner) {
                tvFifthSetPlayerOne.text = it.toString()
                if (isSetWonTieBreakAllowed(it, viewModel.fifthSetPlayersTwo.value!!)) {
                    viewModel.addSetWonByPlayerOne()
                }
            }
            viewModel.fifthSetPlayersTwo.observe(viewLifecycleOwner) {
                tvFifthSetPlayerTwo.text = it.toString()
                if (isSetWonTieBreakAllowed(it, viewModel.fifthSetPlayersOne.value!!)) {
                    viewModel.addSetWonByPlayerTwo()
                }
            }
            viewModel.pointsPlayerOne.observe(viewLifecycleOwner) {
                tvGameResultPlayerOne.text = it
            }
            viewModel.pointsPlayerTwo.observe(viewLifecycleOwner) {
                tvGameResultPlayerTwo.text = it
            }
        }
        return binding.root
    }

    private fun isSetWonNoTiebreak(firstPlayerSetsWon: Int, secondPlayerSetsWon: Int): Boolean =
        firstPlayerSetsWon >= 6 && firstPlayerSetsWon.minus(secondPlayerSetsWon) >= 2

    private fun isSetWonTieBreakAllowed(
        firstPlayerSetsWon: Int,
        secondPlayerSetsWon: Int
    ): Boolean = isSetWonNoTiebreak(
        firstPlayerSetsWon,
        secondPlayerSetsWon
    ) || (firstPlayerSetsWon == 7 && viewModel.match.game.tiebreak != Tiebreak.NOT_ALLOWED)

    private fun enableButtons(enable: Boolean) {
        binding.apply {
            bAceFirst.isEnabled = enable
            bAceSecond.isEnabled = enable
            bDoubleFaultFirst.isEnabled = enable
            bDoubleFaultSecond.isEnabled = enable
            bWinnerFirst.isEnabled = enable
            bWinnerSecond.isEnabled = enable
            bForcedErrorFirst.isEnabled = enable
            bForcedErrorSecond.isEnabled = enable
            bUnforcedErrorFirst.isEnabled = enable
            bUnforcedErrorSecond.isEnabled = enable
        }
    }

    private fun startSet(sets: Int) {
        when (sets) {
            1 -> {
                binding.tvSecondSetPlayerOne.visibility = View.VISIBLE
                binding.tvSecondSetPlayerTwo.visibility = View.VISIBLE
            }
            2 -> {
                binding.tvThirdSetPlayerOne.visibility = View.VISIBLE
                binding.tvThirdSetPayerTwo.visibility = View.VISIBLE
            }
            3 -> {
                binding.tvFourthSetPlayerOne.visibility = View.VISIBLE
                binding.tvFourthSetPlayerTwo.visibility = View.VISIBLE
            }
            4 -> {
                binding.tvFifthSetPlayerOne.visibility = View.VISIBLE
                binding.tvFifthSetPlayerTwo.visibility = View.VISIBLE
            }
        }
    }
}