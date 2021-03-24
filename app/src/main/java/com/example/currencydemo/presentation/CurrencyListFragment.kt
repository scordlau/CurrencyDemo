package com.example.currencydemo.presentation

import android.app.Application
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.core.domain.CurrencyInfo
import com.example.currencydemo.R
import com.example.currencydemo.databinding.FragmentCurrencyListBinding
import com.example.currencydemo.presentation.abstraction.ICurrencyListParent
import com.example.currencydemo.presentation.adapter.CurrencyListAdapter
import com.example.currencydemo.presentation.viewmodel.CurrencyListFragmentViewModel
import com.example.currencydemo.presentation.viewmodel.factory.CurrencyListFragmentViewModelFactory
import java.lang.ref.WeakReference

/**
 * Created by scordlau on 3/23/21.
 */

class CurrencyListFragment() : Fragment(), ICurrencyList {

    lateinit var viewModel: CurrencyListFragmentViewModel
    lateinit var binding: FragmentCurrencyListBinding
    var parentRef: WeakReference<ICurrencyListParent>? = null

    private val currencyListAdapter by lazy {
        CurrencyListAdapter(viewModel.data, viewModel.currencyInfoAdapterItemClick)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            viewModel = ViewModelProvider(this, CurrencyListFragmentViewModelFactory(
                    it.applicationContext as Application,
                    arguments?.getSerializable(CURRENCY_DATA) as? List<CurrencyInfo>
                            ?: listOf()
            )).get(CurrencyListFragmentViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<FragmentCurrencyListBinding>(
                inflater,
                R.layout.fragment_currency_list,
                container,
                false
        ).also {
            it.lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.recyclerViewCurrencyListFragment) {
            adapter = currencyListAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        observeViewModel()
        observeSearchTextChanges()
    }

    private fun observeSearchTextChanges() {
        binding.tvSearchCurrencyListFragment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currencyListAdapter.filter(s.toString())
            }
        })
    }

    private fun observeViewModel() {
        viewModel.currencyInfoAdapterItemClick.observe(viewLifecycleOwner, Observer {
            parentRef?.get()?.onCurrencyItemClick(it)
        })
    }

    override fun register(parent: ICurrencyListParent) {
        parentRef = WeakReference(parent)
    }

    override fun sort() {
        currencyListAdapter.sort()
    }

    companion object {

        const val CURRENCY_DATA = "CurrencyData"

        @JvmStatic
        fun newInstance(args: Bundle): CurrencyListFragment {
            val fragment = CurrencyListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}