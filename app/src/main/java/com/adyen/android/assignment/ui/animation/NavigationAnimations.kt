package com.adyen.android.assignment.ui.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

object NavigationAnimations {

    val enterTransition: EnterTransition = fadeIn(
        animationSpec = tween(300, easing = FastOutSlowInEasing)
    ) + slideInHorizontally(
        initialOffsetX = { 300 },
        animationSpec = tween(400, easing = FastOutSlowInEasing)
    )

    val exitTransition: ExitTransition = fadeOut(
        animationSpec = tween(300, easing = FastOutSlowInEasing)
    ) + slideOutHorizontally(
        targetOffsetX = { -300 },
        animationSpec = tween(400, easing = FastOutSlowInEasing)
    )

    val popEnterTransition: EnterTransition = fadeIn(
        animationSpec = tween(300, easing = FastOutSlowInEasing)
    ) + slideInHorizontally(
        initialOffsetX = { -300 },
        animationSpec = tween(400, easing = FastOutSlowInEasing)
    )

    val popExitTransition: ExitTransition = fadeOut(
        animationSpec = tween(300, easing = FastOutSlowInEasing)
    ) + slideOutHorizontally(
        targetOffsetX = { 300 },
        animationSpec = tween(400, easing = FastOutSlowInEasing)
    )
}
