package com.gkkendor.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.gkkendor.app.adapter.ArticleAdapter
import com.gkkendor.app.databinding.FragmentHomeBinding
import com.gkkendor.app.util.HomeViewModelFactory
import com.gkkendor.app.util.Resource
import com.gkkendor.app.util.toast

class HomeFragment : Fragment() {

    companion object {
        private const val TAG = "HomeFragment"
    }

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding
        get() = _binding!!

    private val articleAdapter: ArticleAdapter by lazy {
        ArticleAdapter()
    }
    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvLatestArticle.apply {
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        homeViewModel.articles.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    activity.toast("Still Loading")
                }
                is Resource.Error -> {
                    activity.toast("Error")
                }
                is Resource.Success -> {
                    response.data?.let { newsResponse ->
                        articleAdapter.differ.submitList(newsResponse.articles)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}