package com.emrehancetin.cs394.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.emrehancetin.cs394.R


class ExchangeFragment : Fragment() {


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
        return inflater.inflate(R.layout.fragment_exchange, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //when radio button click
        val sendOrderButton : Button = view.findViewById(R.id.sendOrderButton)
        val sendOrderButtonText :String = sendOrderButton.text.toString()
        val sellRadioButton:RadioButton = view.findViewById(R.id.sellButton)
        sellRadioButton.setOnClickListener{
            Toast.makeText(requireContext(), "Sell seçildi", Toast.LENGTH_SHORT).show()
            sendOrderButton.setText(sendOrderButtonText + " (SELL)")
        }
        val buyRadioButton:RadioButton = view.findViewById(R.id.buyButton)
        buyRadioButton.setOnClickListener {
            Toast.makeText(requireContext(), "Buy seçildi", Toast.LENGTH_SHORT).show()
            sendOrderButton.setText(sendOrderButtonText + " (BUY)")
        }

    }
}