package com.example.tennistournamenthelper.ui.matches.new_match

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tennistournamenthelper.R
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.databinding.FragmentNewMatchTournamentNoRelatedBinding
import com.example.tennistournamenthelper.model.GameType
import com.example.tennistournamenthelper.presentation.matches.NewMatchTournamentNoRelatedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewMatchTournamentNoRelatedFragment : Fragment() {
    private lateinit var binding: FragmentNewMatchTournamentNoRelatedBinding
    private val viewModel: NewMatchTournamentNoRelatedViewModel by viewModel()
    private val args: NewMatchTournamentNoRelatedFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewMatchTournamentNoRelatedBinding.inflate(layoutInflater)
        binding.apply {
            viewModel = this@NewMatchTournamentNoRelatedFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
            if (args.isMatchFinished) {
                binding.groupResultNewMatchTournamentNoRelated.visibility = View.VISIBLE
            }
            val spinnerAdapter = ArrayAdapter(
                context!!,
                android.R.layout.simple_spinner_item,
                listOf(
                    GameType.SINGLES_MALE,
                    GameType.SINGLES_FEMALE,
                    GameType.DOUBLES_MALE,
                    GameType.DOUBLES_FEMALE,
                    GameType.MIXED_DOUBLES
                )
            )
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGamesNewMatchTournamentNoRelated.adapter = spinnerAdapter
        }
        binding.spinnerGamesNewMatchTournamentNoRelated.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (binding.spinnerGamesNewMatchTournamentNoRelated.selectedItem) {
                        GameType.SINGLES_MALE -> {
                            makeEditTextVisibleSingles()
                            viewModel.setType(GameType.SINGLES_MALE)
                        }
                        GameType.SINGLES_FEMALE -> {
                            makeEditTextVisibleSingles()
                            viewModel.setType(GameType.SINGLES_FEMALE)
                        }
                        GameType.DOUBLES_MALE -> {
                            makeEditTextVisibleDoubles()
                            viewModel.setType(GameType.DOUBLES_MALE)
                        }
                        GameType.DOUBLES_FEMALE -> {
                            makeEditTextVisibleDoubles()
                            viewModel.setType(GameType.DOUBLES_FEMALE)
                        }
                        GameType.MIXED_DOUBLES -> {
                            makeEditTextVisibleDoubles()
                            viewModel.setType(GameType.MIXED_DOUBLES)
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }
        binding.rbSinglesSetsThreeNewMatch.setOnClickListener {
            viewModel.setSets(3)
        }
        binding.rbSinglesSetsTwoNewMatch.setOnClickListener {
            viewModel.setSets(2)
        }
        binding.bCreateNewMatchTournamentNoRelated.setOnClickListener {
            val players = getPlayers()
            val emptyPlayers = players.filter {
                it.contains("") || it.isEmpty()
            }
            if (emptyPlayers.isEmpty()) {
                viewModel.setPlayers(players[0], players[1])
                if (args.isMatchFinished) {
                    setWinner(players[0], players[1])
                    viewModel.setResult(binding.etResultNewMatchTournamentNoRelated.text.toString())
                }
                viewModel.setDate(
                    StringBuilder().append(binding.dpDateNewMatchTournamentNoRelated.dayOfMonth.toString())
                        .append("/")
                        .append((binding.dpDateNewMatchTournamentNoRelated.month + 1).toString())
                        .append("/")
                        .append(binding.dpDateNewMatchTournamentNoRelated.year.toString())
                        .toString()
                )
                viewModel.saveMatch(args.isMatchFinished)
            } else Toast.makeText(
                context,
                getString(R.string.missing_players_names),
                Toast.LENGTH_SHORT
            ).show()
        }


        viewModel.createMatchUiState.observe(viewLifecycleOwner) {
            when (it) {
                is ResultStatus.Success -> {
                    val action: NavDirections = if (args.isMatchFinished) {
                        NewMatchTournamentNoRelatedFragmentDirections.actionNewMatchTournamentNoRelatedFragmentToNavFinishedMatches()
                    } else {
                        NewMatchTournamentNoRelatedFragmentDirections.actionNewMatchTournamentNoRelatedFragmentToNavTrailMatches()
                    }
                    findNavController().navigate(action)
                }
                is ResultStatus.Failure -> Toast.makeText(
                    context,
                    it.message,
                    Toast.LENGTH_LONG
                ).show()
                is ResultStatus.Loading -> {}
            }
        }
        return binding.root
    }

    private fun makeEditTextVisibleDoubles() {
        binding.etPlayerOneFirstNewMatch.visibility = View.VISIBLE
        binding.etPlayerOneSecondNewMatch.visibility = View.VISIBLE
        binding.etPlayerTwoFirstNewMatch.visibility = View.VISIBLE
        binding.etPlayerTwoSecondNewMatch.visibility = View.VISIBLE
    }

    private fun makeEditTextVisibleSingles() {
        binding.etPlayerOneFirstNewMatch.visibility = View.VISIBLE
        binding.etPlayerTwoFirstNewMatch.visibility = View.VISIBLE
        binding.etPlayerOneSecondNewMatch.visibility = View.GONE
        binding.etPlayerTwoSecondNewMatch.visibility = View.GONE
    }

    private fun setWinner(playersOne: List<String>, playersTwo: List<String>) {
        if (binding.rbWinnerFirstNewMatchTournamentNoRelated.isChecked) {
            viewModel.setWinner(playersOne)
        } else viewModel.setWinner(playersTwo)
    }

    private fun getPlayers(): List<List<String>> {
        val playersOne: List<String>
        val playersTwo: List<String>
        if (binding.spinnerGamesNewMatchTournamentNoRelated.selectedItem.toString()
                .contains("Doubles")
        ) {
            playersOne = listOf(
                binding.etPlayerOneFirstNewMatch.text.toString(),
                binding.etPlayerOneSecondNewMatch.text.toString()
            )
            playersTwo = listOf(
                binding.etPlayerTwoFirstNewMatch.text.toString(),
                binding.etPlayerTwoSecondNewMatch.text.toString()
            )
        } else {
            playersOne = listOf(binding.etPlayerOneFirstNewMatch.text.toString())
            playersTwo = listOf(binding.etPlayerTwoFirstNewMatch.text.toString())
        }
        return listOf(playersOne, playersTwo)
    }
}