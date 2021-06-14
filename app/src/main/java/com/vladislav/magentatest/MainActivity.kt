package com.vladislav.magentatest

import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.vladislav.magentatest.adapters.SectionsPagerAdapter
import com.vladislav.magentatest.databinding.ActivityMainBinding
import com.vladislav.magentatest.ui.main.FragmentTypes

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAB_TITLES = listOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
        private const val TAG = "Main Activity"
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
            tab.text = resources.getString(TAB_TITLES[pos])
        }.attach()
        val fab: FloatingActionButton = binding.fab

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}