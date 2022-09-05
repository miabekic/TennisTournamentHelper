package com.example.tennistournamenthelper.ui.tournaments.new_tournament

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tennistournamenthelper.R
import com.example.tennistournamenthelper.databinding.FragmentNewTournamentMatchesRulesBinding
import com.example.tennistournamenthelper.presentation.tournaments.NewTournamentViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class NewTournamentMatchesRulesFragment : Fragment() {
    private lateinit var binding: FragmentNewTournamentMatchesRulesBinding
    private val viewModelTournament: NewTournamentViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewTournamentMatchesRulesBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            this.viewModel = viewModelTournament
            btnNextNewTournamentMatchRules.setOnClickListener {
                if (viewModelTournament.checkedFemaleDoubles.value == true || viewModelTournament.checkedFemaleSingles.value == true || viewModelTournament.checkedMaleDoubles.value == true
                    || viewModelTournament.checkedMaleSingles.value == true || viewModelTournament.checkedMixedDoubles.value == true
                ) {
                    viewModelTournament.setGamesRules()
                    val action =
                        NewTournamentMatchesRulesFragmentDirections.actionNewTournamentMatchesRulesFragmentToNewTournamentMapsFragment()
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.game_type_must_be_selected),
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
            cbFemaleSingles.setOnClickListener {
                viewModelTournament.setCheckedFemaleSingles(cbFemaleSingles.isChecked)
                viewModelTournament.addRemoveFemaleSingles(cbFemaleSingles.isChecked)
            }
            cbFemaleDoubles.setOnClickListener {
                viewModelTournament.setCheckedFemaleDoubles(cbFemaleDoubles.isChecked)
                viewModelTournament.addRemoveFemaleDoubles(cbFemaleDoubles.isChecked)

            }
            cbMaleDoubles.setOnClickListener {
                viewModelTournament.setCheckedMaleDoubles(cbMaleDoubles.isChecked)
                viewModelTournament.addRemoveMaleDoubles(cbMaleDoubles.isChecked)

            }
            cbMaleSingles.setOnClickListener {
                viewModelTournament.setCheckedMaleSingles(cbMaleSingles.isChecked)
                viewModelTournament.addRemoveMaleSingles(cbMaleSingles.isChecked)
            }
            cbMixedDoubles.setOnClickListener {
                viewModelTournament.setCheckedMixedDoubles(cbMixedDoubles.isChecked)
                viewModelTournament.addRemoveMixedDoubles(cbMixedDoubles.isChecked)
            }

        }

        return binding.root
    }

}