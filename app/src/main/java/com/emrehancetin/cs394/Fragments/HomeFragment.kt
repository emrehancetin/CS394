package com.emrehancetin.cs394.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emrehancetin.cs394.Adapter.CryptoAdapter
import com.emrehancetin.cs394.Model.CryptoModel
import com.emrehancetin.cs394.R


class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cryptoList = listOf(
            CryptoModel("Bitcoin", "BTC", 97309.25),
            CryptoModel("Ethereum", "ETH", 3728.09),
            CryptoModel("Cardano", "ADA", 1.07)
        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = CryptoAdapter(cryptoList) { crypto ->
            // Handle item click
            println("Clicked on: ${crypto.name}")
        }
    }


}