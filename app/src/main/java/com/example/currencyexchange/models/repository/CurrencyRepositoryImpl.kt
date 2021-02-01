package com.example.currencyexchange.models.repository

import android.content.SharedPreferences
import com.example.currencyexchange.models.data.ExchangeRate
import com.example.currencyexchange.models.repository.api.CurrencyAPI
import com.example.currencyexchange.models.repository.api.convert
import com.example.currencyexchange.models.repository.dao.AppDatabase
import kotlinx.coroutines.flow.Flow
import java.util.*

class CurrencyRepositoryImpl constructor(
    private val api: CurrencyAPI,
    private val db: AppDatabase,
    private val preferences: SharedPreferences
): CurrencyRepository {
    private val keyLastCachedTime = "keyLastCachedTime"
    private val cacheTime: Long = 30 * 60 * 1000 // 30 minutes

    override val exchangeRates: Flow<List<ExchangeRate>>
        get() = db.exchangeRateDao().getExchangeRates()

    override suspend fun load(currentDate: Date) {
        val lastCachedTime = preferences.getLong(keyLastCachedTime, 0)
        if (currentDate.time - lastCachedTime >  cacheTime) {
            val exchangeRates = api.getExchangeRates()
            exchangeRates.convert().forEach { exchangeRate ->
                db.exchangeRateDao().insertOfUpdate(exchangeRate)
            }
            preferences.edit().putLong(keyLastCachedTime, currentDate.time).apply()
        }
    }
}