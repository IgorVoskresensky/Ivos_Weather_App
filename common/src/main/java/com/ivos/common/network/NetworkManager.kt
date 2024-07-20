package com.ivos.common.network

import android.net.NetworkCapabilities
import android.os.Build
import com.ivos.common.utils.AppContextProvider
import javax.inject.Inject

class NetworkManager @Inject constructor(private val contextProvider: AppContextProvider) {

    private val cm by lazy {
        contextProvider.getConnectivityManager()
    }

    fun isNetworkAvailable(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            cm.activeNetwork != null && cm.getNetworkCapabilities(cm.activeNetwork) != null
        }
    }
}
