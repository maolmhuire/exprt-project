package com.adyen.android.assignment.transaction

import com.adyen.android.assignment.money.Change

sealed class TransactionInput {
    data class Subtraction(
        val balanceChange: Change,
        val itemPrice: Long,
        val pricePaid: Change,
    ): TransactionInput()
}
