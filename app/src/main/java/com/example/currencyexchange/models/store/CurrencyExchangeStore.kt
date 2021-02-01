package com.example.currencyexchange.models.store

import com.example.currencyexchange.models.data.Money
import kotlinx.coroutines.flow.Flow

interface CurrencyExchangeStore {
    val exchanged: Flow<List<Money>>

    suspend fun setAmount(amount: Int)
    suspend fun selectCurrencyCode(currencyCode: String)
}