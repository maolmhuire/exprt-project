package com.adyen.android.assignment.places.di

import com.adyen.android.assignment.core.GPSManager
import com.adyen.android.assignment.places.usecase.GetVenuesNearbyUseCase
import com.adyen.android.assignment.places.repo.PlacesRepository
import com.adyen.android.assignment.places.usecase.GetVenuesNearbyUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlacesModule {

    @Provides
    @Singleton
    fun providesGetVenuesNearbyUseCase(
        gpsManager: GPSManager,
        placesRepo: PlacesRepository
    ): GetVenuesNearbyUseCase =
        GetVenuesNearbyUseCaseImpl(
            gpsManager = gpsManager,
            placesRepository = placesRepo
        )
}
