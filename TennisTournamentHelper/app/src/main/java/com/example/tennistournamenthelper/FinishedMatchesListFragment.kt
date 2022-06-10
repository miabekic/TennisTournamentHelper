package com.example.tennistournamenthelper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tennistournamenthelper.databinding.FragmentFinishedMatchesListBinding
import com.example.tennistournamenthelper.databinding.FragmentTournamentsBinding

class FinishedMatchesListFragment: Fragment() {

    private lateinit var binding: FragmentFinishedMatchesListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinishedMatchesListBinding.inflate(layoutInflater)
        return binding.root
    }
    companion object {
        val Tag = "FinishedMatchesList"

        fun create(): Fragment {
            return FinishedMatchesListFragment()
        }
    }
}