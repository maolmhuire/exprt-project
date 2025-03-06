package com.adyen.android.assignment.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import com.adyen.android.assignment.theme.AdyenTheme
import com.adyen.android.assignment.ui.compose.PlacesRoot
import com.adyen.android.assignment.ui.compose.PlacesRootCallback
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdyenTheme {
                PlacesRoot(
                    callback = object : PlacesRootCallback {
                        override fun finishActivity() {
                            finish()
                        }

                        override fun shouldShowRequestPermissionRationale(permission: String) =
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                this@MainActivity,
                                permission
                            )
                    }
                )
            }
        }
    }
}
