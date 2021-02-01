package com.example.currencyexchange.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.currencyexchange.models.data.Money
import com.example.currencyexchange.models.store.CurrencyStore

class CurrencyExchangeViewModel(
    private val store: CurrencyStore
): ViewModel() {
    class Factory constructor(
        private val store: CurrencyStore
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            CurrencyExchangeViewModel(
                store = store
            ) as T
    }

    val exchanged: LiveData<List<Money>> = store.exchanged.asLiveData()

    fun onAmountTextChanged(text: String) {
    }

    fun onSelectCurrencyCode(code: String) {

    }
}