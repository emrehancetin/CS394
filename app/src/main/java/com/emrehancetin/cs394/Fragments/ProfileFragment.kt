package com.emrehancetin.cs394.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emrehancetin.cs394.Adapter.OrderHistoryAdapter
import com.emrehancetin.cs394.Model.OrderHistoryModel
import com.emrehancetin.cs394.R
import com.emrehancetin.cs394.ViewModel.SharedViewModel

class ProfileFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var adapter: OrderHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        val recyclerViewOrderHistory: RecyclerView = view.findViewById(R.id.recyclerViewOrderHistory)
        recyclerViewOrderHistory.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the adapter
        adapter = OrderHistoryAdapter(mutableListOf())
        recyclerViewOrderHistory.adapter = adapter

        // Observe the order history from the shared ViewModel
        sharedViewModel.orderHistory.observe(viewLifecycleOwner) { updatedHistory ->
            adapter.updateOrders(updatedHistory)
        }
    }
}
