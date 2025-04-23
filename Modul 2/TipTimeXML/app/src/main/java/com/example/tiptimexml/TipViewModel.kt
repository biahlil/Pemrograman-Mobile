package com.example.tiptimexml

import android.content.Context
import android.widget.ArrayAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.ceil

class TipViewModel : ViewModel() {

    private val _tipAmount = MutableLiveData<String>()
    val tipAmount: LiveData<String> = _tipAmount

    fun calculateTip(bill: String?, percentage: Int, roundUp: Boolean) {
        val billAmount = bill?.toDoubleOrNull() ?: 0.0
        var tip = billAmount * percentage / 100.0
        if (roundUp) tip = ceil(tip)
        _tipAmount.value = "Tip Amount: $%.2f".format(tip)
    }

    class TipPercentageAdapter(context: Context, items: List<Int>) :
        ArrayAdapter<Int>(context, android.R.layout.simple_dropdown_item_1line, items)
}


