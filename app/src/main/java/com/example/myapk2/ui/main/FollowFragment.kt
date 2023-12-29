package com.example.myapk2.ui.main


import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapk2.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentFollowBinding
    private var position: Int = 0
    private var username: String = ""
    private lateinit var userAdapter: UserFollowAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME) ?: ""
        }

        val activity = requireActivity() as AppCompatActivity
        mainViewModel = ViewModelProvider(activity).get(MainViewModel::class.java)
        userAdapter = UserFollowAdapter()

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = userAdapter

        if (position == 1) {
            showProgressBar()
            mainViewModel.loadFollowing(username)
            mainViewModel.following.observe(viewLifecycleOwner) { following ->
                following?.let {
                    userAdapter.submitListFollowing(following)
                }
                hideProgressBar()
            }
        } else {
            showProgressBar()
            mainViewModel.loadFollowers(username)
            mainViewModel.followers.observe(viewLifecycleOwner) { followers ->
                followers?.let {
                    userAdapter.submitListFollowers(followers)
                }
                hideProgressBar()
            }
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    companion object {
        fun newInstance(position: Int, username: String): FollowFragment {
            val fragment = FollowFragment()
            val args = Bundle()
            args.putInt(ARG_POSITION, position)
            args.putString(ARG_USERNAME, username)
            fragment.arguments = args
            return fragment
        }

        const val ARG_POSITION = "arg_position"
        const val ARG_USERNAME = "arg_username"
    }
}
