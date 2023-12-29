package com.example.myapk2.data.retrofit

import com.example.myapk2.data.response.DetailUserResponse
import com.example.myapk2.data.response.GithubResponseItem
import com.example.myapk2.data.response.SearchResponse
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @GET("users")
    fun getUsers(
    ): Call<List<GithubResponseItem>>

    @GET("search/users")
    fun searchUsers(
        @Query("q") query: String
    ): Call<SearchResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<GithubResponseItem>>
    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<GithubResponseItem>>
}