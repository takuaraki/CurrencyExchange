package com.example.currencyexchange.models.repository

import com.example.currencyexchange.mock.MockSharedPreferences
import com.example.currencyexchange.models.data.ExchangeRate
import com.example.currencyexchange.models.repository.api.CurrencyAPI
import com.example.currencyexchange.models.repository.api.ExchangeRatesResponse
import com.example.currencyexchange.models.repository.dao.ExchangeRateDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import java.util.*

class CurrencyRepositoryImplTest {
    private lateinit var currencyRepository: CurrencyRepository
    private lateinit var mockApi: MockCurrencyAPI
    private lateinit var mockDao: MockCurrencyDao
    private lateinit var mockSharedPreferences: MockSharedPreferences

    @Before
    fun setUp() {
        mockApi = MockCurrencyAPI()
        mockDao = MockCurrencyDao()
        mockSharedPreferences = MockSharedPreferences()
        currencyRepository = CurrencyRepositoryImpl(
            api = mockApi,
            dao = mockDao,
            preferences = mockSharedPreferences
        )

        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getExchangeRates_updateCache() = runBlocking {
        // prepare mock data
        mockApi.mockExchangeRatesResponse = ExchangeRatesResponse(
            quotes = mapOf(
                "USDAED" to 5f,
                "USDAFN" to 100f,
            )
        )
        listOf(
            ExchangeRate(from = "USD", to = "AED", rate = 1f),
            ExchangeRate(from = "USD", to = "AFN", rate = 2f),
        ).forEach {
            mockDao.insertOrUpdate(it)
        }
        mockSharedPreferences.longValue = 0

        // execute
        currencyRepository.load()

        // validate
        val exchangeRates = currencyRepository.exchangeRates.first()
        assertEquals(2, exchangeRates.count())
        assertEquals(ExchangeRate(from = "USD", to = "AED", rate = 5f), exchangeRates[0])
        assertEquals(ExchangeRate(from = "USD", to = "AFN", rate = 100f), exchangeRates[1])
    }

    @Test
    fun getExchangeRates_notUpdateCache() = runBlocking {
        // prepare mock data
        mockApi.mockExchangeRatesResponse = ExchangeRatesResponse(
            quotes = mapOf(
                "USDAED" to 5f,
                "USDAFN" to 100f,
            )
        )
        listOf(
            ExchangeRate(from = "USD", to = "AED", rate = 1f),
            ExchangeRate(from = "USD", to = "AFN", rate = 2f),
        ).forEach {
            mockDao.insertOrUpdate(it)
        }
        val currentDate = Date()
        mockSharedPreferences.longValue = currentDate.time + 30 * 60 * 1000 + 1 // after 30 min

        // execute
        currencyRepository.load(currentDate)

        // validate
        val exchangeRates = currencyRepository.exchangeRates.first()
        assertEquals(2, exchangeRates.count())
        assertEquals(ExchangeRate(from = "USD", to = "AED", rate = 1f), exchangeRates[0])
        assertEquals(ExchangeRate(from = "USD", to = "AFN", rate = 2f), exchangeRates[1])
    }
}

class MockCurrencyAPI : CurrencyAPI {
    var mockExchangeRatesResponse: ExchangeRatesResponse = ExchangeRatesResponse(quotes = mapOf())

    override suspend fun getExchangeRates(): ExchangeRatesResponse {
        return mockExchangeRatesResponse
    }
}

class MockCurrencyDao : ExchangeRateDao {
    var exchangeRates: MutableList<ExchangeRate> = mutableListOf()
    private val _exchangeRates = ConflatedBroadcastChannel<List<ExchangeRate>>(value = emptyList())

    override fun getExchangeRates(): Flow<List<ExchangeRate>> {
        return _exchangeRates.asFlow()
    }

    override suspend fun insertOrUpdate(exchangeRate: ExchangeRate) {
        val index = exchangeRates.indexOfFirst { it.to == exchangeRate.to }
        if (index < 0) {
            // insert
            exchangeRates.add(exchangeRate)
        } else {
            // update
            exchangeRates[index] = exchangeRate
        }
        _exchangeRates.send(exchangeRates)
    }
}