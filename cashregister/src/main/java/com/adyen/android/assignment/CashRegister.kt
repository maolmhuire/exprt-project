package com.adyen.android.assignment

import com.adyen.android.assignment.exception.TransactionException
import com.adyen.android.assignment.transaction.TransactionInput
import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.MonetaryElement
import com.adyen.android.assignment.validation.TransactionValidator
import kotlin.jvm.Throws

/**
 * The CashRegister class holds the logic for performing transactions.
 *
 * @param change The change that the CashRegister is holding.
 */
class CashRegister(private val change: Change) {
    /**
     * Performs a transaction for a product/products with a certain price and a given amount.
     *
     * @param price The price of the product(s).
     * @param amountPaid The amount paid by the shopper.
     *
     * @return The change for the transaction.
     *
     * @throws TransactionException If the transaction cannot be performed.
     */
    @Throws(TransactionException::class)
    fun performTransaction(price: Long, amountPaid: Change): Change {
        val transaction = TransactionValidator.getValidationForTransaction(
            TransactionInput.Subtraction(
                balanceChange = change,
                itemPrice = price,
                pricePaid = amountPaid
            )
        ).validateTransaction()
        return transaction.performTransaction()
    }
}
