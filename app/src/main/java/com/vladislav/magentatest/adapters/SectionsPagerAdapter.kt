package com.vladislav.magentatest.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vladislav.magentatest.ui.fragments.FavouritesFragment
import com.vladislav.magentatest.ui.fragments.FragmentTypes
import com.vladislav.magentatest.ui.fragments.ImagesFragment

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(
    activity: FragmentActivity,
    private val tabs: List<FragmentTypes>
) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment =
        when(tabs[position]) {
            FragmentTypes.ALL -> ImagesFragment.newInstance()
            FragmentTypes.LIKED -> FavouritesFragment.newInstance()
        }

    override fun getItemCount(): Int = tabs.size
}