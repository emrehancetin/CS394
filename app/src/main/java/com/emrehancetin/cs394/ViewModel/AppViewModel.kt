package com.emrehancetin.cs394.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emrehancetin.cs394.Model.CryptoModel
import com.emrehancetin.cs394.Model.OrderHistoryModel
import com.emrehancetin.cs394.Network.CryptoRepository
import com.emrehancetin.cs394.Network.NetworkModule
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {

    // Crypto List
    private val _cryptoList = MutableLiveData<List<CryptoModel>>()
    val cryptoList: LiveData<List<CryptoModel>> get() = _cryptoList

    // Order History
    private val _orderHistory = MutableLiveData<MutableList<OrderHistoryModel>>(mutableListOf())
    val orderHistory: LiveData<MutableList<OrderHistoryModel>> get() = _orderHistory

    // Wallet Balance (BTC)
    private val _btcBalance = MutableLiveData<Double>(0.0)
    val btcBalance: LiveData<Double> get() = _btcBalance

    // Cash Balance
    private val _cashBalance = MutableLiveData<Double>(1_000_000.0) // Start with $1,000,000
    val cashBalance: LiveData<Double> get() = _cashBalance

    private val repository = CryptoRepository(NetworkModule.coinGeckoService)

    // Load Cryptos from API
    fun loadCryptos() {
        viewModelScope.launch {
            try {
                val cryptos = repository.fetchCryptos()
                _cryptoList.value = cryptos
                // Log the data to verify
                cryptos.forEach { crypto ->
                    println("Crypto: ${crypto.name} - ${crypto.value}")
                }
            } catch (e: Exception) {
                println("Error fetching cryptos: ${e.message}")
            }
        }
    }

    // Add a new order to the history
    fun addOrder(order: OrderHistoryModel) {
        _orderHistory.value?.add(order)
        _orderHistory.value = _orderHistory.value // Trigger LiveData update
    }

    // Update Wallet and Cash Balances
    fun updateWallet(amount: Double, total: Double, orderType: String) {
        when (orderType) {
            "Buy" -> {
                _btcBalance.value = (_btcBalance.value ?: 0.0) + amount
                _cashBalance.value = (_cashBalance.value ?: 0.0) - total
            }
            "Sell" -> {
                _btcBalance.value = (_btcBalance.value ?: 0.0) - amount
                _cashBalance.value = (_cashBalance.value ?: 0.0) + total
            }
        }
        _btcBalance.value = _btcBalance.value // Trigger LiveData update
        _cashBalance.value = _cashBalance.value // Trigger LiveData update
    }
}
