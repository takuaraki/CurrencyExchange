package com.example.currencyexchange.models.repository

import com.example.currencyexchange.models.data.Money
import kotlinx.coroutines.flow.Flow

interface CurrencyExchangedRepository {
    val exchanged: Flow<List<Money>>
}