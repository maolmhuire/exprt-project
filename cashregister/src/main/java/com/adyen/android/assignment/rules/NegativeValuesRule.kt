package com.adyen.android.assignment.rules

class NegativeValuesRule(
    private val price: Long,
    private val pricePaid: Long
) : ValidationRule {

    override fun validate(): NegativeValuesRuleValidationResult =
        NegativeValuesRuleValidationResult(
            isValid = !negativeNumbers()
        )

    private fun negativeNumbers(): Boolean = price < 0L || pricePaid < 0L
}

data class NegativeValuesRuleValidationResult(
    override val isValid: Boolean
): ValidationResult
