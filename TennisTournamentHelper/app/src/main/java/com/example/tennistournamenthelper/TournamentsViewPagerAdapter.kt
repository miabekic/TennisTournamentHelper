package com.example.tennistournamenthelper

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TournamentsViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> FinishedTournamentsListFragment()
            1 -> TrailMatchesListFragment()
            else -> FinishedTournamentsListFragment()
        }
    }

}