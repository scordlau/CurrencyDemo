package com.example.currencydemo.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.core.domain.CurrencyInfo
import com.example.currencydemo.data.database.CurrencyInfoDatabase
import com.example.currencydemo.data.datasource.RoomCurrencyInfoDataSource
import com.example.currencydemo.data.repository.CurrencyInfoRepository
import com.example.currencydemo.domain.CurrencyInfoEntity
import kotlinx.coroutines.*

/**
 * Created by scordlau on 3/23/21.
 */

class DemoActivityViewModel(app: Application) : AndroidViewModel(app) {

    private val supervisorJob = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + supervisorJob)

    val isDatabaseReadyToObserve: MutableLiveData<Boolean> = MutableLiveData()
    var databaseCurrencyListLiveData: LiveData<List<CurrencyInfoEntity>>? = null
    val isShowLoading = Transformations.map(isDatabaseReadyToObserve) {
        it == false
    }

    private var currencyInfoRepository: CurrencyInfoRepository? = null

    init {
        val database = CurrencyInfoDatabase.getInstance(getApplication<Application>().applicationContext, coroutineScope)

        currencyInfoRepository = CurrencyInfoRepository(
                RoomCurrencyInfoDataSource(
                        database
                )
        )
    }

    fun loadData() {
        isDatabaseReadyToObserve.value = false
        coroutineScope.launch {
            val list  = currencyInfoRepository?.retrieveAllCurrencyInfo()
            list?.let {
                withContext(Dispatchers.Main) {
                    databaseCurrencyListLiveData = it
                    isDatabaseReadyToObserve.value = true
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        supervisorJob.cancelChildren()
    }

}