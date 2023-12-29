package com.example.myapk2.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.myapk2.R
import com.example.myapk2.database.Favorite
import com.example.myapk2.databinding.ActivityDetailBinding
import com.example.myapk2.helper.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: SectionsPagerAdapter
    private lateinit var detailViewModel: DetailViewModel
    private var favoriteUser: Favorite = Favorite()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        detailViewModel = obtainViewModel(this@DetailActivity)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR_URL)

        if (username != null) {
            mainViewModel.fetchUserDetails(username)
        }

        binding.loadingProgressBar.visibility = View.VISIBLE

        mainViewModel.detailUser.observe(this) { detailUser ->
            if (detailUser != null) {
                binding.usernameTextView.text = detailUser.name
                binding.usernameText.text = detailUser.login
                binding.followerCountTextView.text = detailUser.followers.toString()
                binding.followingCountTextView.text = detailUser.following.toString()

                Glide.with(this)
                    .load(avatarUrl)
                    .into(binding.contactImageView)

                binding.loadingProgressBar.visibility = View.GONE

                if (username != null) {
                    adapter = SectionsPagerAdapter(this, username)
                    setupViewPager()

                    detailViewModel.checkIsFavorite(detailUser.login).observe(this){ favorite ->
                        if (favorite != null) {
                            binding.fabAdd.setImageDrawable(ContextCompat.getDrawable(binding.fabAdd.context, R.drawable.ic_favorite))
                            binding.fabAdd.setOnClickListener {
                                detailViewModel.deleteFavorite(favorite)
                                binding.fabAdd.setImageDrawable(ContextCompat.getDrawable(binding.fabAdd.context, R.drawable.ic_unfavorit))
                            }
                        } else {
                            binding.fabAdd.setImageDrawable(ContextCompat.getDrawable(binding.fabAdd.context, R.drawable.ic_unfavorit))
                            binding.fabAdd.setOnClickListener {
                                binding.fabAdd.setImageDrawable(ContextCompat.getDrawable(binding.fabAdd.context, R.drawable.ic_favorite))
                                val usernameGithub = detailUser.login

                                favoriteUser.let {
                                    favoriteUser.username = usernameGithub
                                    favoriteUser.avatarUrl = avatarUrl
                                }
                                detailViewModel.insertFavorite(favoriteUser)
                            }
                        }
                    }

                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupViewPager() {
        val viewPager: ViewPager2 = binding.viewPager
        val tabLayout = binding.tabLayout

        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Following"
                else -> "Followers"
            }
        }.attach()
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"
    }
}
