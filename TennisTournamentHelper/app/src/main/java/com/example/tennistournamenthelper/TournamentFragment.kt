package com.example.tennistournamenthelper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tennistournamenthelper.databinding.FragmentTournamentsBinding
import com.google.android.material.tabs.TabLayoutMediator

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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vpTournaments.adapter=TournamentsViewPagerAdapter(this)
        val tabNames= arrayOf("Završeni turniri", "Zakazani/trenutni turniri")
        TabLayoutMediator(binding.tabTournaments, binding.vpTournaments){ tab, position ->
            tab.text = tabNames[position]
        }.attach()
    }
    companion object {
        val Tag = "Tournaments"

        fun create(): Fragment {
            return TournamentFragment()
        }
    }
}