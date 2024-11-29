package com.emrehancetin.cs394.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.emrehancetin.cs394.R

class ProfileFragment : Fragment() {

    private var isOriginalImage = true
    private var isRecyclerViewVisible = false
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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerViewOrderHistory: RecyclerView = view.findViewById(R.id.recyclerViewOrderHistory)
        val orderHistoryImage : ImageView = view.findViewById(R.id.imageViewOrderHistory)
        orderHistoryImage.setOnClickListener {
            if(isOriginalImage){
                orderHistoryImage.setImageResource(R.drawable.withdraw)
            }else {
                orderHistoryImage.setImageResource(R.drawable.deposit)

            }
            isOriginalImage = !isOriginalImage
            if (isRecyclerViewVisible) {
                // Gizle
                recyclerViewOrderHistory.visibility = View.GONE
            } else {
                // Göster
                recyclerViewOrderHistory.visibility = View.VISIBLE
            }
            isRecyclerViewVisible = !isRecyclerViewVisible

        }
    }
}