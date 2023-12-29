package com.example.myapk2.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.myapk2.database.Favorite
import com.example.myapk2.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getFavoriteUser(): LiveData<List<Favorite>> {
        return mFavoriteRepository.getAllUser()
    }

}
