package com.emrehancetin.cs394.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emrehancetin.cs394.Model.OrderHistoryModel

class SharedViewModel : ViewModel() {
    private val _orderHistory = MutableLiveData<MutableList<OrderHistoryModel>>(mutableListOf())
    val orderHistory: LiveData<MutableList<OrderHistoryModel>> get() = _orderHistory

    // Add a new order to the history
    fun addOrder(order: OrderHistoryModel) {
        _orderHistory.value?.add(order)
        _orderHistory.value = _orderHistory.value // Trigger LiveData update
    }
}
