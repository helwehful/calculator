package ru.kpfu.calculator.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.kpfu.calculator.CalculatorAction
import java.lang.NumberFormatException
import kotlin.math.absoluteValue

class MainViewModel : ViewModel() {

    var number: MutableLiveData<Number?> = MutableLiveData(null)
    var action = MutableLiveData(CalculatorAction.UNKNOWN)
    var buffer = MutableLiveData("0")
    var isActionClicked = MutableLiveData(false)

    fun onNumberClicked(num: Int) {
        if (isActionClicked.value == true) {
            buffer.postValue("$num")
            isActionClicked.postValue(false)
        } else {
            buffer.postValue("${buffer.value}$num")
        }
    }

    fun onActionClicked(calculatorAction: CalculatorAction) {
        if (!buffer.value.isNullOrEmpty()) {
            try {
                val bufferedNumber = buffer.value?.toDouble()
                val currentNumberValue = number.value?.toDouble()
                val num = makeAction(currentNumberValue, bufferedNumber)
                saveNumber(num)
                action.postValue(calculatorAction)
            } catch (e: NumberFormatException) {
                buffer.postValue("Ошибка")
            }
        }
    }

    fun changeNumberSign() {
        val bufferValue = buffer.value
        if (bufferValue?.startsWith("-") == true) {
            buffer.postValue(bufferValue.substring(1))
        } else {
            buffer.postValue("-$bufferValue")
        }
    }

    fun onDotClicked() {
        val value = buffer.value
        if (value.isNullOrEmpty() || isActionClicked.value == true) {
            buffer.postValue("0.")
        } else {
            if (!value.contains("."))
                buffer.postValue("${buffer.value}.")
        }
        isActionClicked.postValue(false)
    }

    fun clear() {
        number.postValue(null)
        action.postValue(CalculatorAction.UNKNOWN)
        buffer.postValue("0")
        isActionClicked.postValue(false)
    }

    private fun saveNumber(num: Double?) {
        number.postValue(if (isInt(num)) num?.toInt() else num)
    }

    private fun makeAction(num1: Double?, num2: Double?): Double? {
        return num2?.let {
            when (action.value) {
                CalculatorAction.PLUS -> num1?.plus(it)
                CalculatorAction.MINUS -> num1?.minus(it)
                CalculatorAction.DIVIDE -> num1?.div(it)
                CalculatorAction.MULTIPLY -> num1?.times(it)
                CalculatorAction.EQUALS -> num1
                else -> it
            }
        }
    }

    private fun isInt(num: Double?): Boolean {
        val remValue = num?.absoluteValue?.rem(1)
        return remValue == 0.0
    }
}
