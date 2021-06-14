package com.vladislav.magentatest.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vladislav.magentatest.ui.main.FragmentTypes
import com.vladislav.magentatest.ui.main.PlaceholderFragment

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(
    private val activity: FragmentActivity,
    private val tabs: List<FragmentTypes>
) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment =
        when(tabs[position]) {
            FragmentTypes.ALL -> PlaceholderFragment.newInstance(position + 1)
            FragmentTypes.LIKED -> PlaceholderFragment.newInstance(position + 1)
        }

    override fun getItemCount(): Int = tabs.size
}