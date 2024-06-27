package com.farhan.tugasakhir.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

@SuppressLint("StaticFieldLeak")
class MapsViewModel(private val context: Context) : ViewModel() {

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private lateinit var locationCallback: LocationCallback

    private val _hospitalLocations = MutableLiveData<List<LatLng>>()
    val hospitalLocations: LiveData<List<LatLng>> get() = _hospitalLocations
    private val _hospitalNames = MutableLiveData<List<String>>()
    val hospitalNames: LiveData<List<String>> get() = _hospitalNames


    init {
        _hospitalLocations.value = listOf(
            LatLng(5.563600099999999, 95.3702091),
            LatLng(5.5637127, 95.33758759999999),
            LatLng(5.5547341, 95.32455309999999),
            LatLng(5.558538, 95.3176206),
            LatLng(5.549765, 95.3267879),
            LatLng(5.5638464, 95.3699504),
            LatLng(5.529684899999999, 95.3040224),
            LatLng(5.4101833, 95.44751889999999),
            LatLng(5.5340959, 95.3030491),
            LatLng(5.5116833, 95.34409690000001),
            LatLng(5.4657159, 95.38337109999999),
            LatLng(5.568476599999999, 95.3564029),
            LatLng(5.5757692, 95.32517829999999),
            LatLng(5.5127293, 95.32722020000001),
            LatLng(5.4774848, 95.3382831),
            LatLng(5.5961238, 95.37999599999999),
            LatLng(5.476737, 95.239424),
            LatLng(5.563168, 95.32361859999999),
            LatLng(5.5781927, 95.364262),
            LatLng(5.5171412, 95.4134947)
        )
        _hospitalNames.value = listOf(
            "Rumah Sakit Pendidikan Universitas Syiah Kuala",
            "RSUD Dr. Zainoel Abidin",
            "Rumah Sakit Kesdam Iskandar Muda",
            "RSU Meutia",
            "Rumah Sakit Pertamedika Ummi Rosnati",
            "Prince Nayef bin Abdul Aziz Hospital",
            "RS Teungku Fakinah",
            "RS Ibnu Sina",
            "RS Malahayati",
            "RS Hermina Aceh",
            "Puskesmas Suka Makmur",
            "Puskesmas Syiah Kuala",
            "Puskesmas Lampulo",
            "Puskesmas Darul Imarah",
            "Puskesmas Darul Kamal",
            "Puskesmas Baitussalam",
            "Puskesmas Lhoknga",
            "Puskesmas Kuta Alam",
            "Puskesmas Kopelma Darussalam",
            "Puskesmas Blang Bintang"
        )
    }


    fun startLocationUpdates(callback: LocationCallback) {
        locationCallback = callback

        if (isLocationPermissionGranted()) {
            startLocationUpdate()
        }
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun startLocationUpdate() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 3000
            fastestInterval = 1000
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

}