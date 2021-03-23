package com.example.currencydemo.data.repository

import androidx.lifecycle.LiveData
import com.example.core.domain.CurrencyInfo
import com.example.currencydemo.data.datasource.CurrencyInfoDataSource
import com.example.currencydemo.domain.CurrencyInfoEntity

/**
 * Created by scordlau on 3/22/21.
 */

class CurrencyInfoRepository(private val dataSource: CurrencyInfoDataSource) {

    suspend fun retrieveAllCurrencyInfo(): LiveData<List<CurrencyInfoEntity>> {
        return dataSource.retrieveAll()
    }

}