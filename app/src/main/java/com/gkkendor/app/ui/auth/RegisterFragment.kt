package com.gkkendor.app.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gkkendor.app.R
import com.gkkendor.app.databinding.FragmentRegisterBinding
import com.gkkendor.app.util.Constants
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class RegisterFragment : Fragment() {

    companion object {
        private const val TAG = "RegisterFragment"
        private const val DATE = "DATE_PICKER"
    }

    private var _binding: FragmentRegisterBinding? = null
    private val binding
        get() = _binding!!

    private val registerViewModel: RegisterViewModel by viewModels()
    private val sdf = SimpleDateFormat(Constants.PATTERN_PICK_DATE, Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        binding.inGender.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_gender,
                resources.getStringArray(R.array.gender)
            )
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        binding.inDateOfBirth.apply {
            setOnClickListener {
                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date of birth")
                    .setCalendarConstraints(
                        CalendarConstraints.Builder()
                            .setEnd(MaterialDatePicker.todayInUtcMilliseconds())
                            .build()
                    )
                    .build()
                datePicker.show(parentFragmentManager, DATE)
                datePicker.addOnPositiveButtonClickListener {
                    setText(sdf.format(it))
                }
            }
        }
    }
}