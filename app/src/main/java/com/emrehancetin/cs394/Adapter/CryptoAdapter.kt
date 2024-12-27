package com.emrehancetin.cs394.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emrehancetin.cs394.Model.CryptoModel
import com.emrehancetin.cs394.R

class CryptoAdapter(
    private var cryptoList: MutableList<CryptoModel>,
    private val onItemClick: (CryptoModel) -> Unit
) : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {

    class CryptoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.cryptoName)
        val codeTextView: TextView = itemView.findViewById(R.id.cryptoCode)
        val valueTextView: TextView = itemView.findViewById(R.id.cryptoValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_crypto, parent, false)
        return CryptoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val crypto = cryptoList[position]
        holder.nameTextView.text = crypto.name
        holder.codeTextView.text = crypto.code.uppercase()
        holder.valueTextView.text = "$${String.format("%.2f", crypto.value)}"
    }

    override fun getItemCount(): Int = cryptoList.size

    fun updateCryptoList(newList: List<CryptoModel>) {
        cryptoList.clear()
        cryptoList.addAll(newList)
        notifyDataSetChanged()
    }
}

