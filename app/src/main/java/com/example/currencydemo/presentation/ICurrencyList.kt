package com.example.currencydemo.presentation

import com.example.currencydemo.presentation.abstraction.ICurrencyListParent

/**
 * Created by scordlau on 3/23/21.
 */

interface ICurrencyList {

    fun register(parent: ICurrencyListParent)

    fun sort()

}