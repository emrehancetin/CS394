package com.emrehancetin.cs394.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emrehancetin.cs394.Adapter.OrderHistoryAdapter
import com.emrehancetin.cs394.Model.OrderHistoryModel
import com.emrehancetin.cs394.R

class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerViewOrderHistory: RecyclerView = view.findViewById(R.id.recyclerViewOrderHistory)

        // Sample data for order history
        val orderHistory = listOf(
            OrderHistoryModel("1", "2024-12-01", 100.00, "Buy"),
            OrderHistoryModel("2", "2024-11-25", 200.00, "Sell"),
            OrderHistoryModel("3", "2024-11-10", 50.00, "Buy")
        )

        // Set up the RecyclerView with the adapter
        recyclerViewOrderHistory.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewOrderHistory.adapter = OrderHistoryAdapter(orderHistory)
    }
}
