package com.kzcse.cms.features._core

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import com.kzcse.cms.core.language.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
object MonitorConnectivity {

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private var started = false

    fun start(context: Context) {
        if (started) return
        started = true

        val manager =
            context.applicationContext.getSystemService(ConnectivityManager::class.java)
                ?: return

        val callback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                Log.i("NetworkStatus", "Network available: $network")
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                val connected =
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

                _isConnected.value = connected
                Log.i("NetworkStatus", "Internet connectivity changed: $connected")
            }

            override fun onLost(network: Network) {
                _isConnected.value = false
                Log.i("NetworkStatus", "Network lost: $network")
            }
        }

        manager.registerNetworkCallback(
            NetworkRequest.Builder().build(),
            callback
        )
    }
}
