package com.ventoray.projectmanager.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import com.ventoray.projectmanager.BaseActivity

class NetworkStateReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        val state: Boolean = NetworkUtils().deviceIsConnected(context)
        triggerActivity(context, state)
    }

    private fun triggerActivity(context: Context?, state: Boolean) {
        if (context is BaseActivity) {
            val activity: BaseActivity = context as BaseActivity
            activity.onNetworkStateChange(state)
        }
    }
}

interface NetworkChangeListener {
    abstract fun onNetworkStateChange(connected: Boolean)
}