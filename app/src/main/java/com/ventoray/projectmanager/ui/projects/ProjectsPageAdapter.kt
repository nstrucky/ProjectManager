package com.ventoray.projectmanager.ui.projects

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.ventoray.projectmanager.R

class ProjectsPageAdapter(fm: FragmentManager, context: Context) : FragmentPagerAdapter(fm) {

    private val tabs: Array<String> = context.resources.getStringArray(R.array.projectTabs)
    private val NUM_PAGES: Int = tabs.size

    override fun getItem(position: Int): Fragment {
        return ProjectListFragment.newInstance(position)
    }

    override fun getCount(): Int {
        return NUM_PAGES
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabs[position]
    }



}