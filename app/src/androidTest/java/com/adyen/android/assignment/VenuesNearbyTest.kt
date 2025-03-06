package com.adyen.android.assignment

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.adyen.android.assignment.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class VenuesNearbyTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule(MainActivity::class.java)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun displayResults() {
        val activityScenario: ActivityScenario<MainActivity> =
            ActivityScenario.launch(MainActivity::class.java)
        activityScenario.moveToState(Lifecycle.State.RESUMED)

        val permissionBtn =
            composeTestRule.onNode(
                hasTestTag("action_btn_venues_nearby_card")
            )
        permissionBtn.isDisplayed()
        permissionBtn.performClick()
        requestPermission()

        composeTestRule.waitUntil {
            composeTestRule
                .onNodeWithTag("venues_nearby_overview_loading_spinner")
                .isDisplayed()
        }

        composeTestRule.waitUntil(10_000) {
            composeTestRule
                .onNodeWithTag("venues_collection")
                .isDisplayed()
        }
    }

    private fun requestPermission() {
        InstrumentationRegistry
            .getInstrumentation()
            .uiAutomation
            .grantRuntimePermission(
                BuildConfig.APPLICATION_ID,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        Thread.sleep(500)
    }
}
