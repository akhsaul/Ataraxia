package com.gkkendor.app.ui.article

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.gkkendor.app.adapter.ArticleAdapter
import com.gkkendor.app.databinding.FragmentArticleBinding
import com.gkkendor.app.util.ArticleViewModelFactory
import com.gkkendor.app.util.Resource
import com.gkkendor.app.util.toast

class ArticleFragment : Fragment() {

    companion object {
        private const val TAG = "ArticleFragment"
    }

    private var _binding: FragmentArticleBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val articleAdapter: ArticleAdapter by lazy {
        ArticleAdapter()
    }
    private val articleViewModel: ArticleViewModel by viewModels {
        ArticleViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvReport.apply {
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        binding.swipeRefresh.apply {
            setOnRefreshListener {
                //TODO
                // refresh item
                isRefreshing = false
            }
        }
        articleAdapter.setOnItemClickListener {
            Log.i(TAG, it.toString())
            activity.toast("Clicked item: $it")
        }
        articleViewModel.articles.observe(viewLifecycleOwner) { response ->
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