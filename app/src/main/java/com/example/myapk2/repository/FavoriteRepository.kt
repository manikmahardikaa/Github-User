package com.example.myapk2.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.myapk2.database.Favorite
import com.example.myapk2.database.FavoriteDao
import com.example.myapk2.database.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class FavoriteRepository(application: Application) {

    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getInstance(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllUser(): LiveData<List<Favorite>> = mFavoriteDao.getFavoriteUser()

    fun insertFavorite(favorite: Favorite) {
        executorService.execute{ mFavoriteDao.insertFavorite(favorite) }
    }

    fun deleteFavorite(favorite: Favorite) {
        executorService.execute{ mFavoriteDao.deleteFavorite(favorite) }
    }

    fun getFavoriteByUsername(username: String): LiveData<Favorite> {
        return mFavoriteDao.getFavoriteUserByUsername(username)
    }
}
