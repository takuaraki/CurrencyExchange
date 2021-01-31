package com.example.currencyexchange.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchange.R
import com.example.currencyexchange.databinding.ExchangedListItemBinding
import com.example.currencyexchange.models.data.Money

class ExchangedListAdapter : ListAdapter<Money, ExchangedListAdapter.ViewHolder>(
    ExchangedListDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.exchanged_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ExchangedListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(money: Money) {
            with(binding) {
                currencyCode = money.currencyCode
                amount = money.amount.toString()
            }
        }
    }
}

private class ExchangedListDiffCallback : DiffUtil.ItemCallback<Money>() {
    override fun areItemsTheSame(
        oldItem: Money,
        newItem: Money
    ): Boolean {
        return oldItem.currencyCode == newItem.currencyCode
    }

    override fun areContentsTheSame(
        oldItem: Money,
        newItem: Money
    ): Boolean {
        return oldItem == newItem
    }
}