package com.arturkida.popularmovieskotlin.ui.home

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import com.arturkida.popularmovieskotlin.R
import com.arturkida.popularmovieskotlin.adapter.TabsAdapter
import kotlinx.android.synthetic.main.activity_movies.*

class MoviesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        setupTabLayout()
    }

    private fun setupTabLayout() {
        tab_layout.addTab(tab_layout.newTab().setText("Popular"))
        tab_layout.addTab(tab_layout.newTab().setText("Favorites"))

        val tabLayoutAdapter = TabsAdapter(supportFragmentManager, tab_layout.tabCount)

        view_pager.adapter = tabLayoutAdapter

        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    view_pager.currentItem = tab.position
                }
            }
        })
    }
}
