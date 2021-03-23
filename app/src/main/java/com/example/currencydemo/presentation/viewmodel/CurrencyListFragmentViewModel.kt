package com.example.currencydemo.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.core.domain.CurrencyInfo
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by scordlau on 3/23/21.
 */

class CurrencyListFragmentViewModel(app: Application,
                                    currencyList: List<CurrencyInfo>) : AndroidViewModel(app) {

    val currencyInfoAdapterItemClick = MutableLiveData<CurrencyInfo>()
    val data = currencyList

}