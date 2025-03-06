package com.adyen.android.assignment.core.exception

class LocationClientException(message: String? = null) : Exception(message)
class MissingLocationPermissionException : Exception()