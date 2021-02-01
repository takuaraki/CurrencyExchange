package com.example.currencyexchange.models.repository.dao

import androidx.room.Database
import com.example.currencyexchange.models.data.ExchangeRate

@Database(entities = [ExchangeRate::class], version = 1)
abstract class AppDatabase {
    abstract fun exchangeRateDao(): ExchangeRateDao
}