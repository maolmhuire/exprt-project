package com.adyen.android.assignment.core

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun providesFusedLocationProviderClient(app: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(app)
    }

    @Provides
    @Singleton
    fun providesLocationManager(app: Application): LocationManager {
        return app.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @Provides
    @Singleton
    fun providesGPSManager(
        app: Application,
        fusedLocationProviderClient: FusedLocationProviderClient,
        locationManager: LocationManager,
    ): GPSManager =
        GPSManagerImpl(
            fusedLocationClient = fusedLocationProviderClient,
            locationManager = locationManager,
            permissionCheckProvider = { permission ->
                ContextCompat.checkSelfPermission(app, permission) ==
                        PackageManager.PERMISSION_GRANTED
            }
        )
}
