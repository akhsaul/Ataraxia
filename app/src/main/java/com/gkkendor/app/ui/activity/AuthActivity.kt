package com.gkkendor.app.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gkkendor.app.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityAuthBinding.inflate(layoutInflater).root)
    }
}