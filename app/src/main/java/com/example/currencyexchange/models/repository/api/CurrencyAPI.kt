package com.example.currencyexchange.models.repository.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CurrencyAPI {
    @GET("/live")
    suspend fun getExchangeRates(): ExchangeRatesResponse

    companion object {
        private const val BASE_URL = "http://api.currencylayer.com"

        fun create(): CurrencyAPI {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(object : Interceptor {
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val url = chain.request().url
                            .newBuilder()
                            .addQueryParameter("access_key", "<your access key>")
                            .build()
                        val request = chain.request().newBuilder().url(url).build()
                        return chain.proceed(request)
                    }
                })
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CurrencyAPI::class.java)
        }
    }
}