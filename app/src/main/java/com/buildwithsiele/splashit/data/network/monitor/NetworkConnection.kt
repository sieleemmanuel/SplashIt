package com.buildwithsiele.splashit.data.network.monitor

import android.content.Context
import android.net.ConnectivityManager

class NetworkConnection(private val context: Context) {
    fun isNetworkAvailable():Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val internetInfo = connectivityManager.activeNetworkInfo
        return internetInfo!=null && internetInfo.isConnected
    }
}