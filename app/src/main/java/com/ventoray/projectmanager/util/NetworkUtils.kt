package com.ventoray.projectmanager.util

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkInfo

class NetworkUtils {

    fun deviceIsConnected(context: Context?) : Boolean {
        val networkInfo: NetworkInfo? = getNetworkInfo(context)
        networkInfo?.let {
            if (networkInfo.isConnected) return true
        }
        return false
    }

    fun getNetworkInfo(context: Context?): NetworkInfo? {
        val manager: ConnectivityManager = context?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return manager.activeNetworkInfo
    }
}

