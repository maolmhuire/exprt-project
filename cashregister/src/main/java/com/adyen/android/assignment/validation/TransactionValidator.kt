package com.adyen.android.assignment.validation

import com.adyen.android.assignment.transaction.TransactionInput

object TransactionValidator {

    fun getValidationForTransaction(input: TransactionInput): TransactionValidation {
        return when {

            input is TransactionInput.Subtraction -> {
                SubtractingTransactionValidation(input)
            }

            else -> {
                // TODO: Implement for other kinds of transactions.
                throw NotImplementedError()
            }
        }
    }
}