package com.adyen.android.assignment.transaction

import com.adyen.android.assignment.money.Change

interface Transaction {
    fun performTransaction(): Change
}