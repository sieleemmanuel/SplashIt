package com.buildwithsiele.splashit.data.network.monitor

sealed class NetworkStatus{
    object Available:NetworkStatus()
    object UnAvailable:NetworkStatus()
}
