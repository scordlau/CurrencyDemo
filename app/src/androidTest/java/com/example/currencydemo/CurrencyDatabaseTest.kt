package com.example.currencydemo

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.core.domain.CurrencyInfo
import com.example.currencydemo.data.dao.CurrencyInfoDao
import com.example.currencydemo.data.database.CurrencyInfoDatabase
import com.example.currencydemo.data.datasource.RoomCurrencyInfoDataSource
import com.example.currencydemo.data.repository.CurrencyInfoRepository
import com.example.currencydemo.domain.CurrencyInfoEntity
import com.example.currencydemo.domain.toCurrencyInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws

/**
 * Created by scordlau on 3/22/21.
 */

@RunWith(AndroidJUnit4::class)
class CurrencyDatabaseTest {

    private lateinit var database: CurrencyInfoDatabase
    private lateinit var currencyInfoDao: CurrencyInfoDao

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, CurrencyInfoDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        currencyInfoDao = database.currencyInfoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun testNothing() {

    }

    @Test
    fun testRepositoryLoadFromDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val gson = Gson()
        val inputStream = context.resources.openRawResource(R.raw.default_currency_info_list)
                .bufferedReader()
                .use { it.readText() }
        val type = object : TypeToken<List<CurrencyInfo>>() {}.type
        val result = gson.fromJson<List<CurrencyInfoEntity>>(inputStream, type)
        val repository = CurrencyInfoRepository(RoomCurrencyInfoDataSource(CurrencyInfoDatabase.getInstance(context, CoroutineScope(Dispatchers.Main))))

        CoroutineScope(Dispatchers.Main).launch {
            val currencyListFromDB = repository.retrieveAllCurrencyInfo()
            currencyListFromDB.observeForever(Observer {
                if (it.isNotEmpty()) assert(it.map { it.toCurrencyInfo() }.equals(result))
            })
        }
    }

}