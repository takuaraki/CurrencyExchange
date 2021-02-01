package com.example.currencyexchange.models.repository

import com.example.currencyexchange.models.data.ExchangeRate
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    val exchangeRates: Flow<List<ExchangeRate>>

    suspend fun load()
}