package com.adyen.android.assignment.ui.compose

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.adyen.android.assignment.R
import com.adyen.android.assignment.core.Result
import com.adyen.android.assignment.places.GetVenuesNearbyResponse
import com.adyen.android.assignment.ui.goToSettings
import com.adyen.android.assignment.ui.hasGrantedPermission
import com.adyen.android.assignment.ui.viewmodel.PlacesOverviewViewModel
import kotlinx.coroutines.launch

private val permissions = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesOverViewScreen(
    placesOverviewViewModel: PlacesOverviewViewModel = hiltViewModel(),
    callback: PlacesRootCallback,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val interactionSource = remember { MutableInteractionSource() }
    val hasPermissions = remember { mutableStateOf(checkPermissions(context)) }

    val request = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        val isGranted = permissions.values.all { it }
        hasPermissions.value = isGranted
        if (isGranted) {
            placesOverviewViewModel.retrieveVenuesNearby()
        } else {
            coroutineScope.launch {
                if (shouldShowRequestPermissionRationale(callback)){
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.location_permission_denied)
                    )
                } else {
                    val result = snackbarHostState.showSnackbar(
                        message = context.getString(R.string.location_permission_denied_prompt_end),
                        actionLabel = context.getString(R.string.go_to_settings_cta),
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        context.goToSettings()
                    }
                }
            }
        }
    }

    val state = placesOverviewViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                },
                navigationIcon = {
                    Image(
                        modifier =
                        Modifier
                            .padding(6.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = ripple(),
                                onClick = { callback.finishActivity() }
                            ),
                        painter = painterResource(R.drawable.close_white_24),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
                        contentDescription = null
                    )
                },
                actions = {
                    Text(
                        modifier =
                        Modifier
                            .padding(6.dp)
                            .clickable {
                                if (hasPermissions.value) {
                                    placesOverviewViewModel.retrieveVenuesNearby()
                                } else {
                                    request.launch(permissions)
                                }
                            },
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        text = stringResource(R.string.refresh_cta)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (val result = state.value) {
                is Result.Success -> {
                    VenuesCollection(result.data)
                }
                is Result.Error -> {
                    MessageCard(
                        modifier = Modifier.padding(top = 120.dp),
                        title = stringResource(R.string.error_title),
                        label = result.throwable.message ?: stringResource(R.string.unknown_error),
                        btnText = stringResource(R.string.retry_action),
                        onAction = {
                            if (hasPermissions.value) {
                                placesOverviewViewModel.retrieveVenuesNearby()
                            } else {
                                request.launch(permissions)
                            }
                        }
                    )
                }
                is Result.Idle -> {
                    if (hasPermissions.value) {
                        MessageCard(
                            modifier = Modifier.padding(top = 120.dp),
                            icon = R.drawable.baseline_arrow_outward_24,
                            title = stringResource(R.string.welcome_title),
                            label = stringResource(R.string.location_accepted_caption),
                            btnText = null,
                            onAction = null
                        )
                    } else {
                        MessageCard(
                            modifier = Modifier.padding(top = 120.dp),
                            icon = R.drawable.baseline_question_mark_24,
                            title = stringResource(R.string.permissions_title),
                            label = stringResource(R.string.permissions_request_text),
                            btnText = stringResource(R.string.give_location_cta),
                            onAction = {
                                request.launch(permissions)
                            }
                        )
                    }
                }
                Result.Loading -> {
                    LoadingSpinner()
                }
            }
        }
    }
}

@Composable
private fun VenuesCollection(result: List<GetVenuesNearbyResponse>) {
    LazyColumn(
        Modifier.testTag("venues_collection"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(result) { venue ->
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(String.format(stringResource(R.string.venue_name_title), venue.name))
                Text(String.format(stringResource(R.string.venue_location_title), venue.location))
            }
        }
    }
}

@Composable
fun LoadingSpinner() {
    Box(
        modifier = Modifier
            .testTag("venues_nearby_overview_loading_spinner")
            .fillMaxSize()
            .zIndex(2f),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(45.dp),
        )
    }
}

@Composable
private fun MessageCard(
    modifier: Modifier = Modifier,
    icon: Int = R.drawable.ic_launcher_foreground,
    title: String,
    label: String,
    btnText: String?,
    onAction: (() -> Unit)?
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.padding(10.dp),
            painter = painterResource(id = icon),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            contentDescription = null
        )
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            modifier = Modifier.padding(
                top = 10.dp,
                bottom = 35.dp,
                start = 56.dp,
                end = 56.dp
            ),
            textAlign = TextAlign.Center,
            text = label,
            fontSize = 17.sp
        )
        onAction?.let {
            Button(
                modifier = Modifier
                    .testTag("action_btn_venues_nearby_card")
                    .padding(
                        horizontal = 50.dp,
                        vertical = 9.dp
                    )
                    .clip(RoundedCornerShape(50)),
                onClick = onAction,
            ) {
                Text(
                    text = btnText ?: "",
                )
            }
        }
    }
}

private fun checkPermissions(context: Context): Boolean {
    return context.hasGrantedPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            context.hasGrantedPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
}

private fun shouldShowRequestPermissionRationale(callback: PlacesRootCallback): Boolean =
    callback.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) &&
            callback.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
