package com.example.currencyexchange.models.repository.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.currencyexchange.models.data.ExchangeRate

@Database(entities = [ExchangeRate::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exchangeRateDao(): ExchangeRateDao
}