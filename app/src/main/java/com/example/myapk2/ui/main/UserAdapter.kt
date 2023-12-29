package com.example.myapk2.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapk2.data.response.GithubResponseItem
import com.example.myapk2.databinding.ItemUserBinding

class UserAdapter : ListAdapter<GithubResponseItem, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private var onItemClickListener: ((GithubResponseItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    fun setOnUserItemClickListener(listener: (GithubResponseItem) -> Unit) {
        onItemClickListener = listener
    }

    inner class MyViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: GithubResponseItem) {
            binding.contactNameTextView.text = user.login
            Glide.with(binding.root.context)
                .load(user.avatarUrl)
                .into(binding.contactImageView)

            itemView.setOnClickListener {
                onItemClickListener?.invoke(user)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GithubResponseItem>() {
            override fun areItemsTheSame(oldItem: GithubResponseItem, newItem: GithubResponseItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GithubResponseItem, newItem: GithubResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}



