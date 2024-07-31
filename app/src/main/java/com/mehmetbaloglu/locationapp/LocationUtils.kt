package com.mehmetbaloglu.locationapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class LocationUtils(val context: Context) {


    fun hasLocationPermission(context: Context): Boolean {
        //fine location'un ve coarse location'un izinlerini kontrol eder
        // ve her ikisi de varsa true yoksa false döndürür

        var fineLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        var coarseLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fineLocationPermission && coarseLocationPermission
    }
}
