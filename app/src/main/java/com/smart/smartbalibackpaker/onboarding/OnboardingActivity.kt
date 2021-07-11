package com.smart.smartbalibackpaker.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.auth.LoginActivity
import com.smart.smartbalibackpaker.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private val binding by lazy {ActivityOnboardingBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.vpOnboarding.adapter = OnboardingAdapter(supportFragmentManager)
        binding.btnIn.setOnClickListener {
            startActivity(Intent(this@OnboardingActivity, LoginActivity::class.java))
            finish()
        }
    }
}