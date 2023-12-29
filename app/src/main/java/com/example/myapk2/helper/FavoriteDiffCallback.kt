package com.example.myapk2.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.myapk2.database.Favorite

class FavoriteDiffCallback(private val oldFavoriteList: List<Favorite>, private val newFavoriteList: List<Favorite>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldFavoriteList.size
    }

    override fun getNewListSize(): Int {
        return newFavoriteList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavoriteList[oldItemPosition].username == newFavoriteList[newItemPosition].username
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavorite = oldFavoriteList[oldItemPosition]
        val newFavorite = newFavoriteList[newItemPosition]
        return oldFavorite.username == newFavorite.username && oldFavorite.avatarUrl == newFavorite.avatarUrl
    }
}
