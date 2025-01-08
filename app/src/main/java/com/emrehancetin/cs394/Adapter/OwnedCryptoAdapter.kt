package com.emrehancetin.cs394.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emrehancetin.cs394.Model.OwnedCryptoModel
import com.emrehancetin.cs394.R

class OwnedCryptoAdapter(
    private var ownedCryptoList: MutableList<OwnedCryptoModel>
) : RecyclerView.Adapter<OwnedCryptoAdapter.OwnedCryptoViewHolder>() {

    class OwnedCryptoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val symbolTextView: TextView = itemView.findViewById(R.id.symbolTextView)
        val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OwnedCryptoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_owned_crypto, parent, false)
        return OwnedCryptoViewHolder(view)
    }

    override fun onBindViewHolder(holder: OwnedCryptoViewHolder, position: Int) {
        val ownedCrypto = ownedCryptoList[position]
        holder.nameTextView.text = ownedCrypto.name
        holder.symbolTextView.text = ownedCrypto.symbol
        holder.amountTextView.text = "Owned: ${String.format("%.6f", ownedCrypto.amount)}"
    }

    override fun getItemCount(): Int = ownedCryptoList.size

    fun updateOwnedCryptoList(newList: List<OwnedCryptoModel>) {
        ownedCryptoList.clear()
        ownedCryptoList.addAll(newList)
        notifyDataSetChanged()
    }
}
