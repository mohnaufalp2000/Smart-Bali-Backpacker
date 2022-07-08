package com.smart.smartbalibackpaker.guide

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.smart.smartbalibackpaker.core.viewmodel.ViewModelFactory
import com.smart.smartbalibackpaker.databinding.ActivityTimerGuideBinding

class TimerGuideActivity : AppCompatActivity() {

    private val binding by lazy {ActivityTimerGuideBinding.inflate(layoutInflater)}
    private val detailGuideViewModel by lazy { ViewModelProvider(this, ViewModelFactory.getInstance(this)
        ).get(DetailGuideViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        backToDetailGuide()
        back()
    }

    private fun back() {
        binding.imageView2.setOnClickListener {
            finish()
        }
    }

    private fun backToDetailGuide() {
        binding.btnSkipVacation.setOnClickListener {
            detailGuideViewModel.deleteNode()
            val intent = Intent(this@TimerGuideActivity, DetailGuideActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }
}