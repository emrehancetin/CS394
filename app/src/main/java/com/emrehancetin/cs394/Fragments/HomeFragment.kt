package com.emrehancetin.cs394.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.emrehancetin.cs394.R
import com.emrehancetin.cs394.ViewModel.SharedViewModel

class HomeFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        val walletTextView: TextView = view.findViewById(R.id.walletBalanceTextView)
        val cashTextView: TextView = view.findViewById(R.id.cashBalanceTextView)

        // Observe BTC balance
        sharedViewModel.btcBalance.observe(viewLifecycleOwner) { balance ->
            walletTextView.text = "Wallet: ${String.format("%.6f BTC", balance)}"
        }

        // Observe Cash balance
        sharedViewModel.cashBalance.observe(viewLifecycleOwner) { cash ->
            cashTextView.text = "Cash: $${String.format("%.2f", cash)}"
        }
    }
}
