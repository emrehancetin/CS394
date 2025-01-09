package com.emrehancetin.cs394.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emrehancetin.cs394.Model.CryptoModel
import com.emrehancetin.cs394.Model.OrderHistoryModel
import com.emrehancetin.cs394.Model.OwnedCryptoModel
import com.emrehancetin.cs394.Network.CryptoRepository
import com.emrehancetin.cs394.Network.NetworkModule
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class AppViewModel : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth
    private var db: FirebaseFirestore = Firebase.firestore


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
        fetchWalletFromFirestore()
        fetchHistoryFromFirestore()
        startRefreshingCryptoData()
        loadOwnedCryptos()

    }

    public fun fetchWalletFromFirestore() {
        val userEmail = auth.currentUser?.email.toString()
        db.collection("Wallets")
            .whereEqualTo("email", userEmail)
            .addSnapshotListener { value, _ ->
                val wallet = value?.documents?.firstOrNull()?.getDouble("wallet") ?: 0.0
                _cashBalance.value = wallet
            }
    }
    public fun fetchHistoryFromFirestore(){
        _orderHistory.value?.clear()
        val userEmail = auth.currentUser?.email.toString()
        db.collection("Transactions").whereEqualTo("email",userEmail).orderBy("date", Query.Direction.DESCENDING).addSnapshotListener{ value, _->
            if (value != null) {
                for(document in value.documents){
                    val id = document.get("id") as String
                    val date = document.get("date") as String
                    val code = document.get("code") as String
                    val amount = document.getDouble("amount") as Double
                    val valuE = document.getDouble("value") as Double
                    val orderType = document.get("orderType") as String
                    val oldOrder = OrderHistoryModel(
                        id = id,
                        date = date,
                        cryptoName = code,
                        unitPrice = valuE,
                        amount = amount,
                        type = orderType
                    )
                    _orderHistory.value?.add(oldOrder)


                }

            }

        }
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
        val userEmail = auth.currentUser?.email.toString()
        db.collection("OwnedCrypto").whereEqualTo("email",userEmail).get().addOnSuccessListener {documents ->
            if (documents != null && !documents.isEmpty) {
                for (document in documents) {
                    val ownedCryptos = listOf(
                        OwnedCryptoModel("Bitcoin", "BTC", document.getDouble("BTC") as Double),
                        OwnedCryptoModel("Ethereum", "ETH", document.getDouble("ETH") as Double),
                        OwnedCryptoModel("Solana", "SOL", document.getDouble("SOL") as Double)
                    )
                    _ownedCryptoList.value = ownedCryptos.toMutableList()
                }
            }
        }.addOnFailureListener { exception ->
            println(exception.localizedMessage)
        }


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
        val userEmail = auth.currentUser?.email.toString()
        _orderHistory.value?.add(order)
        _orderHistory.value = _orderHistory.value // Trigger LiveData update

        //db process
            val postMap = hashMapOf<String,Any>()
            postMap.put("id",order.id.toString())
            postMap.put("email",userEmail)
            postMap.put("date",order.date.toString())
            postMap.put("code",order.cryptoName.toString())
            postMap.put("amount",order.amount)
            postMap.put("value",order.unitPrice)
            postMap.put("orderType",order.type.toString())

            db.collection("Transactions").add(postMap).addOnSuccessListener { documentReference ->
                println("db process is successful")
            }.addOnFailureListener { exception ->
                println(exception.localizedMessage)
            }



    }

    fun updateOwnedCrypto(cryptoCode: String, amount: Double, orderType: String, currentUser: FirebaseUser?) {
        val ownedList = _ownedCryptoList.value ?: mutableListOf()
        val ownedCrypto = ownedList.find { it.symbol.equals(cryptoCode, ignoreCase = true) }
        val userEmail = currentUser?.email
        when (orderType) {
            "Buy" -> {
                if (ownedCrypto != null) {
                    ownedCrypto.amount += amount

                    db.collection("OwnedCrypto")
                        .whereEqualTo("email", userEmail.toString())
                        .get()
                        .addOnSuccessListener { value ->
                            if (value != null && !value.isEmpty) {
                                for (document in value.documents) {
                                    if(cryptoCode == "btc"){
                                        db.collection("OwnedCrypto").document(document.id)
                                            .update("BTC", ownedCrypto.amount)
                                            .addOnSuccessListener {
                                                println("OwnedCrypto - btc - updated successfully!")
                                            }
                                            .addOnFailureListener { e ->
                                                println("Error updating OwnedCrypto: ${e.message}")
                                            }
                                    }else if(cryptoCode== "eth"){
                                        db.collection("OwnedCrypto").document(document.id)
                                            .update("ETH", ownedCrypto.amount)
                                            .addOnSuccessListener {
                                                println("OwnedCrypto - eth - updated successfully!")
                                            }
                                            .addOnFailureListener { e ->
                                                println("Error updating OwnedCrypto: ${e.message}")
                                            }
                                    }else if(cryptoCode=="sol"){
                                        db.collection("OwnedCrypto").document(document.id)
                                            .update("SOL", ownedCrypto.amount)
                                            .addOnSuccessListener {
                                                println("OwnedCrypto - sol - updated successfully!")
                                            }
                                            .addOnFailureListener { e ->
                                                println("Error updating OwnedCrypto: ${e.message}")
                                            }
                                    }

                                }
                            } else {
                                println("No matching document found for email: $userEmail")
                            }
                        }
                        .addOnFailureListener { e ->
                            println("Error fetching OwnedCrypto: ${e.message}")
                        }
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
                    /*if (ownedCrypto.amount <= 0) {
                        _ownedCryptoList.value?.remove(ownedCrypto)
                    }*/
                    db.collection("OwnedCrypto")
                        .whereEqualTo("email", userEmail.toString())
                        .get()
                        .addOnSuccessListener { value ->
                            if (value != null && !value.isEmpty) {
                                for (document in value.documents) {
                                    if(cryptoCode == "btc"){
                                        db.collection("OwnedCrypto").document(document.id)
                                            .update("BTC", ownedCrypto.amount)
                                            .addOnSuccessListener {
                                                println("OwnedCrypto - btc - updated successfully!")
                                            }
                                            .addOnFailureListener { e ->
                                                println("Error updating OwnedCrypto: ${e.message}")
                                            }
                                    }else if(cryptoCode== "eth"){
                                        db.collection("OwnedCrypto").document(document.id)
                                            .update("ETH", ownedCrypto.amount)
                                            .addOnSuccessListener {
                                                println("OwnedCrypto - eth - updated successfully!")
                                            }
                                            .addOnFailureListener { e ->
                                                println("Error updating OwnedCrypto: ${e.message}")
                                            }
                                    }else if(cryptoCode=="sol"){
                                        db.collection("OwnedCrypto").document(document.id)
                                            .update("SOL", ownedCrypto.amount)
                                            .addOnSuccessListener {
                                                println("OwnedCrypto - sol - updated successfully!")
                                            }
                                            .addOnFailureListener { e ->
                                                println("Error updating OwnedCrypto: ${e.message}")
                                            }
                                    }

                                }
                            } else {
                                println("No matching document found for email: $userEmail")
                            }
                        }
                        .addOnFailureListener { e ->
                            println("Error fetching OwnedCrypto: ${e.message}")
                        }
                }
            }
        }
        _ownedCryptoList.value = ownedList // Trigger LiveData update
    }

    // Update Wallet and Cash Balances
    fun updateWallet(
        cryptoCode: String,
        amount: Double,
        total: Double,
        orderType: String,
        currentUser: FirebaseUser?
    ) {
        val userEmail= currentUser?.email
        if (orderType == "Buy" && (_cashBalance.value ?: 0.0) >= total) {
            updateOwnedCrypto(cryptoCode, amount, orderType,currentUser)
            _cashBalance.value = (_cashBalance.value ?: 0.0) - total
            db.collection("Wallets")
                .whereEqualTo("email", userEmail.toString())
                .get()
                .addOnSuccessListener { value ->
                    if (value != null && !value.isEmpty) {
                        for (document in value.documents) {
                            db.collection("Wallets").document(document.id)
                                .update("wallet", _cashBalance.value)
                                .addOnSuccessListener {
                                    println("Wallet updated successfully!")
                                }
                                .addOnFailureListener { e ->
                                    println("Error updating wallet: ${e.message}")
                                }
                        }
                    } else {
                        println("No matching document found for email: $userEmail")
                    }
                }
                .addOnFailureListener { e ->
                    println("Error fetching wallet: ${e.message}")
                }

        } else if (orderType == "Sell") {
            updateOwnedCrypto(cryptoCode, amount, orderType,currentUser)
            _cashBalance.value = (_cashBalance.value ?: 0.0) + total

            db.collection("Wallets")
                .whereEqualTo("email", userEmail.toString())
                .get()
                .addOnSuccessListener { value ->
                    if (value != null && !value.isEmpty) {
                        for (document in value.documents) {
                            db.collection("Wallets").document(document.id)
                                .update("wallet", _cashBalance.value)
                                .addOnSuccessListener {
                                    println("Wallet updated successfully!")
                                }
                                .addOnFailureListener { e ->
                                    println("Error updating wallet: ${e.message}")
                                }
                        }
                    } else {
                        println("No matching document found for email: $userEmail")
                    }
                }
                .addOnFailureListener { e ->
                    println("Error fetching wallet: ${e.message}")
                }
        }
    }
}
