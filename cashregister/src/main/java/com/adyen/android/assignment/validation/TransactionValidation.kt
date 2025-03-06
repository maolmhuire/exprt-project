package com.adyen.android.assignment.validation

import com.adyen.android.assignment.exception.TransactionException
import com.adyen.android.assignment.transaction.Transaction
import kotlin.jvm.Throws

interface TransactionValidation {

    @Throws(TransactionException::class)
    fun validateTransaction(): Transaction
}