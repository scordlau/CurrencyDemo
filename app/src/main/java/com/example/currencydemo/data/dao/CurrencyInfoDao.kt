package com.example.currencydemo.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencydemo.domain.CurrencyInfoEntity

/**
 * Created by scordlau on 3/22/21.
 */

@Dao
interface CurrencyInfoDao {

    @Query("SELECT * FROM currencyTable")
    fun getAll(): LiveData<List<CurrencyInfoEntity>>

    @Insert(entity = CurrencyInfoEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currencyInfoEntity: CurrencyInfoEntity)

    @Insert(entity = CurrencyInfoEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<CurrencyInfoEntity>)

}