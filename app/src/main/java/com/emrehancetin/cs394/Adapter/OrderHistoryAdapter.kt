package com.emrehancetin.cs394.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emrehancetin.cs394.Model.OrderHistoryModel
import com.emrehancetin.cs394.R

class OrderHistoryAdapter(
    private var orderList: MutableList<OrderHistoryModel>
) : RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>() {

    class OrderHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.textViewDate)
        val amountTextView: TextView = itemView.findViewById(R.id.textViewAmount)
        val typeTextView: TextView = itemView.findViewById(R.id.textViewType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_history, parent, false)
        return OrderHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val order = orderList[position]
        holder.dateTextView.text = order.date
        holder.amountTextView.text = "${order.amount}"
        holder.typeTextView.text = order.type
        if (order.type == "Buy") {
            holder.typeTextView.setTextColor(holder.itemView.resources.getColor(R.color.green))
        } else {
            holder.typeTextView.setTextColor(holder.itemView.resources.getColor(R.color.red))
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    fun updateOrders(newOrders: List<OrderHistoryModel>) {
        orderList.clear()
        orderList.addAll(newOrders)
        notifyDataSetChanged()
    }
}
