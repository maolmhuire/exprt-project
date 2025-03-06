package com.adyen.android.assignment.ui.compose

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adyen.android.assignment.ui.animation.NavigationAnimations
import com.adyen.android.assignment.ui.navigation.PlacesRootDestinations

interface PlacesRootCallback {
    fun finishActivity()
    fun shouldShowRequestPermissionRationale(permission: String): Boolean
}

@Composable
fun PlacesRoot(
    navController: NavHostController = rememberNavController(),
    callback: PlacesRootCallback,
) {
    NavHost(
        modifier = Modifier
            .imePadding()
            .navigationBarsPadding(),
        navController = navController,
        startDestination = PlacesRootDestinations.VenuesNearbyOverview,
        enterTransition = { NavigationAnimations.enterTransition },
        exitTransition = { NavigationAnimations.exitTransition },
        popEnterTransition = { NavigationAnimations.popEnterTransition },
        popExitTransition = { NavigationAnimations.popExitTransition }
    ) {
        // Main screen (for the moment)
        composable<PlacesRootDestinations.VenuesNearbyOverview> {
            PlacesOverViewScreen(
                callback = callback,
            )
        }
    }
}
