package com.adyen.android.assignment.places.usecase

import com.adyen.android.assignment.core.GPSManager
import com.adyen.android.assignment.core.Result
import com.adyen.android.assignment.places.GetVenuesNearbyResponse
import com.adyen.android.assignment.places.exception.MissingLocationException
import com.adyen.android.assignment.places.repo.PlacesRepository

interface GetVenuesNearbyUseCase {
    suspend fun invoke(): Result<List<GetVenuesNearbyResponse>>
}

class GetVenuesNearbyUseCaseImpl(
    private val gpsManager: GPSManager,
    private val placesRepository: PlacesRepository,
): GetVenuesNearbyUseCase {
    override suspend fun invoke(): Result<List<GetVenuesNearbyResponse>> {
        return try {
            when (val location = gpsManager.getCurrentLocation()) {
                is Result.Success -> {
                    placesRepository.getVenuesNearby(
                        lat = location.data.lat,
                        long = location.data.long
                    )
                }
                else -> {
                    Result.Error(MissingLocationException())
                }
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
