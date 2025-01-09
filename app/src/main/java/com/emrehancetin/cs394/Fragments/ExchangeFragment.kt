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
import com.emrehancetin.cs394.databinding.FragmentExchangeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.*

class ExchangeFragment : Fragment() {
    private var _binding: FragmentExchangeBinding? = null;
    private val binding get() = _binding!!


    private lateinit var appViewModel: AppViewModel
    private lateinit var cryptoDropdown: Spinner
    private lateinit var amountEditText: EditText
    private lateinit var priceTextView: TextView
    private var selectedCrypto: CryptoModel? = null

    private lateinit var auth:FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore
    }

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

        appViewModel.cryptoList.observe(viewLifecycleOwner) { cryptoList ->
            val cryptoNames = cryptoList.map { it.name }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                cryptoNames
            )
            cryptoDropdown.adapter = adapter

            cryptoDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedCrypto = cryptoList[position]
                    priceTextView.text = "Current Price: $${String.format("%.2f", selectedCrypto?.value ?: 0.0)}"
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    selectedCrypto = null
                }
            }
        }

        buyButton.setOnClickListener { processOrder("Buy") }
        sellButton.setOnClickListener { processOrder("Sell") }
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

        if (orderType == "Sell" && !_hasSufficientCrypto(crypto.code.uppercase(), amount)) {
            Toast.makeText(
                requireContext(),
                "Insufficient balance to sell ${crypto.name}.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (orderType == "Buy" && (appViewModel.cashBalance.value ?: 0.0) < total) {
            Toast.makeText(
                requireContext(),
                "Insufficient cash to buy ${crypto.name}.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        appViewModel.updateWallet(crypto.code, amount, total, orderType,auth.currentUser)
        appViewModel.addOrder(OrderHistoryModel(UUID.randomUUID().toString(), date, crypto.code, crypto.value ,amount, orderType))

        Toast.makeText(
            requireContext(),
            "$orderType Order for ${crypto.name} placed successfully!",
            Toast.LENGTH_SHORT
        ).show()



        amountEditText.text.clear()
    }

    private fun _hasSufficientCrypto(symbol: String, amount: Double): Boolean {
        val ownedCrypto = appViewModel.ownedCryptoList.value?.find { it.symbol == symbol }
        return ownedCrypto?.amount ?: 0.0 >= amount
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
