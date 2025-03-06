package com.adyen.android.assignment.validation

import com.adyen.android.assignment.exception.TransactionException
import com.adyen.android.assignment.transaction.TransactionInput
import com.adyen.android.assignment.rules.IncorrectChangeRule
import com.adyen.android.assignment.rules.NegativeValuesRule
import com.adyen.android.assignment.rules.InsufficientFundsRule
import com.adyen.android.assignment.transaction.SubtractingTransaction
import com.adyen.android.assignment.transaction.Transaction

class SubtractingTransactionValidation(
    private val input: TransactionInput.Subtraction
) : TransactionValidation {

    override fun validateTransaction(): Transaction = with(input) {

        val amountPaidTotal = pricePaid.total

        if (!NegativeValuesRule(itemPrice, amountPaidTotal).validate().isValid) {
            throw TransactionException(
                "Cannot complete subtracting transaction of negative numbers."
            )
        }

        if (!InsufficientFundsRule(itemPrice, amountPaidTotal).validate().isValid) {
            throw TransactionException("Insufficient funds for transaction.")
        }

        val changeRuleValidationResult = IncorrectChangeRule(
            balance = balanceChange,
            itemCost = itemPrice,
            amountPaid = amountPaidTotal
        ).validate()

        if (!changeRuleValidationResult.isValid) {
            throw TransactionException("Incorrect change: cannot complete transaction.")
        }

        return SubtractingTransaction(
            input = input,
            elementsToRemove = changeRuleValidationResult.change,
        )
    }
}
