package com.emrehancetin.cs394.Model

data class OwnedCryptoModel(
    val name: String,   // Cryptocurrency name (e.g., Bitcoin)
    val symbol: String, // Cryptocurrency symbol (e.g., BTC)
    var amount: Double  // Owned amount
)
