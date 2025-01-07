package com.emrehancetin.cs394.Model

import com.google.gson.annotations.SerializedName

data class CryptoModel(
    @SerializedName("name") val name: String,
    @SerializedName("symbol") val code: String,
    @SerializedName("current_price") val value: Double
)