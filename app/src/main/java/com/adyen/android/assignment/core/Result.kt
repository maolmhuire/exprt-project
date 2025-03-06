package com.adyen.android.assignment.core

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val throwable: Throwable) : Result<Nothing>
    data object Loading : Result<Nothing>
    data object Idle : Result<Nothing>
}
