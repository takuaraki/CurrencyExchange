package com.example.currencyexchange.models.repository.api

data class ExchangeRatesResponse(
    val quotes: Map<String, Float>
)