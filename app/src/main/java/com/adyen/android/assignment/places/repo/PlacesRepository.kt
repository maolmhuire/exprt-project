package com.adyen.android.assignment.places.repo

import com.adyen.android.assignment.core.Result
import com.adyen.android.assignment.places.GetVenuesNearbyResponse

interface PlacesRepository {
    suspend fun getVenuesNearby(lat: Double, long: Double): Result<List<GetVenuesNearbyResponse>>
}
