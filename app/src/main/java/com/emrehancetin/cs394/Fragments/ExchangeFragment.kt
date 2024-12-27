package com.emrehancetin.cs394.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.emrehancetin.cs394.Model.CryptoModel
import com.emrehancetin.cs394.Model.OrderHistoryModel
import com.emrehancetin.cs394.R
import com.emrehancetin.cs394.ViewModel.AppViewModel
import java.text.SimpleDateFormat
import java.util.*

class ExchangeFragment : Fragment() {

    private lateinit var appViewModel: AppViewModel
    private lateinit var cryptoDropdown: Spinner
    private lateinit var amountEditText: EditText
    private lateinit var priceTextView: TextView
    private var selectedCrypto: CryptoModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_exchange, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appViewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]

        cryptoDropdown = view.findViewById(R.id.cryptoDropdown)
        amountEditText = view.findViewById(R.id.amountEditText)
        priceTextView = view.findViewById(R.id.priceTextView)

        val buyButton: Button = view.findViewById(R.id.buyButton)
        val sellButton: Button = view.findViewById(R.id.sellButton)

        // Observe the crypto list and populate the dropdown
        appViewModel.cryptoList.observe(viewLifecycleOwner) { cryptoList ->
            val cryptoNames = cryptoList.map { it.name }
            val adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item, // Use custom layout
                cryptoNames
            )
            adapter.setDropDownViewResource(R.layout.spinner_item) // Dropdown layout
            cryptoDropdown.adapter = adapter

            cryptoDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedCrypto = cryptoList[position]
                    priceTextView.text = "Current Price: $${String.format("%.2f", selectedCrypto?.value ?: 0.0)}"
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    selectedCrypto = null
                }
            }
        }


        buyButton.setOnClickListener {
            processOrder("Buy")
        }

        sellButton.setOnClickListener {
            processOrder("Sell")
        }

        appViewModel.loadCryptos() // Ensure cryptos are loaded dynamically
    }

    private fun processOrder(orderType: String) {
        val amountText = amountEditText.text.toString()
        val amount = amountText.toDoubleOrNull() ?: 0.0
        val crypto = selectedCrypto

        if (crypto == null || amount <= 0) {
            Toast.makeText(requireContext(), "Please select a valid crypto and enter an amount.", Toast.LENGTH_SHORT).show()
            return
        }

        val total = amount * crypto.value

        if (orderType == "Sell" && (appViewModel.btcBalance.value ?: 0.0) < amount) {
            Toast.makeText(
                requireContext(),
                "Insufficient balance to sell ${crypto.name}.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // Update wallet balances and add order
        appViewModel.updateWallet(amount, total, orderType)
        appViewModel.addOrder(OrderHistoryModel(UUID.randomUUID().toString(), date, amount, orderType))

        Toast.makeText(
            requireContext(),
            "$orderType Order for ${crypto.name} placed successfully!",
            Toast.LENGTH_SHORT
        ).show()

        amountEditText.text.clear()
    }
}
