package com.aungpyaephyo.yoga.admin.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Monitors the network connection status and provides information about the active connection type.
 *
 * **Reference:**
 * [Monitor connectivity status and connection metering](https://developer.android.com/training/monitoring-device-state/connectivity-status-type)
 *
 * @param context The application context.
 */
class ConnectionChecker(
    private val context: Context
) {
    private val connectivityManager by lazy {
        context.getSystemService<ConnectivityManager>()
    }

    private val _isConnectionAvailable = MutableStateFlow<Boolean?>(null)
    val isConnectionAvailable: StateFlow<Boolean?> = _isConnectionAvailable

    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _isConnectionAvailable.tryEmit(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            _isConnectionAvailable.tryEmit(false)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            _isConnectionAvailable.tryEmit(false)
        }
    }

    fun start() {
        _isConnectionAvailable.tryEmit(isNetworkAvailable())
        connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun stop() {
        connectivityManager?.unregisterNetworkCallback(networkCallback)
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = connectivityManager ?: return false
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }
}