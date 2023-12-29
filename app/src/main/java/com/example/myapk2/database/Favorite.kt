package com.example.myapk2.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "favorite_users")
@Parcelize
data class Favorite(
    @PrimaryKey
    var username: String = "",
    var avatarUrl: String? = null
) : Parcelable

