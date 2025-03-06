package com.adyen.android.assignment

import com.adyen.android.assignment.exception.TransactionException
import com.adyen.android.assignment.money.Bill
import com.adyen.android.assignment.money.Change
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CashRegisterTest {

    private lateinit var cashRegisterBalance: Change
    private lateinit var cashRegister: CashRegister

    @Before
    fun setUp() {
        initBalance()
    }

    private fun initBalance() {
        cashRegisterBalance = Change()
            .add(Bill.FIVE_EURO, 4)
            .add(Bill.TEN_EURO, 4)
            .add(Bill.TWENTY_EURO, 4)
            .add(Bill.FIFTY_EURO, 4)
            .add(Bill.ONE_HUNDRED_EURO, 4)
        cashRegister = CashRegister(cashRegisterBalance)
    }

    @Test
    fun `Test the maximum amount of transactions where 20 euro change is returned`() {
        initBalance()
        var beforeTransaction20Count = cashRegisterBalance.getCount(Bill.TWENTY_EURO)
        var beforeTransaction10Count = cashRegisterBalance.getCount(Bill.TEN_EURO)
        var beforeTransaction5Count = cashRegisterBalance.getCount(Bill.FIVE_EURO)

        val maxNumTransactions = (beforeTransaction5Count / 4) +
                (beforeTransaction10Count / 2) +
                beforeTransaction20Count

        val pricePaid = Change()
            .add(Bill.ONE_HUNDRED_EURO, 1)

        val itemPrice = 80_00L

        for (i in 0 until maxNumTransactions) {
            val change = cashRegister.performTransaction(itemPrice, pricePaid)
            change.getElements().forEach {
                val changeNoteCount = change.getCount(it)
                val registerNoteCount = cashRegisterBalance.getCount(it)

                val newTotal = when (it) {
                    Bill.TWENTY_EURO -> beforeTransaction20Count - changeNoteCount
                    Bill.TEN_EURO -> beforeTransaction10Count - changeNoteCount
                    Bill.FIVE_EURO -> beforeTransaction5Count - changeNoteCount
                    else -> registerNoteCount
                }

                Assert.assertTrue(
                    registerNoteCount == newTotal
                )

                // Update counts.
                beforeTransaction20Count = cashRegisterBalance.getCount(Bill.TWENTY_EURO)
                beforeTransaction10Count = cashRegisterBalance.getCount(Bill.TEN_EURO)
                beforeTransaction5Count = cashRegisterBalance.getCount(Bill.FIVE_EURO)
            }
        }

        Assert.assertTrue(
            // No possible twenty change combination remains.
            cashRegisterBalance.getCount(Bill.FIVE_EURO) == 0 &&
                    cashRegisterBalance.getCount(Bill.TEN_EURO) == 0 &&
                    cashRegisterBalance.getCount(Bill.TWENTY_EURO) == 0
        )
    }

    @Test
    fun `Remove one twenty note`() {
        initBalance()
        val beforeTransaction = cashRegisterBalance.getCount(Bill.TWENTY_EURO)
        val pricePaid = Change()
            .add(Bill.ONE_HUNDRED_EURO, 1)

        val itemPrice = 80_00L

        val change = cashRegister.performTransaction(itemPrice, pricePaid)
        Assert.assertTrue(
            change.getCount(Bill.TWENTY_EURO) == 1
                    && (cashRegisterBalance.getCount(Bill.TWENTY_EURO) + 1 == beforeTransaction)
        )
    }

    @Test
    fun `Remove one fifty note`() {
        initBalance()
        val beforeTransaction = cashRegisterBalance.getCount(Bill.FIFTY_EURO)
        val pricePaid = Change()
            .add(Bill.ONE_HUNDRED_EURO, 1)

        val itemPrice = 50_00L

        val change = cashRegister.performTransaction(itemPrice, pricePaid)
        Assert.assertTrue(
            change.getCount(Bill.FIFTY_EURO) == 1
                    && cashRegisterBalance.getCount(Bill.FIFTY_EURO) + 1 == beforeTransaction
        )
    }

    @Test
    fun `Remove one ten note`() {
        initBalance()
        val beforeTransaction = cashRegisterBalance.getCount(Bill.TWENTY_EURO)
        val pricePaid = Change()
            .add(Bill.TWENTY_EURO, 1)

        val itemPrice = 10_00L

        val change = cashRegister.performTransaction(itemPrice, pricePaid)
        Assert.assertTrue(
            change.getCount(Bill.TEN_EURO) == 1
                    && (cashRegisterBalance.getCount(Bill.TEN_EURO) + 1) == beforeTransaction
        )
    }

    @Test
    fun `Remove one fifty, one twenty, and one five note`() {
        initBalance()
        val initialTotal = cashRegisterBalance.total
        val beforeTransaction50 = cashRegisterBalance.getCount(Bill.FIFTY_EURO)
        val beforeTransaction20 = cashRegisterBalance.getCount(Bill.TWENTY_EURO)
        val beforeTransaction5 = cashRegisterBalance.getCount(Bill.FIVE_EURO)

        val pricePaid = Change()
            .add(Bill.ONE_HUNDRED_EURO, 1)

        val itemPrice = 25_00L

        val change = cashRegister.performTransaction(itemPrice, pricePaid)

        val notesInChange = change.getCount(Bill.TWENTY_EURO) == 1
                && change.getCount(Bill.TWENTY_EURO) == 1
                && change.getCount(Bill.FIVE_EURO) == 1

        val notesRemovedFromRegister =
            (cashRegisterBalance.getCount(Bill.FIFTY_EURO) + 1) == beforeTransaction50
                && (cashRegisterBalance.getCount(Bill.TWENTY_EURO) + 1) == beforeTransaction20
                && (cashRegisterBalance.getCount(Bill.FIVE_EURO) + 1) == beforeTransaction5

        val totalsEqual = initialTotal + itemPrice == cashRegisterBalance.total
        Assert.assertTrue(
            notesInChange && notesRemovedFromRegister && totalsEqual
        )
    }

    @Test
    fun `Remove all five euro notes and throw exception for incorrect change`() {
        initBalance()
        val maximumAmountOfFiveNotesPlusOne = cashRegisterBalance.getCount(Bill.FIVE_EURO) + 1
        var exception: TransactionException? = null
        try {
            for (i in 0 until maximumAmountOfFiveNotesPlusOne) {
                val pricePaid = Change()
                    .add(Bill.TEN_EURO, 1)
                val itemPrice = 5_00L
                cashRegister.performTransaction(itemPrice, pricePaid)
            }
        } catch (e: TransactionException) {
            exception = e
        }

        Assert.assertTrue(
            exception?.message != null && cashRegisterBalance.getCount(Bill.FIVE_EURO) == 0
        )
    }

    @Test
    fun `Throws exception for negative number`() {
        initBalance()
        val pricePaid = Change()
            .add(Bill.TEN_EURO, 1)

        val itemPrice = -10_00L

        var exception: TransactionException? = null
        try {
            cashRegister.performTransaction(itemPrice, pricePaid)
        } catch (e: TransactionException) {
            exception = e
        }
        Assert.assertNotNull(exception)
    }

    @Test
    fun `Throws exception for insufficient funds`() {
        initBalance()
        val pricePaid = Change()
            .add(Bill.TEN_EURO, 1)

        val itemPrice = 50_00L

        var exception: TransactionException? = null
        try {
            cashRegister.performTransaction(itemPrice, pricePaid)
        } catch (e: TransactionException) {
            exception = e
        }
        Assert.assertNotNull(exception)
    }
}
