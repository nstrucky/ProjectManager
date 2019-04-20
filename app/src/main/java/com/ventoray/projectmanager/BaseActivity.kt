package com.ventoray.projectmanager

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ventoray.projectmanager.util.NetworkChangeListener
import com.ventoray.projectmanager.util.NetworkStateReceiver

open class BaseActivity : AppCompatActivity(), NetworkChangeListener {

    private var networkStateReceiever: NetworkStateReceiver? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        registerNetworkReceiver()
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkStateReceiever)
    }

    private fun registerNetworkReceiver(): Unit {
        networkStateReceiever = NetworkStateReceiver()
        val intentFilter: IntentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkStateReceiever, intentFilter)
    }

    override fun onNetworkStateChange(connected: Boolean) {
        //Do Nothing
    }
}
