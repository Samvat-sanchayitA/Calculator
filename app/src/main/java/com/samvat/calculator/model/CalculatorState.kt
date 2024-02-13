package com.samvat.calculator.model

data class CalculatorState(
    val input1: String = "",
    val input2: String = "",
    val operation: CalculatorOperation? = null
)

