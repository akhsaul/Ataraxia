package com.gkkendor.app.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gkkendor.app.databinding.FragmentDetailReportBinding

class DetailReportFragment : Fragment() {

    companion object {
        private const val TAG = "DetailReportFragment"
    }

    private var _binding: FragmentDetailReportBinding? = null
    private val binding
        get() = _binding!!

    private val detailReportViewModel: DetailReportViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailReportBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}