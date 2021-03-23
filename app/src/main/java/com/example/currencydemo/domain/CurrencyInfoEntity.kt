package com.example.currencydemo.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.domain.CurrencyInfo

/**
 * Created by scordlau on 3/22/21.
 */

@Entity(tableName = "currencyTable")
data class CurrencyInfoEntity(@PrimaryKey val id: String,
                              val name: String,
                              val symbol: String) {
}

fun CurrencyInfoEntity.toCurrencyInfo(): CurrencyInfo {
    return CurrencyInfo(id, name, symbol)
}

fun CurrencyInfo.toCurrencyInfoEntity(): CurrencyInfoEntity {
    return CurrencyInfoEntity(id, name, symbol)
}