package com.smart.smartbalibackpaker.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smart.smartbalibackpaker.MainActivity
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.databinding.ActivityLoginBinding
import com.smart.smartbalibackpaker.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.txtLogin.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            finishAffinity()
        }
        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.tbSignUp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.tbSignUp.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.tbSignUp.setNavigationOnClickListener {
            finish()
        }
    }
}