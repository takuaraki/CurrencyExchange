package com.example.currencyexchange.models.repository.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencyexchange.models.data.ExchangeRate
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeRateDao {
    @Query("SELECT * from exchangeRate ORDER BY `to`")
    fun getExchangeRates(): Flow<List<ExchangeRate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(exchangeRate: ExchangeRate)
}