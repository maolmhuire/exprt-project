package com.adyen.android.assignment.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
internal sealed class PlacesRootDestinations {

    @Serializable
    data object VenuesNearbyOverview : PlacesRootDestinations()
}