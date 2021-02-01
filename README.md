# CurrencyExchange

## Access Key
CurrencyAPI.kt
```
.addQueryParameter("access_key", "<your access key>")
```

## Architecture
This project is architected by MVVM (Model View ViewModel).
- Model
  - CurrencyStore
  - CurrencyRepository
  - CurrencyAPI
  - ExchangedRateDao
- View
  - CurrencyExchangeActivity
- ViewModel
  - CurrencyExchangeViewModel
