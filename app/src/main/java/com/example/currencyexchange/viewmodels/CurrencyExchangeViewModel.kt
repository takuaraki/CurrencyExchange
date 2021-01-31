package com.example.currencyexchange.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.currencyexchange.models.data.Money
import com.example.currencyexchange.models.repository.CurrencyExchangedRepository

class CurrencyExchangeViewModel(
    val currencyExchangeRepository: CurrencyExchangedRepository
): ViewModel() {
    class Factory constructor(
        private val currencyExchangeRepository: CurrencyExchangedRepository
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            CurrencyExchangeViewModel(
                currencyExchangeRepository = currencyExchangeRepository
            ) as T
    }

    val exchanged: LiveData<List<Money>> = currencyExchangeRepository.exchanged.asLiveData()
}