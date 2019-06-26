package com.ventoray.projectmanager.ui.projects

import android.support.v7.widget.SearchView
import org.greenrobot.eventbus.EventBus

class QueryTextListener: SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?): Boolean {
        EventBus.getDefault().post(EBEvent.SearchEvent(query))
        if (query.isNullOrEmpty()) return false
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        EventBus.getDefault().post(EBEvent.SearchEvent(query))
        if (query.isNullOrEmpty()) return false
        return true
    }
}