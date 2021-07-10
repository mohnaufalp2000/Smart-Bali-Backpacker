package com.smart.smartbalibackpaker.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private val binding by lazy {ActivityOnboardingBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.vpOnboarding.adapter = OnboardingAdapter(supportFragmentManager)
    }
}