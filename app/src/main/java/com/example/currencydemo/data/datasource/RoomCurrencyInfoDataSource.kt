package com.example.currencydemo.data.datasource

import androidx.lifecycle.LiveData
import com.example.core.domain.CurrencyInfo
import com.example.currencydemo.data.database.CurrencyInfoDatabase
import com.example.currencydemo.domain.CurrencyInfoEntity
import com.example.currencydemo.domain.toCurrencyInfo
import com.example.currencydemo.domain.toCurrencyInfoEntity

class RoomCurrencyInfoDataSource(private val instance: CurrencyInfoDatabase) : CurrencyInfoDataSource {

    override suspend fun add(item: CurrencyInfo) {
        instance.currencyInfoDao().insert(item.toCurrencyInfoEntity())
    }

    override suspend fun addAll(list: List<CurrencyInfo>) {
        instance.currencyInfoDao().insertAll(
                list.map { it.toCurrencyInfoEntity() }
        )
    }

    override suspend fun retrieveAll(): LiveData<List<CurrencyInfoEntity>> {
        return instance.currencyInfoDao().getAll()
    }
}
