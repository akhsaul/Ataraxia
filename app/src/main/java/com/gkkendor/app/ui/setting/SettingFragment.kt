package com.gkkendor.app.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gkkendor.app.databinding.FragmentSettingBinding
import com.gkkendor.app.ui.activity.AuthActivity

class SettingFragment : Fragment() {

    companion object {
        fun newInstance() = SettingFragment()
        private const val TAG = "SettingFragment"
    }

    private var _binding: FragmentSettingBinding? = null
    private val binding
        get() = _binding!!

    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogout.setOnClickListener {
            activity?.let {
                startActivity(
                    Intent(it, AuthActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                it.finish()
            }
        }
    }

}