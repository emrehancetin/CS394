package com.emrehancetin.cs394.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emrehancetin.cs394.Model.CryptoModel
import com.emrehancetin.cs394.databinding.ItemCryptoBinding

class CryptoAdapter(
    private var cryptoList: MutableList<CryptoModel>,
    private val onItemClick: (CryptoModel) -> Unit
) : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {

    class CryptoViewHolder(val binding: ItemCryptoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        // Inflate the layout using View Binding
        val binding = ItemCryptoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CryptoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val crypto = cryptoList[position]
        with(holder.binding) {
            // Bind your data here
            cryptoName.text = crypto.name
            cryptoCode.text = crypto.code.uppercase()
            cryptoValue.text = "Price: $${String.format("%.2f", crypto.value)}"

            // Handle item click
            root.setOnClickListener {
                onItemClick(crypto)
            }
        }
    }

    override fun getItemCount(): Int = cryptoList.size

    fun updateCryptoList(newList: List<CryptoModel>) {
        cryptoList.clear()
        cryptoList.addAll(newList)
        notifyDataSetChanged()
    }
}
