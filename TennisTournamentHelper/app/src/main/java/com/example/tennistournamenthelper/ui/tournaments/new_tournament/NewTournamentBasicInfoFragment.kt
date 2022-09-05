package com.example.tennistournamenthelper.ui.tournaments.new_tournament

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tennistournamenthelper.R
import com.example.tennistournamenthelper.databinding.FragmentNewTournamentBasicInfoBinding
import com.example.tennistournamenthelper.model.PlayersGender
import com.example.tennistournamenthelper.presentation.tournaments.NewTournamentViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class NewTournamentBasicInfoFragment : Fragment() {
    private lateinit var binding: FragmentNewTournamentBasicInfoBinding
    private val viewModelTournaments: NewTournamentViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewTournamentBasicInfoBinding.inflate(layoutInflater)
        binding.apply {
            this.viewModel = viewModelTournaments
            bNextNewTournamentBasicInfo.setOnClickListener {
                if (etNameNewTournament.text.isNotEmpty()) {
                    viewModelTournaments.setBasicInfo(
                        etNameNewTournament.text.toString(),
                        (StringBuilder().append(dpStartDate.dayOfMonth.toString()).append("/")
                            .append((dpStartDate.month + 1).toString()).append("/")
                            .append(dpStartDate.year.toString())).toString(),
                        StringBuilder().append(dpEndDate.dayOfMonth.toString()).append("/")
                            .append((dpEndDate.month + 1).toString()).append("/")
                            .append(dpEndDate.year.toString()).toString()
                    )

                    val action =
                        NewTournamentBasicInfoFragmentDirections.actionNewTournamentBasicInfoFragmentToNewTournamentMatchesRulesFragment()
                    findNavController().navigate(action)
                } else Toast.makeText(context, R.string.missing_tournament_name, Toast.LENGTH_SHORT)
                    .show()
            }
            rbFemaleNewTournament.setOnClickListener {
                if (rbFemaleNewTournament.isChecked) {
                    viewModelTournaments.setPlayersGender(PlayersGender.FEMALE)
                    viewModelTournaments.resetMaleGames()
                    viewModelTournaments.resetMixedDoublesGame()
                }
            }
            rbMaleNewTournament.setOnClickListener {
                if (rbMaleNewTournament.isChecked) {
                    viewModelTournaments.setPlayersGender(PlayersGender.MALE)
                    viewModelTournaments.resetFemaleGames()
                    viewModelTournaments.resetMixedDoublesGame()
                }
            }
        }

        return binding.root
    }

}
