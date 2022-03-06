package com.sofit.test.data.network

interface NetworkAvailability {
    fun onNetworkAvailable()
    fun onNetworkLost()
}