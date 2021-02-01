package com.example.currencyexchange.models.store

import com.example.currencyexchange.models.data.ExchangeRate
import com.example.currencyexchange.models.data.Money
import com.example.currencyexchange.models.repository.CurrencyRepository
import com.example.currencyexchange.models.repository.CurrencyRepositoryImpl
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

import org.junit.Assert.*
import org.junit.Test
import java.util.*

class CurrencyStoreImplTest {

    private lateinit var currencyStore: CurrencyStore
    private lateinit var mockCurrencyRepository: MockCurrencyRepository

    @Before
    fun setUp() {
        mockCurrencyRepository = MockCurrencyRepository()
        currencyStore = CurrencyStoreImpl(
            repository = mockCurrencyRepository
        )

        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun exchangeRates() = runBlocking {
        mockCurrencyRepository.mockExchangeRates = listOf(
            ExchangeRate(from = "USD", to = "AAA", rate = 1000f),
            ExchangeRate(from = "USD", to = "BBB", rate = 200f),
            ExchangeRate(from = "USD", to = "CCC", rate = 50f),
            ExchangeRate(from = "USD", to = "USD", rate = 1f),
        )

        currencyStore.load()
        currencyStore.selectCurrencyCode("BBB")
        currencyStore.setAmount(100)

        val exchanged = currencyStore.exchanged.first()
        assertEquals(4, exchanged.count())
        assertEquals(Money(amount = 500f, currencyCode = "AAA"), exchanged[0])
        assertEquals(Money(amount = 100f, currencyCode = "BBB"), exchanged[1])
        assertEquals(Money(amount = 25f, currencyCode = "CCC"), exchanged[2])
        assertEquals(Money(amount = 0.5f, currencyCode = "USD"), exchanged[3])
    }
}

class MockCurrencyRepository : CurrencyRepository {
    var mockExchangeRates: List<ExchangeRate> = emptyList()
    private val _exchangeRates = ConflatedBroadcastChannel<List<ExchangeRate>>(value = emptyList())

    override val exchangeRates: Flow<List<ExchangeRate>>
        get() = _exchangeRates.asFlow()

    override suspend fun load(currentDate: Date) {
        _exchangeRates.send(mockExchangeRates)
    }
}