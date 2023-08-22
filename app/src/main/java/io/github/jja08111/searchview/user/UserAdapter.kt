package io.github.jja08111.searchview.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.github.jja08111.searchview.databinding.ItemUserBinding
import io.github.jja08111.searchview.model.User

class UserAdapter : ListAdapter<User, UserAdapter.UserViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(layoutInflater)
        return UserViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user: User? = getItem(position)
        if (user != null) {
            holder.bind(user = user)
        }
    }

    class UserViewHolder(private val binding: ItemUserBinding) : ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.textView.text = user.name
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }
}
