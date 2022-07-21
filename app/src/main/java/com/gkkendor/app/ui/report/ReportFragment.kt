package com.gkkendor.app.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gkkendor.app.R
import com.gkkendor.app.adapter.ReportAdapter
import com.gkkendor.app.databinding.FragmentReportBinding
import com.gkkendor.app.util.ReportViewModelFactory
import com.gkkendor.app.util.Resource
import com.gkkendor.app.util.toast

class ReportFragment : Fragment() {

    companion object {
        private const val TAG = "ReportFragment"
    }

    private var _binding: FragmentReportBinding? = null
    private val binding
        get() = _binding!!

    private val reportViewModel: ReportViewModel by viewModels {
        ReportViewModelFactory()
    }
    private val reportAdapter: ReportAdapter by lazy {
        ReportAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvReport.apply {
            adapter = reportAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        binding.swipeRefresh.apply {
            setOnRefreshListener {
                //TODO
                // refresh item
                isRefreshing = false
            }
        }
        binding.btnAddReport.setOnClickListener {
            findNavController().navigate(
                R.id.action_reportFragment_to_detailReportFragment,
                Bundle().apply {
                    putBoolean("isEditor", true)
                    putSerializable("report", null)
                }
            )
        }
        reportAdapter.setOnItemClickListener {
            // TODO
            //  Add Argument
            findNavController().navigate(
                R.id.action_reportFragment_to_detailReportFragment,
                Bundle().apply {
                    putBoolean("isEditor", false)
                    putSerializable("report", it)
                }
            )
        }
        reportViewModel.resources.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    activity.toast("Still Loading")
                }
                is Resource.Error -> {
                    activity.toast("Error")
                }
                is Resource.Success -> {
                    response.data?.let { reportResponse ->
                        reportAdapter.differ.submitList(reportResponse.reports)
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