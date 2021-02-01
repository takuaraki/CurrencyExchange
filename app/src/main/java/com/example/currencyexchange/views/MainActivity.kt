package com.example.currencyexchange.views

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
                        api = CurrencyAPI.create(),
                        db = Room.databaseBuilder(
                            this,
                            AppDatabase::class.java,
                            "database"
                        ).build()
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

class DebugRepo : CurrencyRepository {
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

    override suspend fun load() {
        // do nothing
    }
}