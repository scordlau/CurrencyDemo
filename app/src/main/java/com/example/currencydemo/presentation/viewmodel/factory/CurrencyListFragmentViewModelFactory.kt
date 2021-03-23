package com.example.currencydemo.presentation.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.domain.CurrencyInfo
import com.example.currencydemo.presentation.viewmodel.CurrencyListFragmentViewModel

/**
 * Created by scordlau on 3/23/21.
 */

class CurrencyListFragmentViewModelFactory(private val app: Application,
                                           private val currencyList: List<CurrencyInfo>) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrencyListFragmentViewModel::class.java)) {
            return CurrencyListFragmentViewModel(app, currencyList) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}