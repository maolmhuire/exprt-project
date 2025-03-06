package com.adyen.android.assignment.api.repoimpl

import com.adyen.android.assignment.api.PlacesService
import com.adyen.android.assignment.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.api.mapper.toVenueModel
import com.adyen.android.assignment.core.Result
import com.adyen.android.assignment.places.repo.PlacesRepository
import com.adyen.android.assignment.places.GetVenuesNearbyResponse
import com.adyen.android.assignment.places.exception.VenueRecommendationsException
import kotlin.Exception

class PlacesRepositoryImpl(
    private val service: PlacesService
) : PlacesRepository {

    override suspend fun getVenuesNearby(
        lat: Double,
        long: Double
    ): Result<List<GetVenuesNearbyResponse>> {
        val query = VenueRecommendationsQueryBuilder()
            .setLatitudeLongitude(lat, long)
            .build()

        return try {
            val result = service.getVenueRecommendations(query).execute()
            val models = result.body()?.results?.map { it.toVenueModel() } ?: emptyList()
            Result.Success(models)
        } catch (e: Exception) {
            Result.Error(VenueRecommendationsException(e.message))
        }
    }
}
