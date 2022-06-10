package com.example.tennistournamenthelper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tennistournamenthelper.databinding.FragmentTournamentsBinding

class TournamentFragment: Fragment() {

    private lateinit var binding: FragmentTournamentsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTournamentsBinding.inflate(layoutInflater)
        return binding.root
    }
    companion object {
        val Tag = "Tournaments"

        fun create(): Fragment {
            return TournamentFragment()
        }
    }
}