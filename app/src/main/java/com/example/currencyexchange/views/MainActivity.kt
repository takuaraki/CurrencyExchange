package com.example.currencyexchange.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.currencyexchange.R
import com.example.currencyexchange.databinding.ActivityMainBinding
import com.example.currencyexchange.models.data.Money
import com.example.currencyexchange.models.repository.ExchangeRateRepository
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
            CurrencyExchangeViewModel.Factory(DebugRepo())
        ).get(CurrencyExchangeViewModel::class.java)

        val adapter = ExchangedListAdapter()
        binding.exchangedRecyclerView.adapter = adapter
        viewModel.exchanged.observe(this) { exchangedList ->
            adapter.submitList(exchangedList)
        }
    }
}

class DebugRepo : ExchangeRateRepository {
    override val exchangeRates: Flow<List<Money>>
        get() = flowOf(
            listOf(
                Money(amount = 100, currencyCode = "JPY"),
                Money(amount = 1, currencyCode = "USD"),
                Money(amount = 300, currencyCode = "AAA"),
                Money(amount = 1000, currencyCode = "BBB"),
                Money(amount = 50, currencyCode = "CCC"),
                Money(amount = 25, currencyCode = "DDD"),
            )
        )
}