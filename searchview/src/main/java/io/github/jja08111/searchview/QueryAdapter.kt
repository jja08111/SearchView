package io.github.jja08111.searchview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.github.jja08111.searchview.databinding.QueryItemBinding

class QueryAdapter(
    private val onItemClick: (Int) -> Unit
) : ListAdapter<String, QueryAdapter.QueryViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = QueryItemBinding.inflate(layoutInflater, parent, false)
        return QueryViewHolder(binding, onItemClick = onItemClick)
    }

    override fun onBindViewHolder(holder: QueryViewHolder, position: Int) {
        val query: String? = getItem(position)
        if (query != null) {
            holder.bind(query)
        }
    }

    class QueryViewHolder(
        private val binding: QueryItemBinding,
        private val onItemClick: (Int) -> Unit
    ) : ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }

        fun bind(query: String) {
            binding.itemText.text = query
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}