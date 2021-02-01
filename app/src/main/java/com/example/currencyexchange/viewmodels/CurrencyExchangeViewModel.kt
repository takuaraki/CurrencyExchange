package com.example.currencyexchange.viewmodels

import androidx.lifecycle.*
import com.example.currencyexchange.models.data.Money
import com.example.currencyexchange.models.store.CurrencyStore
import kotlinx.coroutines.launch

class CurrencyExchangeViewModel(
    private val store: CurrencyStore
) : ViewModel() {
    class Factory constructor(
        private val store: CurrencyStore
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            CurrencyExchangeViewModel(
                store = store
            ) as T
    }

    val supportedCurrencyCodes: LiveData<List<String>> = store.supportedCurrencyCodes.asLiveData()
    val exchanged: LiveData<List<Money>> = store.exchanged.asLiveData()

    fun onAmountTextChanged(text: String) {
        text.toIntOrNull()?.also { amount ->
            viewModelScope.launch {
                store.setAmount(amount)
            }
        }
    }

    fun onSelectCurrencyCode(code: String) {
        viewModelScope.launch {
            store.selectCurrencyCode(code)
        }
    }
}