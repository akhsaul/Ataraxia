package com.gkkendor.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gkkendor.app.R
import com.gkkendor.app.databinding.FragmentLoginBinding
import com.gkkendor.app.ui.activity.MainActivity

class LoginFragment : Fragment() {

    companion object {
        private const val TAG = "LoginFragment"
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            activity?.let {
                startActivity(
                    Intent(it, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                it.finish()
            }
        }
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}