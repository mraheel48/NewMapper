package com.example.newmapper

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.*
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object NetworkWorking : ConnectivityManager.NetworkCallback() {

    private val networkLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getNetworkLiveData(context: Context): LiveData<Boolean> {

        networkLiveData.postValue(false)
//        networkLiveData.postValue(context.isNetworkConnected)

        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

//           val builder = NetworkRequest.Builder()
        connectivityManager.registerNetworkCallback(networkRequest, this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val nw = connectivityManager.activeNetwork

            if (nw == null) {
                networkLiveData.postValue(false)
                return networkLiveData
            }

            val actNw = connectivityManager.getNetworkCapabilities(nw)

            if (actNw == null) {
                networkLiveData.postValue(false)
                return networkLiveData
            }

            if (actNw.hasTransport(TRANSPORT_WIFI)) {
                networkLiveData.postValue(true)
                return networkLiveData
            }

            if (actNw.hasTransport(TRANSPORT_ETHERNET)) {
                networkLiveData.postValue(true)
                return networkLiveData
            }

            if (actNw.hasTransport(TRANSPORT_BLUETOOTH)) {
                networkLiveData.postValue(true)
                return networkLiveData
            }

            if (actNw.hasTransport(TRANSPORT_CELLULAR)) {
                networkLiveData.postValue(true)
                return networkLiveData
            }

        } else {

            @Suppress("DEPRECATION")
            (return if (connectivityManager.activeNetworkInfo?.isConnected == true) {
                networkLiveData.postValue(true)
                networkLiveData
            } else {
                networkLiveData.postValue(false)
                networkLiveData
            })

        }

        return networkLiveData
    }

    override fun onAvailable(network: Network) {
        networkLiveData.postValue(true)
    }

    override fun onLost(network: Network) {
        networkLiveData.postValue(false)
    }

    private val Context.isNetworkConnected: Boolean
        get() {

            val manager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                manager.getNetworkCapabilities(manager.activeNetwork)?.let {
                    it.hasTransport(TRANSPORT_WIFI) || it.hasTransport(TRANSPORT_CELLULAR) ||
                            it.hasTransport(TRANSPORT_BLUETOOTH) ||
                            it.hasTransport(TRANSPORT_ETHERNET) ||
                            it.hasTransport(TRANSPORT_VPN)
                } ?: false
            else
                @Suppress("DEPRECATION")
                manager.activeNetworkInfo?.isConnected == true
        }

    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addCapability(NET_CAPABILITY_INTERNET)
        .addTransportType(TRANSPORT_WIFI)
        .addTransportType(TRANSPORT_ETHERNET)
        .addTransportType(TRANSPORT_BLUETOOTH)
        .addTransportType(TRANSPORT_CELLULAR)
        .build()

}