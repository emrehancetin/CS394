package com.emrehancetin.cs394.Model

data class OrderHistoryModel(
    val id: String,
    val date: String,
    val amount: Double,
    val type: String // "Deposit" or "Withdraw"
)
