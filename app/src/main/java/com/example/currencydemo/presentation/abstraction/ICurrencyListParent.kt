package com.example.currencydemo.presentation.abstraction

import com.example.core.domain.CurrencyInfo

/**
 * Created by scordlau on 3/23/21.
 */

interface ICurrencyListParent {

    fun onCurrencyItemClick(data: CurrencyInfo)

}