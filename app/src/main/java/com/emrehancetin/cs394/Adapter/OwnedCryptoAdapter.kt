package com.emrehancetin.cs394.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emrehancetin.cs394.Model.OwnedCryptoModel
import com.emrehancetin.cs394.R
import com.emrehancetin.cs394.databinding.ItemOwnedCryptoBinding

class OwnedCryptoAdapter(
    private var ownedCryptoList: MutableList<OwnedCryptoModel>
) : RecyclerView.Adapter<OwnedCryptoAdapter.OwnedCryptoViewHolder>() {

    class OwnedCryptoViewHolder(val binding: ItemOwnedCryptoBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OwnedCryptoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOwnedCryptoBinding.inflate(inflater, parent, false)
        return OwnedCryptoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OwnedCryptoViewHolder, position: Int) {
        val ownedCrypto = ownedCryptoList[position]
        with(holder.binding) {
            nameTextView.text = ownedCrypto.name
            symbolTextView.text = ownedCrypto.symbol
            amountTextView.text = "Owned: ${String.format("%.6f", ownedCrypto.amount)}"
        }
    }

    override fun getItemCount(): Int = ownedCryptoList.size

    fun updateOwnedCryptoList(newList: List<OwnedCryptoModel>) {
        ownedCryptoList.clear()
        ownedCryptoList.addAll(newList)
        notifyDataSetChanged()
    }
}
