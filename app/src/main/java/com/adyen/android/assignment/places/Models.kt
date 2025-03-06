package com.adyen.android.assignment.places

data class GetVenuesNearbyResponse(
    val categoryNames: List<String>?,
    val distance: Int?,
    val geocode: VenueLatLong?,
    val location: VenueLocation?,
    val name: String?,
    val timezone: String?,
)

data class VenueLocation(
    val address: String?,
    val country: String?,
    val locality: String?,
    val neighbourhood: List<String>?,
    val postcode: String?,
    val region: String?,
)

data class VenueLatLong(
    val latitude: Double?,
    val longitude: Double?,
)