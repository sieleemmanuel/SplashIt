package com.buildwithsiele.splashit.data.network.monitor

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.net.Socket

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class NetworkStatusHelper(private val context: Context): LiveData<NetworkStatus>() {

    var connectivityManager:ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val validateNetworkConnection = ArrayList<Network>()
    private lateinit var connectivityManagerCallback:ConnectivityManager.NetworkCallback


    override fun onActive() {
        super.onActive()
        connectivityManagerCallback = getConnectivityManagerCallback()
        val networkRequest = NetworkRequest
            .Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, connectivityManagerCallback)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
    }

    private fun announceStatus() {
        if (validateNetworkConnection.isNotEmpty())
            postValue(NetworkStatus.Available)
        else
            postValue(NetworkStatus.UnAvailable)
    }

    private fun getConnectivityManagerCallback() =
    object: ConnectivityManager.NetworkCallback(){

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            val hasNetworkConnection = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                ?:false
            if (hasNetworkConnection) {
                if (InternetAvailability.check()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        validateNetworkConnection.add(network)
                        announceStatus()
                    }
                }
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            validateNetworkConnection.remove(network)
            announceStatus()
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                validateNetworkConnection.add(network)
            } else {
                validateNetworkConnection.remove(network)
            }
            announceStatus()
        }
    }


    object InternetAvailability{
        fun check():Boolean{
            return  try {
                val socket = Socket()
                socket.connect(InetSocketAddress("8.8.8.8", 53))
                socket.close()
                true

            }catch (e:Exception){
                e.printStackTrace()
                false

            }
        }
    }
}