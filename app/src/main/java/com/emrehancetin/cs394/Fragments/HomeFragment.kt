package com.emrehancetin.cs394.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emrehancetin.cs394.Adapter.CryptoAdapter
import com.emrehancetin.cs394.Model.CryptoModel
import com.emrehancetin.cs394.R
import com.emrehancetin.cs394.ViewModel.AppViewModel

class HomeFragment : Fragment() {

    private lateinit var appViewModel: AppViewModel
    private lateinit var adapter: CryptoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appViewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = CryptoAdapter(mutableListOf()) { crypto ->
            Toast.makeText(
                requireContext(),
                "Selected: ${crypto.name} (${crypto.code}) - $${String.format("%.2f", crypto.value)}",
                Toast.LENGTH_SHORT
            ).show()
        }
        recyclerView.adapter = adapter

        appViewModel.cryptoList.observe(viewLifecycleOwner) { cryptoList ->
            println("Observed crypto list: $cryptoList")
            adapter.updateCryptoList(cryptoList)
        }

        appViewModel.loadCryptos()
    }


}
