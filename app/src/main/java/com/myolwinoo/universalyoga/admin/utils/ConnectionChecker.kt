package com.myolwinoo.universalyoga.admin.utils

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

    /**
     * A [StateFlow] that emits the current network connection status.
     *
     * The flow emits `true` when a network connection is available, `false` when there is no
     * connection, and `null` when the connection status is unknown.
     */
    private val _isConnectionAvailable = MutableStateFlow<Boolean?>(null)
    val isConnectionAvailable: StateFlow<Boolean?> = _isConnectionAvailable

    /**
     * A [NetworkRequest] that specifies the desired network capabilities.
     *
     * This request is used to register the network callback with the connectivity manager.
     */
    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    /**
     * A [ConnectivityManager.NetworkCallback] that listens for network changes.
     *
     * This callback updates the `_isConnectionAvailable` flow when the network connection
     * status changes.
     */
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        /**
         * Called when a network connection is available.
         */
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _isConnectionAvailable.tryEmit(true)
        }

        /**
         * Called when a network connection is lost.
         */
        override fun onLost(network: Network) {
            super.onLost(network)
            _isConnectionAvailable.tryEmit(false)
        }

        /**
         * Called when a network connection is unavailable.
         */
        override fun onUnavailable() {
            super.onUnavailable()
            _isConnectionAvailable.tryEmit(false)
        }
    }

    /**
     * Starts monitoring the network connection status.
     *
     * This method registers the network callback with the connectivity manager and emits the
     * initial connection status.
     */
    fun start() {
        _isConnectionAvailable.tryEmit(isNetworkAvailable())
        connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)
    }

    /**
     * Stops monitoring the network connection status.
     *
     * This method unregisters the network callback from the connectivity manager.
     */
    fun stop() {
        connectivityManager?.unregisterNetworkCallback(networkCallback)
    }

    /**
     * Checks if a network connection is currently available.
     *
     * @return `true` if a network connection is available, `false` otherwise.
     */
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = connectivityManager ?: return false
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }
}