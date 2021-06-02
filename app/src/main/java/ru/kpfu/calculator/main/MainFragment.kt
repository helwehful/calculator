package ru.kpfu.calculator.main

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.kpfu.calculator.CalculatorAction
import ru.kpfu.calculator.R

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private lateinit var buttonClear: Button
    private lateinit var buttonChangeSign: Button
    private lateinit var buttonDivide: Button
    private lateinit var buttonMultiply: Button
    private lateinit var buttonPlus: Button
    private lateinit var buttonMinus: Button
    private lateinit var buttonEquals: Button
    private lateinit var buttonDot: Button

    private lateinit var button0: Button
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var button4: Button
    private lateinit var button5: Button
    private lateinit var button6: Button
    private lateinit var button7: Button
    private lateinit var button8: Button
    private lateinit var button9: Button

    private lateinit var numberTextView: TextView

    private lateinit var actions: List<Button>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        bindView()
        initOnClickListeners()
        initViewModel()
    }

    private fun bindView() {
        requireActivity().apply {
            buttonClear = findViewById(R.id.clear_btn)
            buttonChangeSign = findViewById(R.id.change_sign_btn)
            buttonDivide = findViewById(R.id.divide_btn)
            buttonMultiply = findViewById(R.id.multiply_btn)
            buttonPlus = findViewById(R.id.plus_btn)
            buttonMinus = findViewById(R.id.minus_btn)
            buttonEquals = findViewById(R.id.equals_btn)
            buttonDot = findViewById(R.id.dot_btn)

            button0 = findViewById(R.id.num0_btn)
            button1 = findViewById(R.id.num1_btn)
            button2 = findViewById(R.id.num2_btn)
            button3 = findViewById(R.id.num3_btn)
            button4 = findViewById(R.id.num4_btn)
            button5 = findViewById(R.id.num5_btn)
            button6 = findViewById(R.id.num6_btn)
            button7 = findViewById(R.id.num7_btn)
            button8 = findViewById(R.id.num8_btn)
            button9 = findViewById(R.id.num9_btn)

            numberTextView = findViewById(R.id.number_tv)
        }
        actions = listOf(buttonPlus, buttonMinus, buttonMultiply, buttonDivide, buttonEquals)
    }

    private fun initOnClickListeners() {
        initNumbers()
        initActions()
        buttonClear.setOnClickListener {
            viewModel.clear()
        }
        buttonChangeSign.setOnClickListener {
            viewModel.changeNumberSign()
        }
        buttonDot.setOnClickListener {
            viewModel.onDotClicked()
        }
    }


    private fun initNumbers() {
        val onNumClickListener = View.OnClickListener {
            val num = when (it.id) {
                R.id.num0_btn -> 0
                R.id.num1_btn -> 1
                R.id.num2_btn -> 2
                R.id.num3_btn -> 3
                R.id.num4_btn -> 4
                R.id.num5_btn -> 5
                R.id.num6_btn -> 6
                R.id.num7_btn -> 7
                R.id.num8_btn -> 8
                R.id.num9_btn -> 9
                else -> 0
            }

            viewModel.onNumberClicked(num)
        }
        val numbers = listOf(
            button0,
            button1, button2, button3,
            button4, button5, button6,
            button7, button8, button9
        )

        for (number in numbers) {
            number.setOnClickListener(onNumClickListener)
        }
    }

    private fun initActions() {
        val onActionClickListener = View.OnClickListener {
            val action = when (it.id) {
                R.id.plus_btn -> CalculatorAction.PLUS
                R.id.minus_btn -> CalculatorAction.MINUS
                R.id.multiply_btn -> CalculatorAction.MULTIPLY
                R.id.divide_btn -> CalculatorAction.DIVIDE
                R.id.equals_btn -> CalculatorAction.EQUALS
                else -> CalculatorAction.UNKNOWN
            }

            viewModel.onActionClicked(action)
        }

        for (action in actions) {
            action.setOnClickListener(onActionClickListener)
        }
    }

    private fun initViewModel() {
        viewModel.apply {
            buffer.observe(viewLifecycleOwner) {
                numberTextView.text = it
            }
            number.observe(viewLifecycleOwner) {
                buffer.postValue(it?.toString() ?: "0")
            }
            action.observe(viewLifecycleOwner) { actiontype ->
                if (!buffer.value.isNullOrEmpty() || isActionClicked.value == true) {
                    changeButtonColor(actiontype)
                    isActionClicked.postValue(true)
                }
            }
        }
    }

    private fun changeButtonColor(action: CalculatorAction) {
        val buttonSelected = when (action) {
            CalculatorAction.PLUS -> buttonPlus
            CalculatorAction.MINUS -> buttonMinus
            CalculatorAction.MULTIPLY -> buttonMultiply
            CalculatorAction.DIVIDE -> buttonDivide
            else -> null
        }

        val colorDefault = getButtonColor(R.color.purple_500)
        for (actionButton in actions) {
            actionButton.backgroundTintList = colorDefault
        }
        buttonSelected?.backgroundTintList = getButtonColor(R.color.teal_200)
    }

    private fun getButtonColor(color: Int) =
        ColorStateList.valueOf(ContextCompat.getColor(requireContext(), color))
}
