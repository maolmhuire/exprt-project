package com.adyen.android.assignment.rules

import com.adyen.android.assignment.exception.TransactionException
import kotlin.jvm.Throws

interface ValidationRule {

    @Throws(TransactionException::class)
    fun validate(): ValidationResult
}

interface ValidationResult {
    val isValid: Boolean
}
