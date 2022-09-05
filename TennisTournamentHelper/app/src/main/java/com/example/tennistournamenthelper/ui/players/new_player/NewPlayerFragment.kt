package com.example.tennistournamenthelper.ui.players.new_player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tennistournamenthelper.R
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.databinding.FragmentNewPlayerBinding
import com.example.tennistournamenthelper.model.Gender
import com.example.tennistournamenthelper.model.PlayersGender
import com.example.tennistournamenthelper.presentation.players.NewPlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewPlayerFragment : Fragment() {
    private lateinit var binding: FragmentNewPlayerBinding
    private val viewModel: NewPlayerViewModel by viewModel()
    private val args: NewPlayerFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getAllowedGenders(args.tournamentId)
        binding = FragmentNewPlayerBinding.inflate(layoutInflater)

        binding.apply {
            viewModel.allowedGenders.observe(viewLifecycleOwner) {
                when (it) {
                    null -> {
                        enableRbGender(false)
                        Toast.makeText(
                            context,
                            getString(R.string.not_able_to_define_allowed_genders),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    PlayersGender.FEMALE -> {
                        rbFemaleNewPlayer.isChecked = true
                        enableRbGender(false)
                        bSavePlayer.isEnabled = true
                    }
                    PlayersGender.MALE -> {
                        rbMaleNewPlayer.isChecked = true
                        enableRbGender(false)
                        bSavePlayer.isEnabled = true
                    }
                    PlayersGender.BOTH -> {
                        enableRbGender(true)
                        bSavePlayer.isEnabled = true
                    }
                }
            }
            bSavePlayer.setOnClickListener {
                if (etNameNewPlayer.text.isNotEmpty()) {
                    if (rbFemaleNewPlayer.isChecked) {
                        viewModel.savePlayer(
                            etNameNewPlayer.text.toString(),
                            Gender.FEMALE,
                            args.tournamentId
                        )
                    } else {
                        viewModel.savePlayer(
                            etNameNewPlayer.text.toString(),
                            Gender.MALE,
                            args.tournamentId
                        )
                    }
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.missing_players_names),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            viewModel.newPlayerUiState.observe(viewLifecycleOwner) {
                when (it) {
                    is ResultStatus.Success -> {
                        val action =
                            NewPlayerFragmentDirections.actionNewPlayerFragmentToPlayerListFragment(
                                args.tournamentId
                            )
                        findNavController().navigate(action)
                    }
                    is ResultStatus.Failure -> {
                        binding.bSavePlayer.isEnabled = true
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                    is ResultStatus.Loading -> {
                        binding.bSavePlayer.isEnabled = false
                    }
                }
            }
        }
        return binding.root
    }

    private fun enableRbGender(enable: Boolean) {
        binding.rbFemaleNewPlayer.isEnabled = enable
        binding.rbMaleNewPlayer.isEnabled = enable
    }
}