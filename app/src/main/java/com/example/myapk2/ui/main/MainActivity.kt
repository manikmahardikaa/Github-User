package com.example.myapk2.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapk2.R
import com.example.myapk2.data.response.GithubResponseItem
import com.example.myapk2.databinding.ActivityMainBinding
import com.example.myapk2.helper.SettingModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(application.dataStore)

        val settingsViewModel = ViewModelProvider(this, SettingModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        settingsViewModel.getThemeSettings().observe(this) { isDarkmodeActive ->
            if (isDarkmodeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        supportActionBar?.hide()

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val layoutManager = LinearLayoutManager(this)
        binding.contactRecyclerView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.contactRecyclerView.addItemDecoration(itemDecoration)

        userAdapter = UserAdapter()
        binding.contactRecyclerView.adapter = userAdapter

        mainViewModel.users.observe(this) { users ->
            if (users != null) {
                setUserData(users)
            }
        }

        mainViewModel.searchResponse.observe(this) { searchResponse ->
            setSearchResults(searchResponse)
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        val username = searchView.text.toString()
                        if (username.isNotBlank()) {
                            mainViewModel.searchUsers(username)
                        }
                        searchView.hide()
                        true
                    } else {
                        false
                    }
                }
        }

        binding.searchBar.inflateMenu(R.menu.menu_item)
        binding.searchBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_favorite -> {
                    val intentFavoritePage = Intent(this@MainActivity, FavoriteActivity::class.java)
                    startActivity(intentFavoritePage)
                    true
                }
                R.id.action_setting -> {
                    val intentSettings = Intent(this@MainActivity, SettingActivity::class.java)
                    startActivity(intentSettings)
                    true
                }
                else -> {false}
            }
        }
    }

    private fun setUserData(users: List<GithubResponseItem>) {
        userAdapter.submitList(users)
        userAdapter.setOnUserItemClickListener { user ->
            val moveWithDataIntent = Intent(this, DetailActivity::class.java)
            moveWithDataIntent.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
            moveWithDataIntent.putExtra(DetailActivity.EXTRA_AVATAR_URL, user.avatarUrl)
            startActivity(moveWithDataIntent)
        }
    }

    private fun setSearchResults(searchResponse: List<GithubResponseItem>) {
        userAdapter.submitList(searchResponse)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
