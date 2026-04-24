package ec.edu.uisek.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class CalculatorState (
    val display : String = "0"
)

sealed class CalculatorEvent {
    data class Number(val number: String) : CalculatorEvent()
    data class Operator(val operator: String) : CalculatorEvent()
    object Clear : CalculatorEvent()
    object AllClear : CalculatorEvent()
    object Calculate : CalculatorEvent()
    object Decimal : CalculatorEvent()

}

class CalculatorViewModel : ViewModel(){
    private var number1 : String = ""
    private var number2 : String = ""
    private var operator : String? = null
    var state by mutableStateOf(CalculatorState())
        private set

    fun onEvent(event : CalculatorEvent) {
        when (event) {
            is CalculatorEvent.Number -> enterNumber(event.number)
            CalculatorEvent.AllClear -> clearALL()
            CalculatorEvent.Calculate -> performCalculation()
            CalculatorEvent.Clear -> clearLast()
            CalculatorEvent.Decimal -> enterDecimal()
            is CalculatorEvent.Operator -> enterOperator(event.operator)
        }
    }

    /**
     * Permite ingresar un número a la calculadora
     */
    private fun enterNumber(number : String){
        if(operator == null){
            number1 += number
            state = state.copy(display = number1)
        } else{
            number2 += number
            state = state.copy(display = number2)
        }
    }
    /**
     * Permite ingresar un operador a la calculadora
     */
    private fun enterDecimal(){
        val currentNumber = if (operator == null) number1 else number2
        if (!currentNumber.contains(".")) {
            if (operator == null) {
                number1 += "."
                state = state.copy(display = number1)
            } else {
                number2 += "."
                state = state.copy(display = number2)
            }
        }
    }


    /**
     *
     */
    private fun enterOperator(operator : String){
        if (number1.isBlank()) return

        if (number2.isNotBlank() && this.operator != null) {
            performCalculation()
        }

        this.operator = operator

    }

    /**
     *
     */
    private fun clearALL(){
        number1 = ""
        number2 = ""
        operator = null
        state = state.copy(display = "0")
    }

    /**
     *
     */
    private fun clearLast(){
        if (operator == null) {
            number1 = number1.dropLast(1)
            state = state.copy(display = number1)
        } else {
            number2 = number2.dropLast(1)
            state = state.copy(display = number2)
        }
    }

    /**
     * Realiza el cálculo de la operación
     */

    private fun performCalculation(){
        val num1 = number1.toDoubleOrNull()
        val num2 = number2.toDoubleOrNull()
        if (num1 != null && num2 != null && operator != null) {
            val result = when (operator) {
                "+" -> num1 + num2
                "−" -> num1 - num2
                "×" -> num1 * num2
                "÷" -> if (num2 != 0.0) num1 / num2 else Double.NaN
                else -> 0.0
            }

            clearALL()
            val resultString = if (result.isNaN()) "Error" else result.toString()
                .removeSuffix(".0")
            number1 = if (result.isNaN()) "" else resultString
            state = state.copy(display = resultString)
        }
    }

}
