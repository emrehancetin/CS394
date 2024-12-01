package com.emrehancetin.cs394.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emrehancetin.cs394.Model.CryptoModel
import com.emrehancetin.cs394.R

class CryptoAdapter(
    private val cryptoList: List<CryptoModel>,
    private val onItemClick: (CryptoModel) -> Unit
) : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {

    // ViewHolder class to hold the views for each item
    class CryptoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.cryptoName)
        val codeTextView: TextView = itemView.findViewById(R.id.cryptoCode)
        val valueTextView: TextView = itemView.findViewById(R.id.cryptoValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        // Inflate the item layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_crypto, parent, false)
        return CryptoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        // Bind data to the views
        val crypto = cryptoList[position]
        holder.nameTextView.text = crypto.name
        holder.codeTextView.text = crypto.code
        holder.valueTextView.text = "$${crypto.value}"

        // Set click listener for the item
        holder.itemView.setOnClickListener {
            onItemClick(crypto)
        }
    }

    override fun getItemCount(): Int {
        // Return the total count of items
        return cryptoList.size
    }
}
