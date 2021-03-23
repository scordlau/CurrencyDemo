package com.example.currencydemo.data.datasource

import androidx.lifecycle.LiveData
import com.example.core.domain.CurrencyInfo
import com.example.currencydemo.domain.CurrencyInfoEntity

interface CurrencyInfoDataSource {

    suspend fun add(item: CurrencyInfo)

    suspend fun addAll(list: List<CurrencyInfo>)

    suspend fun retrieveAll(): LiveData<List<CurrencyInfoEntity>>

}
