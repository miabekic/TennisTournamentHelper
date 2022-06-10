package com.example.tennistournamenthelper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.tennistournamenthelper.databinding.FragmentMatchesBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vpMatches.adapter=MatchesViewPagerAdapter(this)
        val tabNames= arrayOf("Završeni mečevi", "Mečevi za suženje")
        TabLayoutMediator(binding.tabMatches, binding.vpMatches){ tab, position ->
            tab.text = tabNames[position]
        }.attach()
    }
    companion object {
        val Tag = "Matches"

        fun create(): Fragment {
            return MatchesFragment()
        }
    }
}