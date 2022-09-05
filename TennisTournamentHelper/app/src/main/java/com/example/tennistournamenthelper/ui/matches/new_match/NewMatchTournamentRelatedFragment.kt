package com.example.tennistournamenthelper.ui.matches.new_match

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tennistournamenthelper.R
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.databinding.FragmentNewMatchTournamentRelatedBinding
import com.example.tennistournamenthelper.model.GameType
import com.example.tennistournamenthelper.presentation.matches.NewMatchTournamentRelatedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewMatchTournamentRelatedFragment : Fragment() {
    private lateinit var binding: FragmentNewMatchTournamentRelatedBinding
    private val viewModel: NewMatchTournamentRelatedViewModel by viewModel()
    private val args: NewMatchTournamentRelatedFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewMatchTournamentRelatedBinding.inflate(layoutInflater)
        binding.apply {
            if (args.isMatchFinished) {
                binding.groupResultNewMatchTournamentRelated.visibility = View.VISIBLE
            }
            swipeRefreshNewMatch.setOnRefreshListener {
                binding.bCreateNewMatchTournamentRelated.isEnabled = false
                viewModel.getTournamentsNames()
            }
            viewModel.tournamentsNames.observe(viewLifecycleOwner) {
                if (swipeRefreshNewMatch.isRefreshing) {
                    swipeRefreshNewMatch.isRefreshing = false
                }
                when (it) {
                    null -> Toast.makeText(
                        context,
                        getString(R.string.check_internet_connection),
                        Toast.LENGTH_LONG
                    ).show()
                    listOf<String>() -> Toast.makeText(
                        context,
                        getString(R.string.no_created_tournaments),
                        Toast.LENGTH_LONG
                    ).show()
                    else -> {
                        setAdapter(it, spinnerTournaments)
                        ArrayAdapter.createFromResource(
                            context!!,
                            R.array.round_array,
                            android.R.layout.simple_spinner_item
                        ).also { adapter ->
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerRounds.adapter = adapter
                        }
                        spinnerTournaments.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    viewModel.setMatchBasicInfo(position)
                                    makeSpinnersGone()
                                    bCreateNewMatchTournamentRelated.isEnabled = false
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                            }
                        spinnerGamesNewMatchTournamentRelated.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    viewModel.setMatchGameRules(
                                        binding.spinnerTournaments.selectedItemPosition,
                                        position
                                    )
                                    when (spinnerGamesNewMatchTournamentRelated.selectedItem.toString()) {
                                        GameType.SINGLES_MALE.toString() -> {
                                            viewModel.setMalePlayers(binding.spinnerTournaments.selectedItemPosition)
                                            viewModel.setType(GameType.SINGLES_MALE)
                                        }
                                        GameType.SINGLES_FEMALE.toString() -> {
                                            viewModel.setFemalePlayers(binding.spinnerTournaments.selectedItemPosition)
                                            viewModel.setType(GameType.SINGLES_FEMALE)
                                        }
                                        GameType.DOUBLES_MALE.toString() -> {
                                            viewModel.setMalePlayers(binding.spinnerTournaments.selectedItemPosition)
                                            viewModel.setType(GameType.DOUBLES_MALE)
                                        }
                                        GameType.DOUBLES_FEMALE.toString() -> {
                                            viewModel.setFemalePlayers(binding.spinnerTournaments.selectedItemPosition)
                                            viewModel.setType(GameType.DOUBLES_FEMALE)
                                        }
                                        GameType.MIXED_DOUBLES.toString() -> {
                                            viewModel.setMalePlayers(binding.spinnerTournaments.selectedItemPosition)
                                            viewModel.setFemalePlayers(binding.spinnerTournaments.selectedItemPosition)
                                            viewModel.setType(GameType.MIXED_DOUBLES)
                                        }
                                    }
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                            }
                    }
                }
            }

            bCreateNewMatchTournamentRelated.setOnClickListener {
                val players = getPlayers()
                viewModel.setPlayers(players[0], players[1])
                if (args.isMatchFinished) {
                    setWinner(players[0], players[1])
                    viewModel.setResult(binding.etResultNewMatchTournamentRelated.text.toString())
                }
                viewModel.setDate(
                    StringBuilder().append(binding.dpDateNewMatchTournamentRelated.dayOfMonth.toString())
                        .append("/")
                        .append((binding.dpDateNewMatchTournamentRelated.month + 1).toString())
                        .append("/")
                        .append(binding.dpDateNewMatchTournamentRelated.year.toString()).toString()
                )
                viewModel.setRound(binding.spinnerRounds.selectedItem.toString())
                viewModel.saveMatch(args.isMatchFinished)
            }
            viewModel.createMatchUiState.observe(viewLifecycleOwner) {
                when (it) {
                    is ResultStatus.Success -> {
                        val action: NavDirections = if (args.isMatchFinished) {
                            NewMatchTournamentRelatedFragmentDirections.actionNewMatchTournamentRelatedFragmentToNavFinishedMatches()
                        } else NewMatchTournamentRelatedFragmentDirections.actionNewMatchTournamentRelatedFragmentToNavTrailMatches()
                        findNavController().navigate(action)
                    }
                    is ResultStatus.Failure -> Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_LONG
                    ).show()
                    is ResultStatus.Loading -> {
                        binding.bCreateNewMatchTournamentRelated.isEnabled = false
                    }
                }
            }

            viewModel.malePlayers.observe(viewLifecycleOwner) {
                when (it) {
                    null -> {
                        makeSpinnersGone()
                        binding.bCreateNewMatchTournamentRelated.isEnabled = false
                        Toast.makeText(
                            context,
                            getString(R.string.no_players_for_selected_tournament_or_lost_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    listOf<String>() -> {
                        makeSpinnersGone()
                        Toast.makeText(
                            context,
                            getString(R.string.no_players_for_selected_tournament),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> {
                        when (binding.spinnerGamesNewMatchTournamentRelated.selectedItem.toString()) {
                            GameType.SINGLES_MALE.toString() -> singles(it)
                            GameType.DOUBLES_MALE.toString() -> doubles(it)
                            GameType.MIXED_DOUBLES.toString() -> {
                                if (isThereEnoughPlayersForMixDoubles(it.count())) {
                                    setAdapter(it, binding.spinnerPlayerOneFirst)
                                    setAdapter(it, binding.spinnerPlayerTwoFirst)
                                    makeSpinnersVisibleDoubles()
                                    binding.bCreateNewMatchTournamentRelated.isEnabled = true
                                } else {
                                    makeSpinnersGone()
                                    Toast.makeText(
                                        context,
                                        getString(R.string.no_enough_male_players),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
            viewModel.femalePlayers.observe(viewLifecycleOwner) {
                when (it) {
                    null -> {
                        makeSpinnersGone()
                        Toast.makeText(
                            context,
                            getString(R.string.check_internet_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    listOf<String>() -> {
                        makeSpinnersGone()
                        Toast.makeText(
                            context,
                            getString(R.string.no_players_for_selected_tournament),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> {
                        when (binding.spinnerGamesNewMatchTournamentRelated.selectedItem.toString()) {
                            GameType.SINGLES_FEMALE.toString() -> singles(it)
                            GameType.DOUBLES_FEMALE.toString() -> doubles(it)
                            GameType.MIXED_DOUBLES.toString() -> {
                                if (isThereEnoughPlayersForMixDoubles(it.count())) {
                                    setAdapter(it, binding.spinnerPlayerTwoSecond)
                                    setAdapter(it, binding.spinnerPlayerOneSecond)
                                } else {
                                    makeSpinnersGone()
                                    Toast.makeText(
                                        context,
                                        getString(R.string.no_enough_female_players),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
            viewModel.games.observe(viewLifecycleOwner) {
                setAdapter(it, binding.spinnerGamesNewMatchTournamentRelated)
            }
            return binding.root
        }
    }

    private fun singles(players: List<String>) {
        if (isThereEnoughPlayersForSingles(players.count())) {
            makeSpinnerVisibleSingles()
            setAdapter(players, binding.spinnerPlayerOneFirst)
            setAdapter(players, binding.spinnerPlayerTwoFirst)
            binding.bCreateNewMatchTournamentRelated.isEnabled = true
        } else {
            makeSpinnersGone()
            Toast.makeText(context, getString(R.string.no_enough_players), Toast.LENGTH_LONG).show()
        }
    }

    private fun doubles(players: List<String>) {
        if (isThereEnoughPlayersForDoubles(players.count())) {
            setAdapter(players, binding.spinnerPlayerOneFirst)
            setAdapter(players, binding.spinnerPlayerOneSecond)
            setAdapter(players, binding.spinnerPlayerTwoFirst)
            setAdapter(players, binding.spinnerPlayerTwoSecond)
            makeSpinnersVisibleDoubles()
            binding.bCreateNewMatchTournamentRelated.isEnabled = true
        } else {
            makeSpinnersGone()
            Toast.makeText(context, getString(R.string.no_enough_players), Toast.LENGTH_LONG).show()
        }
    }

    private fun setAdapter(data: List<String>, spinner: Spinner) {
        val spinnerAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, data)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
    }


    private fun makeSpinnersVisibleDoubles() {
        binding.spinnerPlayerOneFirst.visibility = View.VISIBLE
        binding.spinnerPlayerTwoFirst.visibility = View.VISIBLE
        binding.spinnerPlayerOneSecond.visibility = View.VISIBLE
        binding.spinnerPlayerTwoSecond.visibility = View.VISIBLE
    }

    private fun makeSpinnerVisibleSingles() {
        binding.spinnerPlayerOneFirst.visibility = View.VISIBLE
        binding.spinnerPlayerTwoFirst.visibility = View.VISIBLE
        binding.spinnerPlayerOneSecond.visibility = View.GONE
        binding.spinnerPlayerTwoSecond.visibility = View.GONE
    }

    private fun makeSpinnersGone() {
        binding.spinnerPlayerOneFirst.visibility = View.GONE
        binding.spinnerPlayerTwoFirst.visibility = View.GONE
        binding.spinnerPlayerOneSecond.visibility = View.GONE
        binding.spinnerPlayerTwoSecond.visibility = View.GONE
    }

    private fun setWinner(playersOne: List<String>, playersTwo: List<String>) {
        if (binding.rbWinnerFirstNewMatchTournamentRelated.isChecked) {
            viewModel.setWinner(playersOne)
        } else viewModel.setWinner(playersTwo)
    }

    private fun getPlayers(): List<List<String>> {
        val playersOne: List<String>
        val playersTwo: List<String>
        if (binding.spinnerGamesNewMatchTournamentRelated.selectedItem.toString()
                .contains("Doubles")
        ) {
            playersOne = listOf(
                binding.spinnerPlayerOneFirst.selectedItem.toString(),
                binding.spinnerPlayerOneSecond.selectedItem.toString()
            )
            playersTwo = listOf(
                binding.spinnerPlayerTwoFirst.selectedItem.toString(),
                binding.spinnerPlayerTwoSecond.selectedItem.toString()
            )
        } else {
            playersOne = listOf(binding.spinnerPlayerOneFirst.selectedItem.toString())
            playersTwo = listOf(binding.spinnerPlayerTwoFirst.selectedItem.toString())
        }
        return listOf(playersOne, playersTwo)
    }

    private fun isThereEnoughPlayersForSingles(players: Int): Boolean {
        return players > 1
    }

    private fun isThereEnoughPlayersForDoubles(players: Int): Boolean {
        return players > 3
    }

    private fun isThereEnoughPlayersForMixDoubles(players: Int): Boolean {
        return players > 1
    }
}