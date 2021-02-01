package com.example.currencyexchange.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.currencyexchange.R
import com.example.currencyexchange.databinding.ActivityMainBinding
import com.example.currencyexchange.models.data.ExchangeRate
import com.example.currencyexchange.models.data.Money
import com.example.currencyexchange.models.repository.ExchangeRateRepository
import com.example.currencyexchange.models.store.CurrencyExchangeStore
import com.example.currencyexchange.models.store.CurrencyExchangeStoreImpl
import com.example.currencyexchange.viewmodels.CurrencyExchangeViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val viewModel = ViewModelProvider(
            this,
            CurrencyExchangeViewModel.Factory(CurrencyExchangeStoreImpl(repository = DebugRepo()))
        ).get(CurrencyExchangeViewModel::class.java)

        val adapter = ExchangedListAdapter()
        binding.exchangedRecyclerView.adapter = adapter
        viewModel.exchanged.observe(this) { exchangedList ->
            adapter.submitList(exchangedList)
        }
    }
}

class DebugRepo : ExchangeRateRepository {
    override val exchangeRates: Flow<List<ExchangeRate>>
        get() = flowOf(
            listOf(
                ExchangeRate(from = "USD", to = "USD", rate = 1f),
                ExchangeRate(from = "USD", to = "JPY", rate = 100f),
                ExchangeRate(from = "USD", to = "AAA", rate = 50f),
                ExchangeRate(from = "USD", to = "BBB", rate = 1000f),
                ExchangeRate(from = "USD", to = "CCC", rate = 25f),
            )
        )
}