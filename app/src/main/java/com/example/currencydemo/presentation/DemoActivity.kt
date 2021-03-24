package com.example.currencydemo.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.core.domain.CurrencyInfo
import com.example.currencydemo.R
import com.example.currencydemo.databinding.ActivityDemoBinding
import com.example.currencydemo.domain.toCurrencyInfo
import com.example.currencydemo.presentation.abstraction.ICurrencyListParent
import com.example.currencydemo.presentation.viewmodel.DemoActivityViewModel

class DemoActivity : AppCompatActivity(), ICurrencyListParent {

    var currencyListFragment: CurrencyListFragment? = null
    lateinit var binding: ActivityDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val demoActivityViewModel = ViewModelProvider(this)
                .get(DemoActivityViewModel::class.java)

        binding = DataBindingUtil.setContentView<ActivityDemoBinding>(this, R.layout.activity_demo).also {
            it.viewModel = demoActivityViewModel
        }

        observeViewModel(demoActivityViewModel)
        initClickListener()
    }

    private fun initClickListener() {
        binding.btnSortDemo.setOnClickListener {
            currencyListFragment?.sort()
        }
    }

    private fun observeViewModel(demoActivityViewModel: DemoActivityViewModel) {
        demoActivityViewModel.isDatabaseReadyToObserve.observe(this, Observer { readyToObserve ->
            if (readyToObserve == true) {
                demoActivityViewModel.databaseCurrencyListLiveData?.observe(this, Observer {
                    val isDataSetReady = it.isNotEmpty()
                    if (isDataSetReady) createCurrencyListFragment(it.map { it.toCurrencyInfo() } as ArrayList<CurrencyInfo>)
                })
            }
        })
    }

    private fun createCurrencyListFragment(it: ArrayList<CurrencyInfo>?) {
        currencyListFragment = CurrencyListFragment.newInstance(Bundle().apply {
            putSerializable(CurrencyListFragment.CURRENCY_DATA, it)
        }).also {
            it.register(this)
            supportFragmentManager.beginTransaction().replace(
                    R.id.linear_layout_demo,
                    it,
                    this.javaClass.name
            ).commit()
        }
    }

    override fun onCurrencyItemClick(data: CurrencyInfo) {
        if (isFinishing) return
        Toast.makeText(this, data.toString(), Toast.LENGTH_LONG).show()
    }
}