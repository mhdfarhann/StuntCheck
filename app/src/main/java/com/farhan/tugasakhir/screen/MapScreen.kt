package com.farhan.tugasakhir.screen

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.farhan.tugasakhir.components.AppBottomNavigation
import com.farhan.tugasakhir.viewmodel.MapsViewModel
import com.farhan.tugasakhir.viewmodel.MapsViewModelFactory
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "MissingPermission")
@Composable
fun MapScreen(navController: NavController) {
    val context = LocalContext.current
    val factory = MapsViewModelFactory(context)
    val mapsViewModel: MapsViewModel = viewModel(factory = factory)
    val cameraPositionState = rememberCameraPositionState()
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val currentLocation = remember { mutableStateOf(LatLng(0.0, 0.0)) }
    val hospitalLocations by mapsViewModel.hospitalLocations.observeAsState(emptyList())
    val hospitalNames by mapsViewModel.hospitalNames.observeAsState(emptyList())
    val hasCameraMoved = remember { mutableStateOf(false) }
    var lastCameraPosition by remember { mutableStateOf(cameraPositionState.position) }

    when (locationPermissionState.status) {
        PermissionStatus.Granted -> {
            Scaffold(
                bottomBar = {
                    AppBottomNavigation(
                        navController = navController
                    )
                }
            ) { innerPadding ->
                GoogleMap(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = innerPadding.calculateBottomPadding()),
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = true,
                        myLocationButtonEnabled = true,
                        mapToolbarEnabled = true,
                        indoorLevelPickerEnabled = true,
                        compassEnabled = true,
                    ),
                    cameraPositionState = cameraPositionState,
                    onMapLoaded = {
                        hasCameraMoved.value = false
                    },
                    properties = MapProperties(isMyLocationEnabled = true) // Mengaktifkan lapisan lokasi
                ) {
                    LaunchedEffect(cameraPositionState.position) {
                        if (cameraPositionState.position != lastCameraPosition) {
                            hasCameraMoved.value = true
                            lastCameraPosition = cameraPositionState.position
                        }
                    }

                    Marker(
                        state = MarkerState(
                            position = currentLocation.value
                        ),
                        title = "Anda",
                        snippet = "Anda Disini"
                    )

                    hospitalLocations.forEachIndexed { index, location ->
                        key(location) {
                            Marker(
                                state = MarkerState(position = location),
                                title = hospitalNames.getOrElse(index) { "Rumah Sakit / Puskesmas" },
                                snippet = "Lokasi Medis"
                            )
                        }
                    }

                    val callback = remember {
                        object : LocationCallback() {
                            override fun onLocationResult(p0: LocationResult) {
                                val location = p0.lastLocation
                                if (location != null) {
                                    val latLng = LatLng(location.latitude, location.longitude)
                                    currentLocation.value = latLng

                                    if (!hasCameraMoved.value) {
                                        cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                                    }
                                }
                            }
                        }
                    }

                    LaunchedEffect(Unit) {
                        mapsViewModel.startLocationUpdates(callback)
                    }

                    DisposableEffect(Unit) {
                        onDispose {
                            mapsViewModel.stopLocationUpdates()
                        }
                    }
                }
            }
        }
        is PermissionStatus.Denied -> {
            LaunchedEffect(locationPermissionState) {
                locationPermissionState.launchPermissionRequest()
            }
        }
    }
}

