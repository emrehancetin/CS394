package com.emrehancetin.cs394.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emrehancetin.cs394.Model.OrderHistoryModel
import com.emrehancetin.cs394.R
import com.emrehancetin.cs394.databinding.ItemOrderHistoryBinding

class OrderHistoryAdapter(
    private var orderList: MutableList<OrderHistoryModel>
) : RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>() {

    class OrderHistoryViewHolder(val binding: ItemOrderHistoryBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOrderHistoryBinding.inflate(inflater, parent, false)
        return OrderHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val order = orderList[position]
        with(holder.binding) {
            textViewDate.text = order.date
            textViewCryptoName.text = order.cryptoName
            textViewUnitPrice.text = "Unit Price: $${String.format("%.2f", order.unitPrice)}"
            textViewAmount.text = "${order.amount}"
            textViewType.text = order.type

            // Set text color based on Buy/Sell
            textViewType.setTextColor(
                root.resources.getColor(
                    if (order.type == "Buy") R.color.green else R.color.red
                )
            )
        }
    }

    override fun getItemCount(): Int = orderList.size

    fun updateOrders(newOrders: List<OrderHistoryModel>) {
        orderList.clear()
        orderList.addAll(newOrders)
        notifyDataSetChanged()
    }
}
