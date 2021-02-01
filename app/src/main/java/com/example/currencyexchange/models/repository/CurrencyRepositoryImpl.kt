package com.example.currencyexchange.models.repository

import com.example.currencyexchange.models.data.ExchangeRate
import com.example.currencyexchange.models.repository.api.CurrencyAPI
import com.example.currencyexchange.models.repository.api.convert
import com.example.currencyexchange.models.repository.dao.AppDatabase
import kotlinx.coroutines.flow.Flow

class CurrencyRepositoryImpl constructor(
    private val api: CurrencyAPI,
    private val db: AppDatabase
): CurrencyRepository {
    override val exchangeRates: Flow<List<ExchangeRate>>
        get() = db.exchangeRateDao().getExchangeRates()

    override suspend fun load() {
        val exchangeRates = api.getExchangeRates()
        exchangeRates.convert().forEach { exchangeRate ->
            db.exchangeRateDao().insertOfUpdate(exchangeRate)
        }
    }
}