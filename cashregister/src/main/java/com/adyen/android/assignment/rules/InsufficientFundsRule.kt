package com.adyen.android.assignment.rules


class InsufficientFundsRule(
    private val price: Long,
    private val pricePaid: Long,
) : ValidationRule {

    override fun validate(): InsufficientFundsRuleValidationResult =
        InsufficientFundsRuleValidationResult(
            isValid = !insufficientFunds()
        )

    private fun insufficientFunds(): Boolean = pricePaid < price
}

data class InsufficientFundsRuleValidationResult(
    override val isValid: Boolean
): ValidationResult
