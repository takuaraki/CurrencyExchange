package com.example.currencyexchange.models.store

import com.example.currencyexchange.models.data.Money
import com.example.currencyexchange.models.repository.CurrencyRepository
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class CurrencyStoreImpl(private val repository: CurrencyRepository) :
    CurrencyStore {
    override val supportedCurrencyCodes: Flow<List<String>>
        get() = repository.exchangeRates.map { exchangeRates ->
            exchangeRates.map { exchangeRate -> exchangeRate.to }
        }
    override val exchanged: Flow<List<Money>>
        get() = combine(
            repository.exchangeRates,
            _amount.asFlow(),
            _currencyCode.asFlow()
        ) { exchangeRates, amount, currencyCode ->
            val selectedExchangeRate =
                exchangeRates.firstOrNull() { exchangeRate -> exchangeRate.to == currencyCode }
                    ?: return@combine emptyList<Money>()
            exchangeRates.map { exchangeRate ->
                Money(
                    amount = amount / selectedExchangeRate.rate * exchangeRate.rate,
                    currencyCode = exchangeRate.to
                )
            }
        }
    private val _amount = ConflatedBroadcastChannel<Int>(value = 0)
    private val _currencyCode = ConflatedBroadcastChannel<String>(value = "USD")

    override suspend fun load() {
        repository.load()
    }

    override suspend fun setAmount(amount: Int) {
        _amount.send(amount)
    }

    override suspend fun selectCurrencyCode(currencyCode: String) {
        _currencyCode.send(currencyCode)
    }
}