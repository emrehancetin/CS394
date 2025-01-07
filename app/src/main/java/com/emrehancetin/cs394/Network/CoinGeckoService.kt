package com.emrehancetin.cs394.Network

import com.emrehancetin.cs394.Model.CryptoModel
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoService {
    @GET("coins/markets")
    suspend fun getMarketData(
        @Query("vs_currency") currency: String,
        @Query("ids") ids: String
    ): List<CryptoModel>
}
