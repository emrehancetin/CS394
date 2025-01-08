package com.emrehancetin.cs394.ViewModel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emrehancetin.cs394.Model.CryptoModel
import com.emrehancetin.cs394.Model.OrderHistoryModel
import com.emrehancetin.cs394.Model.OwnedCryptoModel
import com.emrehancetin.cs394.Network.CryptoRepository
import com.emrehancetin.cs394.Network.NetworkModule
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Console

class AppViewModel : ViewModel() {

    // Crypto List
    private val _cryptoList = MutableLiveData<List<CryptoModel>>()
    val cryptoList: LiveData<List<CryptoModel>> get() = _cryptoList

    private val _ownedCryptoList = MutableLiveData<MutableList<OwnedCryptoModel>>(mutableListOf())
    val ownedCryptoList: LiveData<MutableList<OwnedCryptoModel>> get() = _ownedCryptoList

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

    init {
        startRefreshingCryptoData()
        loadOwnedCryptos()
    }

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

    private fun loadOwnedCryptos() {
        // Simulate owned cryptocurrencies for demonstration
        val ownedCryptos = listOf(
            OwnedCryptoModel("Bitcoin", "BTC", 0.0),
            OwnedCryptoModel("Ethereum", "ETH", 0.0),
            OwnedCryptoModel("Solana", "SOL", 0.0)
        )
        _ownedCryptoList.value = ownedCryptos.toMutableList()
    }


    // Periodically refresh crypto data every 10 seconds
    private fun startRefreshingCryptoData() {
        viewModelScope.launch {
            while (true) {
                try {
                    val cryptos = repository.fetchCryptos()
                    _cryptoList.postValue(cryptos)
                } catch (e: Exception) {
                    println("Error refreshing cryptos: ${e.message}")
                }
                delay(30_000)
            }
        }
    }

    // Add a new order to the history
    fun addOrder(order: OrderHistoryModel) {
        _orderHistory.value?.add(order)
        _orderHistory.value = _orderHistory.value // Trigger LiveData update
    }

    fun updateOwnedCrypto(cryptoCode: String, amount: Double, orderType: String) {
        val ownedList = _ownedCryptoList.value ?: mutableListOf()
        val ownedCrypto = ownedList.find { it.symbol.equals(cryptoCode, ignoreCase = true) }

        when (orderType) {
            "Buy" -> {
                if (ownedCrypto != null) {
                    ownedCrypto.amount += amount
                } else {
                    _ownedCryptoList.value?.add(
                        OwnedCryptoModel(
                            name = cryptoList.value?.find { it.code == cryptoCode }?.name ?: cryptoCode,
                            symbol = cryptoCode,
                            amount = amount
                        )
                    )
                }
            }
            "Sell" -> {
                if (ownedCrypto != null) {
                    ownedCrypto.amount -= amount
                    if (ownedCrypto.amount <= 0) {
                        _ownedCryptoList.value?.remove(ownedCrypto)
                    }
                }
            }
        }
        _ownedCryptoList.value = ownedList // Trigger LiveData update
    }

    // Update Wallet and Cash Balances
    fun updateWallet(cryptoCode: String, amount: Double, total: Double, orderType: String) {
        if (orderType == "Buy" && (_cashBalance.value ?: 0.0) >= total) {
            updateOwnedCrypto(cryptoCode, amount, orderType)
            _cashBalance.value = (_cashBalance.value ?: 0.0) - total
        } else if (orderType == "Sell") {
            updateOwnedCrypto(cryptoCode, amount, orderType)
            _cashBalance.value = (_cashBalance.value ?: 0.0) + total
        }
    }
}
