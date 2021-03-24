package com.example.currencydemo.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.core.domain.CurrencyInfo
import com.example.currencydemo.R
import com.example.currencydemo.databinding.ItemCurrencyListBinding
import com.example.currencydemo.presentation.viewmodel.CurrencyListVHViewModel
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by scordlau on 3/23/21.
 */

class CurrencyListAdapter(private val data: List<CurrencyInfo>,
                          private val currencyInfoAdapterItemClick: MutableLiveData<CurrencyInfo>
) : RecyclerView.Adapter<CurrencyListAdapter.CurrencyListViewHolder>() {

    private var viewModels = data.map { CurrencyListVHViewModel(it) }.toMutableList()
    var isSortAscending = AtomicBoolean(true)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyListViewHolder {
        val binding = DataBindingUtil.inflate<ItemCurrencyListBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_currency_list,
                parent,
                false
        ).also {
            it.lifecycleOwner = parent.context as? LifecycleOwner
        }
        return CurrencyListViewHolder(binding)
    }

    override fun getItemCount(): Int = viewModels.size

    override fun onBindViewHolder(holder: CurrencyListViewHolder, position: Int) {
        holder.bind(viewModels[position])
    }

    fun sort() {
        synchronized(this) {
            sortData(isSortAscending.getAndSet(!isSortAscending.get()))
            notifyItemRangeChanged(0, viewModels.size)
        }
    }

    private fun sortData(isSortAsc: Boolean) {
        when (isSortAsc) {
            true -> viewModels.sortBy { it.data.id }
            false -> viewModels.sortByDescending { it.data.id }
        }
    }

    fun filter(searchText: String) {
        val isOriginalDataSet = viewModels.size == data.size
        if (searchText.isEmpty() && isOriginalDataSet) return
        val oldSize = itemCount
        viewModels = filterViewModelPerKeyword(searchText)
        sortData(isSortAscending.get())
        notifyItemRangeChanged(0, oldSize)
    }

    private fun filterViewModelPerKeyword(searchText: String): MutableList<CurrencyListVHViewModel> =
            data.filter { it.name.toLowerCase(Locale.getDefault()).contains(searchText.toLowerCase(Locale.getDefault())) }
                    .map { CurrencyListVHViewModel(it) }
                    .toMutableList()

    inner class CurrencyListViewHolder(private val binding: ItemCurrencyListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(currencyListVHViewModel: CurrencyListVHViewModel) {
            binding.viewModel = currencyListVHViewModel
            binding.root.rootView.setOnClickListener {
                currencyInfoAdapterItemClick.value = currencyListVHViewModel.data
            }
        }

    }
}