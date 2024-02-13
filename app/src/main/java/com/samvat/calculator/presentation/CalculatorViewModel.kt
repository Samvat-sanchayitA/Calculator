package com.samvat.calculator.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.samvat.calculator.model.CalculatorAction
import com.samvat.calculator.model.CalculatorOperation
import com.samvat.calculator.model.CalculatorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class CalculatorViewModel : ViewModel() {
    val state = MutableStateFlow(CalculatorState())

    fun onAction(action: CalculatorAction) {
        when (action) {
            CalculatorAction.Calculate -> calculate()
            CalculatorAction.Clear ->  state.update { CalculatorState() }
            CalculatorAction.Decimal -> enterDecimal()
            CalculatorAction.Delete -> delete()
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Operation -> enterOperation(action.operation)
        }
    }

    private fun enterOperation(operation: CalculatorOperation) {
        if (state.value.input1.isNotBlank()) {
            state.update { it.copy(operation = operation) }
        }else if(operation.symbol == "-") {
            state.update { it.copy(input1 = "-") }
        }
    }

    private fun enterNumber(number: Int) {
        if (state.value.operation == null) {

                if (state.value.input1.length >= MAX_NUM_LENGTH) {
                    return
                }
                state.update { it.copy(input1 = it.input1 + number) }
            return
        }
            if (state.value.input2.length >= MAX_NUM_LENGTH) {
                return
            }
            state.update { it.copy(input2 = it.input2 + number) }
    }

    private fun delete() {
        when {
            state.value.input2.isNotBlank() -> state.update {
                it.copy(
                    input2 = it.input2.dropLast(1)
                )
            }

            state.value.operation != null -> state.update {
                it.copy(
                    operation = null
                )
            }

            state.value.input1.isNotBlank() ->  state.update {
                it.copy(
                    input1 = it.input1.dropLast(1)
                )
            }
        }
    }

    private fun enterDecimal() {
        if (state.value.operation == null && !state.value.input1.contains(".") && state.value.input1.isNotBlank()) {
            state.update {
                it.copy(
                    input1 = it.input1 + "."
                )
            }
            return
        }
        if (!state.value.input2.contains(".") && state.value.input2.isNotBlank()) {
            state.update {
                it.copy(
                    input2 = it.input2 + "."
                )
            }
        }
    }

    private fun calculate() {
        val number1 = state.value.input1.toDoubleOrNull()
        val number2 = state.value.input2.toDoubleOrNull()
        if (number1 != null && number2 != null) {
            val result = when (state.value.operation) {
                is CalculatorOperation.Add -> number1 + number2
                is CalculatorOperation.Subtract -> number1 - number2
                is CalculatorOperation.Multiply -> number1 * number2
                is CalculatorOperation.Divide -> number1 / number2
                is CalculatorOperation.Modulo -> number1 % number2
                is CalculatorOperation.Power -> Math.pow(number1, number2)
                null -> return
            }
            showResult(result)
        }

    }

    private fun showResult(result: Double) {
        state.update {
            it.copy(
                input1 = result.toString().take(7),
                input2 = "",
                operation = null
            )
        }
    }

    companion object {
        private const val MAX_NUM_LENGTH = 8
    }
}