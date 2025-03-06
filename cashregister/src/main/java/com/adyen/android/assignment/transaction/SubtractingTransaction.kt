package com.adyen.android.assignment.transaction

import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.MonetaryElement

class SubtractingTransaction(
    private val input: TransactionInput.Subtraction,
    private val elementsToRemove: List<MonetaryElement>
): Transaction {

    override fun performTransaction(): Change {
        val change = Change()
        // Add to balance
        input.pricePaid.getElements().forEach {
            input.balanceChange.add(it, 1)
        }
        // Remove appropriate change.
        elementsToRemove.forEach {
            input.balanceChange.remove(it, 1)
            change.add(it, 1)
        }
        return change
    }
}
