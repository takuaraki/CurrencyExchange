package com.example.currencyexchange.models.repository.api

import com.example.currencyexchange.models.data.ExchangeRate

data class ExchangeRatesResponse(
    val quotes: Map<String, Float>
)

fun ExchangeRatesResponse.convert(): List<ExchangeRate> {
    return quotes.map { entry ->
        ExchangeRate(
            from = entry.key.substring(0..2),
            to = entry.key.substring(3..5),
            rate = entry.value
        )
    }
}