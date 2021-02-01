package com.example.currencyexchange.models.repository

import com.example.currencyexchange.models.data.ExchangeRate
import com.example.currencyexchange.models.repository.dao.AppDatabase
import kotlinx.coroutines.flow.Flow

class ExchangeRateRepositoryImpl constructor(
    private val db: AppDatabase
): ExchangeRateRepository {
    override val exchangeRates: Flow<List<ExchangeRate>>
        get() = db.exchangeRateDao().getExchangeRates()
}