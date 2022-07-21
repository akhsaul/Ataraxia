package com.gkkendor.app.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gkkendor.app.databinding.FragmentCreateReportBinding

class CreateReportFragment : Fragment() {

    companion object {
        private const val TAG = "CreateReportFragment"
    }

    private var _binding: FragmentCreateReportBinding? = null
    private val binding
    get() = _binding!!

    private val createReportViewModel: CreateReportViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateReportBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // TODO
        //  need to save current state
        //  https://developer.android.com/guide/fragments/saving-state
        //  https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate
        //  https://developer.android.com/topic/libraries/architecture/saving-states#:~:text=When%20the%20activity%20goes%20into,back%20into%20its%20current%20state.
    }
}