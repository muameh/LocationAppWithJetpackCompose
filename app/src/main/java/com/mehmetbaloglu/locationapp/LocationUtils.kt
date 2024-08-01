package com.mehmetbaloglu.locationapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

class LocationUtils(val context: Context) {

    private val _fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @Suppress("MissingPermission")
    fun requestLocationUpdates(viewModel: LocationViewModel) {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let { location ->
                    val locationData = LocationData(location.latitude, location.longitude)
                    viewModel.updateLocation(locationData)
                }
            }
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 1000).build()

        _fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,Looper.getMainLooper())
    }

    fun hasLocationPermission(context: Context): Boolean {
        //fine location'un ve coarse location'un izinlerini kontrol eder
        // ve her ikisi de varsa true yoksa false döndürür

        var fineLocationPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        var coarseLocationPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fineLocationPermission && coarseLocationPermission
    }

    fun reverseGeocodeLocation(location: LocationData): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val coordinate = LatLng(location.latitude, location.longitude)
        val addresses :MutableList<Address>? =
            geocoder.getFromLocation(coordinate.latitude, coordinate.longitude, 1)

        return if (addresses?.isNotEmpty() == true) {
            addresses[0].getAddressLine(0) ?: "Unknown Location"
        } else {
            "Unknown Location"
        }
    }

}
