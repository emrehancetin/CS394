package com.emrehancetin.cs394.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emrehancetin.cs394.Adapter.CryptoAdapter
import com.emrehancetin.cs394.Adapter.OwnedCryptoAdapter
import com.emrehancetin.cs394.R
import com.emrehancetin.cs394.ViewModel.AppViewModel

class HomeFragment : Fragment() {

    private lateinit var appViewModel: AppViewModel
    private lateinit var cryptoAdapter: CryptoAdapter
    private lateinit var ownedCryptoAdapter: OwnedCryptoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appViewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]

        val cashTextView: TextView = view.findViewById(R.id.cashBalanceTextView)
        val ownedRecyclerView: RecyclerView = view.findViewById(R.id.ownedRecyclerView)
        val cryptoRecyclerView: RecyclerView = view.findViewById(R.id.cryptoRecyclerView)

        // Observe cash balance
        appViewModel.ownedCryptoList.observe(viewLifecycleOwner) { ownedList ->
            ownedCryptoAdapter.updateOwnedCryptoList(ownedList)
        }

        // Setup Owned Cryptos RecyclerView
        ownedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        ownedCryptoAdapter = OwnedCryptoAdapter(mutableListOf())
        ownedRecyclerView.adapter = ownedCryptoAdapter

        // Setup Cryptos RecyclerView
        cryptoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        cryptoAdapter = CryptoAdapter(mutableListOf()) {}
        cryptoRecyclerView.adapter = cryptoAdapter

        appViewModel.cashBalance.observe(viewLifecycleOwner) { cash ->
            cashTextView.text = "Cash: $${String.format("%.2f", cash)}"
        }

        appViewModel.ownedCryptoList.observe(viewLifecycleOwner) { ownedList ->
            ownedCryptoAdapter.updateOwnedCryptoList(ownedList)
        }

        appViewModel.cryptoList.observe(viewLifecycleOwner) { cryptoList ->
            cryptoAdapter.updateCryptoList(cryptoList)
        }

        appViewModel.loadCryptos()
    }
}
