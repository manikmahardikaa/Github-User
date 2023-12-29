package com.example.myapk2.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapk2.data.response.DetailUserResponse
import com.example.myapk2.data.response.GithubResponseItem
import com.example.myapk2.data.response.SearchResponse
import com.example.myapk2.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _users = MutableLiveData<List<GithubResponseItem>>()
    val users: LiveData<List<GithubResponseItem>> = _users

    private val _searchResponse = MutableLiveData<List<GithubResponseItem>>()
    val searchResponse: LiveData<List<GithubResponseItem>> = _searchResponse

    private val _detailUser = MutableLiveData<DetailUserResponse?>()
    val detailUser: MutableLiveData<DetailUserResponse?> = _detailUser

    private val _following = MutableLiveData<List<GithubResponseItem>>()
    val following: LiveData<List<GithubResponseItem>> = _following

    private val _followers = MutableLiveData<List<GithubResponseItem>>()
    val followers: LiveData<List<GithubResponseItem>> = _followers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "MainViewModel"
    }

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers()
        client.enqueue(object : Callback<List<GithubResponseItem>> {
            override fun onResponse(
                call: Call<List<GithubResponseItem>>,
                response: Response<List<GithubResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val users = response.body()
                    _users.value = users ?: emptyList()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<GithubResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun searchUsers(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUsers(query)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val searchResponse = response.body()
                    val userList = searchResponse?.items ?: emptyList()
                    _searchResponse.value = userList
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                val errorMessage = "Terjadi kesalahan: ${t.message}"
                Log.e(TAG, errorMessage)
            }
        })
    }

    fun fetchUserDetails(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val userDetails = response.body()
                    _detailUser.value = userDetails
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                val errorMessage = "Terjadi kesalahan: ${t.message}"
                Log.e(TAG, errorMessage)
            }
        })
    }


    fun loadFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<GithubResponseItem>> {
            override fun onResponse(
                call: Call<List<GithubResponseItem>>,
                response: Response<List<GithubResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val userFollowers = response.body()
                    _followers.value = userFollowers ?: emptyList()
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<GithubResponseItem>>, t: Throwable) {
                _isLoading.value = false
                val errorMessage = "Terjadi kesalahan: ${t.message}"
                Log.e(TAG, errorMessage)
            }
        })
    }

    fun loadFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<GithubResponseItem>> {
            override fun onResponse(
                call: Call<List<GithubResponseItem>>,
                response: Response<List<GithubResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val userFollowing = response.body()
                    _following.value = userFollowing ?: emptyList()
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<GithubResponseItem>>, t: Throwable) {
                _isLoading.value = false
                val errorMessage = "Terjadi kesalahan: ${t.message}"
                Log.e(TAG, errorMessage)
            }
        })
    }
}
