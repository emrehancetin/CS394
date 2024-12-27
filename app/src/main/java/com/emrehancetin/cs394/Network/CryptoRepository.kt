package com.emrehancetin.cs394.Network

import com.emrehancetin.cs394.Model.CryptoModel

class CryptoRepository(private val service: CoinGeckoService) {
    suspend fun fetchCryptos(): List<CryptoModel> {
        return service.getMarketData("usd", "bitcoin,ethereum,solana")
    }
}
