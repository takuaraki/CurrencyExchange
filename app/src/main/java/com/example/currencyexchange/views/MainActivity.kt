package com.example.currencyexchange.views

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.room.Room
import com.example.currencyexchange.R
import com.example.currencyexchange.databinding.ActivityMainBinding
import com.example.currencyexchange.models.data.ExchangeRate
import com.example.currencyexchange.models.repository.CurrencyRepository
import com.example.currencyexchange.models.repository.CurrencyRepositoryImpl
import com.example.currencyexchange.models.repository.api.CurrencyAPI
import com.example.currencyexchange.models.repository.api.ExchangeRatesResponse
import com.example.currencyexchange.models.repository.dao.AppDatabase
import com.example.currencyexchange.models.store.CurrencyStoreImpl
import com.example.currencyexchange.viewmodels.CurrencyExchangeViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: CurrencyExchangeViewModel
    private lateinit var adapter: ExchangedListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider(
            this,
            CurrencyExchangeViewModel.Factory(
                CurrencyStoreImpl(
                    repository = CurrencyRepositoryImpl(
                        api = DebugAPI(),
                        db = Room.databaseBuilder(
                            this,
                            AppDatabase::class.java,
                            "database"
                        ).build(),
                        preferences = getSharedPreferences("currencyExchange", Context.MODE_PRIVATE)
                    )
                )
            )
        ).get(CurrencyExchangeViewModel::class.java)

        adapter = ExchangedListAdapter()
        binding.exchangedRecyclerView.adapter = adapter

        setInputs()
        handleOutputs()
    }

    private fun setInputs() {
        binding.amountEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.onAmountTextChanged(text.toString())
        }

        binding.currencySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val spinner: Spinner = parent as Spinner
                    val code: String = spinner.selectedItem as String
                    viewModel.onSelectCurrencyCode(code)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
    }

    private fun handleOutputs() {
        viewModel.supportedCurrencyCodes.observe(this) { currencyCodes ->
            binding.currencySpinner.adapter =
                ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currencyCodes)
        }

        viewModel.exchanged.observe(this) { exchangedList ->
            adapter.submitList(exchangedList)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }
}

class DebugAPI: CurrencyAPI {
    override suspend fun getExchangeRates(): ExchangeRatesResponse {
        return ExchangeRatesResponse(quotes = mapOf(
            "USDUSD" to 1f,
            "USDJPY" to 100f,
            "USDAAA" to 50f,
            "USDBBB" to 1000f,
            "USDCCC" to 25f,
            "USDDDD" to 10f,
            "USDEEE" to 5f,
        ))
    }
}