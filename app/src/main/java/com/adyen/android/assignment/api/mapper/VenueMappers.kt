package com.adyen.android.assignment.api.mapper

import com.adyen.android.assignment.api.model.GeoCode
import com.adyen.android.assignment.api.model.Location
import com.adyen.android.assignment.api.model.Venue
import com.adyen.android.assignment.places.GetVenuesNearbyResponse
import com.adyen.android.assignment.places.VenueLatLong
import com.adyen.android.assignment.places.VenueLocation

fun Venue.toVenueModel(): GetVenuesNearbyResponse {
    return GetVenuesNearbyResponse(
        categoryNames = categories?.map { it.name },
        distance = distance,
        geocode = geocode?.toVenueModel(),
        location = location?.toVenueModel(),
        name = name,
        timezone = timezone
    )
}

fun GeoCode.toVenueModel(): VenueLatLong {
    return VenueLatLong(
        latitude = this.main?.latitude,
        longitude = this.main?.longitude
    )
}

fun Location.toVenueModel(): VenueLocation {
    return VenueLocation(
        address = address,
        country = country,
        locality = locality,
        neighbourhood = neighbourhood,
        postcode = postcode,
        region = region
    )
}