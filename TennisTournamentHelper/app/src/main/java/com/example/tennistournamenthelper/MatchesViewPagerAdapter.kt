package com.example.tennistournamenthelper

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MatchesViewPagerAdapter(fragment: Fragment):  FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> FinishedMatchesListFragment()
            1 -> TrailMatchesListFragment()
            else -> FinishedMatchesListFragment()
        }
    }

}