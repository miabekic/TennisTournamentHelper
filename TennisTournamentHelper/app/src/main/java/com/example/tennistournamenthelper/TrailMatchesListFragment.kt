package com.example.tennistournamenthelper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tennistournamenthelper.databinding.FragmentTournamentsBinding
import com.example.tennistournamenthelper.databinding.FragmentTrailMatchesListBinding

class TrailMatchesListFragment: Fragment() {
    private lateinit var binding: FragmentTrailMatchesListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrailMatchesListBinding.inflate(layoutInflater)
        return binding.root
    }
    companion object {
        val Tag = "TrailMatchesList"

        fun create(): Fragment {
            return TrailMatchesListFragment()
        }
    }
}