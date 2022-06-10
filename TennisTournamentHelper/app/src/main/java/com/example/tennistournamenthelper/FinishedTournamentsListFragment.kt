package com.example.tennistournamenthelper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tennistournamenthelper.databinding.FragmentFinisedTournamentsListBinding
import com.example.tennistournamenthelper.databinding.FragmentTrailMatchesListBinding

class FinishedTournamentsListFragment: Fragment() {
    private lateinit var binding: FragmentFinisedTournamentsListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinisedTournamentsListBinding.inflate(layoutInflater)
        return binding.root
    }
    companion object {
        val Tag = "FinishedTournamentsList"

        fun create(): Fragment {
            return FinishedTournamentsListFragment()
        }
    }
}