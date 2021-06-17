package com.vladislav.magentatest.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.vladislav.magentatest.R
import com.vladislav.magentatest.adapters.SectionsPagerAdapter
import com.vladislav.magentatest.databinding.ActivityMainBinding
import com.vladislav.magentatest.ui.fragments.FragmentTypes

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAB_TITLES = listOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
        private const val TAG = "Main Activity"
        const val JPG = ".jpg"
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, listOf(
            FragmentTypes.ALL, FragmentTypes.LIKED
        ))

        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) {tab, pos ->
            Log.d(TAG, "position: $pos")
            Log.d(TAG, resources.configuration.screenWidthDp.toString())
            tab.text = resources.getString(TAB_TITLES[pos])
        }.attach()
    }

}