package com.arturkida.popularmovieskotlin.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.arturkida.popularmovieskotlin.ui.home.FavoriteFragment
import com.arturkida.popularmovieskotlin.ui.home.PopularFragment

class TabsAdapter(fragmentManager: FragmentManager,
                  private val tabCount: Int) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return PopularFragment()
            else -> return FavoriteFragment()
        }
    }

    override fun getCount(): Int = tabCount
}