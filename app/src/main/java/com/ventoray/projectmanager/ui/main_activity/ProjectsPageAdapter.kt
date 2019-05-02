package com.ventoray.projectmanager.ui.main_activity

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.ventoray.projectmanager.R
import com.ventoray.projectmanager.ui.main_activity.ProjectsFragment

class ProjectsPageAdapter(fm: FragmentManager, context: Context) : FragmentPagerAdapter(fm) {

    private val tabs: Array<String> = context.resources.getStringArray(R.array.projectTabs)
    private val NUM_PAGES: Int = tabs.size

    override fun getItem(position: Int): Fragment {
        return ProjectsFragment.newInstance(position)
    }

    override fun getCount(): Int {
        return NUM_PAGES
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabs[position]
    }
}