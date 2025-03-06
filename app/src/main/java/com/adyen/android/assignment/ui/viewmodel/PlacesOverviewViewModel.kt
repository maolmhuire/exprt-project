package com.adyen.android.assignment.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.core.Result
import com.adyen.android.assignment.places.GetVenuesNearbyResponse
import com.adyen.android.assignment.places.usecase.GetVenuesNearbyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlacesOverviewViewModel @Inject constructor(
    private val venuesNearbyUseCase: GetVenuesNearbyUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<Result<List<GetVenuesNearbyResponse>>>(Result.Idle)
    val uiState: StateFlow<Result<List<GetVenuesNearbyResponse>>> = _uiState.asStateFlow()

    fun retrieveVenuesNearby() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { Result.Loading }
            _uiState.update { venuesNearbyUseCase.invoke() }
        }
    }
}
