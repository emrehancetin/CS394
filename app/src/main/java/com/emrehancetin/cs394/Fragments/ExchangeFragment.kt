package com.emrehancetin.cs394.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.emrehancetin.cs394.Model.OrderHistoryModel
import com.emrehancetin.cs394.R
import com.emrehancetin.cs394.ViewModel.SharedViewModel
import java.text.SimpleDateFormat
import java.util.*

class ExchangeFragment : Fragment() {

    private val btcPrice: Double = 15100.0 // Example BTC price
    private lateinit var sharedViewModel: SharedViewModel
    private var selectedOrderType: String = "Buy" // Default order type

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exchange, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        val amountEdtTxt: EditText = view.findViewById(R.id.amountEdtTxt)
        val totalEdtTxt: EditText = view.findViewById(R.id.totalEdtTxt)
        val sendOrderButton: Button = view.findViewById(R.id.sendOrderButton)
        val radioGroup: RadioGroup = view.findViewById(R.id.radioGroup)

        // Track radio button selection
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedOrderType = when (checkedId) {
                R.id.buyButton -> "Buy"
                R.id.sellButton -> "Sell"
                else -> "Unknown"
            }
        }

        // Add TextWatcher to calculate total
        amountEdtTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val amountText = s.toString()
                if (amountText.isNotEmpty()) {
                    try {
                        val amount = amountText.toDouble()
                        val total = amount * btcPrice
                        totalEdtTxt.setText(String.format("%.2f", total))
                    } catch (e: NumberFormatException) {
                        totalEdtTxt.setText("")
                    }
                } else {
                    totalEdtTxt.setText("")
                }
            }
        })

        // Send Order
        sendOrderButton.setOnClickListener {
            sendOrder(amountEdtTxt, totalEdtTxt)
        }
    }

    private fun sendOrder(amountEdtTxt: EditText, totalEdtTxt: EditText) {
        val amountText = amountEdtTxt.text.toString()
        val amount = if (amountText.isNotEmpty()) amountText.toDouble() else 0.0
        val totalText = totalEdtTxt.text.toString()
        val total = if (totalText.isNotEmpty()) totalText.toDouble() else 0.0
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // Validate Sell Orders
        if (selectedOrderType == "Sell") {
            val walletBalance = sharedViewModel.btcBalance.value ?: 0.0
            if (amount > walletBalance) {
                Toast.makeText(
                    requireContext(),
                    "Insufficient BTC balance to sell. Available: ${String.format("%.6f", walletBalance)} BTC",
                    Toast.LENGTH_LONG
                ).show()
                return // Stop the order process
            }
        }

        // Update Wallet and Cash Balance
        sharedViewModel.updateWallet(amount, total, selectedOrderType)

        // Create Order
        val newOrder = OrderHistoryModel(UUID.randomUUID().toString(), date, amount, selectedOrderType)
        sharedViewModel.addOrder(newOrder)

        // Show Toast
        Toast.makeText(
            requireContext(),
            "Order sent successfully.",
            Toast.LENGTH_LONG
        ).show()
    }
}
