package dallaz.winline.domain.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dallaz.winline.app.di.AppScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AppScope
class ConnectivityProvider @Inject constructor(context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val status = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onUnavailable() = runBlocking {
                send(ConnectionStatus.Disconnected)
            }

            override fun onAvailable(network: Network) = runBlocking {
                send(ConnectionStatus.Connected)
            }

            override fun onLost(network: Network) = runBlocking {
                send(ConnectionStatus.Disconnected)
            }
        }

        val connected =
            connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
        send(if (connected) ConnectionStatus.Connected else ConnectionStatus.Disconnected)

        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build(), callback
        )
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()
}

sealed class ConnectionStatus {
    object Connected : ConnectionStatus()
    object Disconnected : ConnectionStatus()
}