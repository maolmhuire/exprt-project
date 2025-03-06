package com.adyen.android.assignment.api

import com.adyen.android.assignment.api.model.ResponseWrapper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface PlacesService {
    /**
     * Get venue recommendations.
     *
     * See [the docs](https://developer.foursquare.com/reference/places-nearby)
     */
    @GET("places/nearby")
    fun getVenueRecommendations(@QueryMap query: Map<String, String>): Call<ResponseWrapper>

}
