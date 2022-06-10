package com.example.tennistournamenthelper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tennistournamenthelper.databinding.FragmentMatchesBinding
import com.example.tennistournamenthelper.databinding.FragmentTournamentsBinding

class MatchesFragment: Fragment() {
    private lateinit var binding: FragmentMatchesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchesBinding.inflate(layoutInflater)
        return binding.root
    }
    companion object {
        val Tag = "Matches"

        fun create(): Fragment {
            return MatchesFragment()
        }
    }
}