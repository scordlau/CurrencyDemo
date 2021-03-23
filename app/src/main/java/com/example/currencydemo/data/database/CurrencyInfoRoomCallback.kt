package com.example.currencydemo.data.database

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.core.domain.CurrencyInfo
import com.example.currencydemo.domain.CurrencyInfoEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

/**
 * Created by scordlau on 3/22/21.
 */

class CurrencyInfoRoomCallback(private val context: Context,
                               private val coroutineScope: CoroutineScope,
                               private val rawResName: Int
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                val gson = Gson()
                val inputStream = context.resources.openRawResource(rawResName)
                        .bufferedReader()
                        .use { it.readText() }
                val type = object : TypeToken<List<CurrencyInfoEntity>>() {}.type
                val instance = CurrencyInfoDatabase.getInstance(context, coroutineScope)
                val result = gson.fromJson<List<CurrencyInfoEntity>>(inputStream, type)
                Log.d("RoomCallback", result.toString())
                instance.currencyInfoDao().insertAll(result)
            }
        }
    }
}