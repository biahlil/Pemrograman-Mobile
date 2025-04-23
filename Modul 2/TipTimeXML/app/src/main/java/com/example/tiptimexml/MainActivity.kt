package com.example.tiptimexml

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.tiptimexml.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: TipViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TipViewModel::class.java]

        val percentages = listOf(10, 15, 20)
        binding.tipPercentageDropdown.setAdapter(
            TipViewModel.TipPercentageAdapter(this, percentages)
        )

        fun updateTip() {
            val bill = binding.billAmountEditText.text?.toString()
            val percent = binding.tipPercentageDropdown.text.toString().toIntOrNull() ?: 0
            val round = binding.roundUpSwitch.isChecked
            viewModel.calculateTip(bill, percent, round)
        }

        binding.billAmountEditText.doAfterTextChanged    { updateTip() }
        binding.tipPercentageDropdown.doAfterTextChanged    { updateTip() }
        binding.roundUpSwitch.setOnCheckedChangeListener { _, _ -> updateTip() }

        viewModel.tipAmount.observe(this) { binding.tipResultText.text = it }
    }
}

