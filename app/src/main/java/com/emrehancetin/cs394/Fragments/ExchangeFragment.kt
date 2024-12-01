package com.emrehancetin.cs394.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.emrehancetin.cs394.R
import java.text.SimpleDateFormat
import java.util.*

class ExchangeFragment : Fragment() {

    private val btcPrice: Double = 15100.0 // Example BTC price

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exchange, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val amountEdtTxt: EditText = view.findViewById(R.id.amountEdtTxt)
        val totalEdtTxt: EditText = view.findViewById(R.id.totalEdtTxt)
        val sendOrderButton: Button = view.findViewById(R.id.sendOrderButton)
        val radioGroup: RadioGroup = view.findViewById(R.id.radioGroup)

        // Add TextWatcher to the amount field
        amountEdtTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val amountText = s.toString()
                if (amountText.isNotEmpty()) {
                    try {
                        val amount = amountText.toDouble()
                        val total = amount * btcPrice
                        totalEdtTxt.setText(String.format("%.2f", total)) // Update total field
                    } catch (e: NumberFormatException) {
                        totalEdtTxt.setText("") // Clear total field if input is invalid
                    }
                } else {
                    totalEdtTxt.setText("") // Clear total field if amount is empty
                }
            }
        })

        // Set up click listener for the send order button
        sendOrderButton.setOnClickListener {
            sendOrder(amountEdtTxt, totalEdtTxt, radioGroup)
        }
    }

    private fun sendOrder(amountEdtTxt: EditText, totalEdtTxt: EditText, radioGroup: RadioGroup) {
        // Get the amount
        val amountText = amountEdtTxt.text.toString()
        val amount = if (amountText.isNotEmpty()) amountText else "0"

        // Get the total
        val totalText = totalEdtTxt.text.toString()
        val total = if (totalText.isNotEmpty()) totalText else "0.00"

        // Get the order type
        val selectedRadioButtonId = radioGroup.checkedRadioButtonId
        val orderType = if (selectedRadioButtonId != -1) {
            val selectedRadioButton: RadioButton = requireView().findViewById(selectedRadioButtonId)
            selectedRadioButton.text.toString()
        } else {
            "Unknown"
        }

        // Get the current date
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // Show the toast
        Toast.makeText(
            requireContext(),
            "Total: $total\nAmount: $amount\nDate: $date\nOrder Type: $orderType",
            Toast.LENGTH_LONG
        ).show()
    }
}
