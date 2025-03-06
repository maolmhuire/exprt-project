package com.adyen.android.assignment.core

import android.Manifest
import android.annotation.SuppressLint
import android.location.LocationManager
import com.adyen.android.assignment.core.exception.LocationClientException
import com.adyen.android.assignment.core.exception.MissingLocationPermissionException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.jvm.Throws

interface GPSManager {
    @Throws(Exception::class)
    suspend fun getCurrentLocation(): Result<LatLong>
}

class GPSManagerImpl(
    private val locationManager: LocationManager,
    private val fusedLocationClient: FusedLocationProviderClient,
    private val permissionCheckProvider: (permission: String) -> Boolean,
): GPSManager {

    private fun hasLocationPermission(): Boolean =
        checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) &&
                checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)

    private fun checkPermission(permission: String): Boolean = permissionCheckProvider(permission)

    private fun hasGPSProvider(): Boolean =
        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("MissingPermission") // see #hasLocationPermission
    override suspend fun getCurrentLocation(): Result<LatLong> {
        return if (hasLocationPermission() && hasGPSProvider()) {
            suspendCancellableCoroutine { cont ->
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                        override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                            CancellationTokenSource().token

                        override fun isCancellationRequested() = false
                    }
                ).apply {
                    if (isComplete) {
                        if (isSuccessful) {
                            cont.resume(
                                Result.Success(
                                    LatLong(
                                        lat = result.latitude,
                                        long = result.longitude
                                    )
                                ),
                                { err -> throwError(err) }
                            )
                        } else {
                            cont.resume(
                                Result.Error(LocationClientException()),
                                { err -> throwError(err) }
                            )
                        }
                        return@suspendCancellableCoroutine
                    }
                    addOnSuccessListener {
                        if (it != null) {
                            cont.resume(
                                Result.Success(
                                    LatLong(
                                        lat = it.latitude,
                                        long = it.longitude
                                    )
                                ),
                                { err -> throwError(err) }
                            )
                        } else {
                            cont.resume(
                                Result.Error(LocationClientException()),
                                { err -> throwError(err) }
                            )
                        }
                    }
                    addOnFailureListener {
                        cont.resume(
                            Result.Error(LocationClientException()),
                            { err -> throwError(err) }
                        )
                    }
                    addOnCanceledListener {
                        cont.cancel()
                    }
                }
            }
        } else {
            Result.Error(MissingLocationPermissionException())
        }
    }

    private fun throwError(err: Throwable) {
        throw LocationClientException(err.message)
    }
}

data class LatLong(
    val lat: Double = 0.0,
    val long: Double = 0.0
)
