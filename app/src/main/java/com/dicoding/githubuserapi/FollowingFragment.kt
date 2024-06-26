package com.dicoding.githubuserapi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapi.adapter.FollowAdapter
import com.dicoding.githubuserapi.databinding.FragmentFollowingBinding
import com.dicoding.githubuserapi.response.Follow
import com.dicoding.githubuserapi.viewmodel.HomeViewModel

class FollowingFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private val adapter = FollowAdapter()

    private lateinit var binding: FragmentFollowingBinding
    private val _binding get() = binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showViewModel()
        showRecyclerView()
        viewModel.getIsLoading.observe(viewLifecycleOwner, this::showLoading)
    }

    private fun showViewModel() {
        viewModel.following(UserDetailActivity.username)
        viewModel.getFollowing.observe(viewLifecycleOwner) { following ->
            if (following.size != 0) {
                adapter.setData(following)
            } else {
                Toast.makeText(context, "Following Not Found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showRecyclerView() {
        binding.rvFollowing.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFollowing.setHasFixedSize(true)
        binding.rvFollowing.adapter = adapter

        adapter.setOnItemClickCallback { data -> selectedUser(data) }
    }

    private fun selectedUser(user: Follow) {
        val i = Intent(activity, UserDetailActivity::class.java)
        i.putExtra(UserDetailActivity.EXTRA_USER, user.login)
        startActivity(i)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.following(UserDetailActivity.username)
    }
}