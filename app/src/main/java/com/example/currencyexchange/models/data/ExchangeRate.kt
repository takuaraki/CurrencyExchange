package com.example.currencyexchange.models.data

import androidx.room.Entity

@Entity(tableName = "exchangeRate", primaryKeys = ["from", "to"])
data class ExchangeRate (
    val from: String,
    val to: String,
    val rate: Float
)