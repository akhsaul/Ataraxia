package com.gkkendor.app.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gkkendor.app.R
import com.gkkendor.app.databinding.FragmentDetailArticleBinding

class DetailArticleFragment : Fragment() {

    companion object {
        private const val TAG = "DetailArticleFragment"
    }

    private var _binding: FragmentDetailArticleBinding? = null
    private val binding
        get() = _binding!!

    private val detailArticleViewModel: DetailArticleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailArticleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}