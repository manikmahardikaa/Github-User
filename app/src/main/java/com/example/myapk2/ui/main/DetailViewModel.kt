package com.example.myapk2.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapk2.database.Favorite
import com.example.myapk2.repository.FavoriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : ViewModel() {

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun checkIsFavorite(username: String): LiveData<Favorite> {
       return mFavoriteRepository.getFavoriteByUsername(username)
    }

    fun insertFavorite(favorite: Favorite) {
        mFavoriteRepository.insertFavorite(favorite)
    }

    fun deleteFavorite(favorite: Favorite){
        mFavoriteRepository.deleteFavorite(favorite)
    }

}
