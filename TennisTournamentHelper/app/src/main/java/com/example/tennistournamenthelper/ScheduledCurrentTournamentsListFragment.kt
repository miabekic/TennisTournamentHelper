package com.example.tennistournamenthelper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tennistournamenthelper.databinding.FragmentScheduledCurrentTournamentsListBinding
import com.example.tennistournamenthelper.databinding.FragmentTrailMatchesListBinding

class ScheduledCurrentTournamentsListFragment: Fragment() {
    private lateinit var binding: FragmentScheduledCurrentTournamentsListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScheduledCurrentTournamentsListBinding.inflate(layoutInflater)
        return binding.root
    }
    companion object {
        val Tag = "ScheduledCurrentTournamentsList"

        fun create(): Fragment {
            return ScheduledCurrentTournamentsListFragment()
        }
    }
}