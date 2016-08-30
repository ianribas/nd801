package com.example.ianribas.mypopularmovies.model;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityManagerDelegate {
    private final ConnectivityManager mConnManager;

    public ConnectivityManagerDelegate(ConnectivityManager cm) {
        mConnManager = cm;
    }

    public boolean isOnline() {
        NetworkInfo netInfo = mConnManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}