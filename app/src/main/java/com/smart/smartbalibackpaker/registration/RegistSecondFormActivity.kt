package com.smart.smartbalibackpaker.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smart.smartbalibackpaker.dashboard.DashboardFragment
import com.smart.smartbalibackpaker.databinding.ActivityRegistSecondFormBinding

class RegistSecondFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistSecondFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistSecondFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFinalRegister.setOnClickListener {
            val intent = Intent(this, DashboardFragment::class.java)
            startActivity(intent)
        }
    }
}