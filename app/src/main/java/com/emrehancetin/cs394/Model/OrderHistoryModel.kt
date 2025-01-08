package com.emrehancetin.cs394.Model

data class OrderHistoryModel(
    val id: String,
    val date: String,
    val cryptoName: String,
    val unitPrice: Double,
    val amount: Double,
    val type: String
)
