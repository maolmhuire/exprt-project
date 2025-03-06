package com.adyen.android.assignment.rules

import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.MonetaryElement

class IncorrectChangeRule(
    private val balance: Change,
    private val itemCost: Long,
    private val amountPaid: Long,
) : ValidationRule {

    override fun validate(): IncorrectChangeRuleValidationResult {
        val requiredChangeValue = amountPaid - itemCost
        val elements = arrayListOf<MonetaryElement>()
        var changeTotal = 0L

        balance.getElements()
            .sortedByDescending { it.minorValue }
            .forEach {
                for (i in 0 until balance.getCount(it)) {
                    val lessThanRequiredAmount = changeTotal < requiredChangeValue
                    val canAddProposedValue = (changeTotal + it.minorValue) <= requiredChangeValue

                    if (lessThanRequiredAmount && canAddProposedValue) {
                        changeTotal += it.minorValue
                        elements.add(it)
                    }
                }
            }

        return if (requiredChangeValue == changeTotal) {
            IncorrectChangeRuleValidationResult(
                isValid = true,
                change = elements
            )
        } else {
            IncorrectChangeRuleValidationResult(
                isValid = false,
                change = emptyList()
            )
        }
    }
}

data class IncorrectChangeRuleValidationResult(
    override val isValid: Boolean,
    val change: List<MonetaryElement>,
): ValidationResult
