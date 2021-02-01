package com.example.currencyexchange.models.repository

import com.example.currencyexchange.models.data.ExchangeRate
import kotlinx.coroutines.flow.Flow

interface ExchangeRateRepository {
    val exchangeRates: Flow<List<ExchangeRate>>
}