package com.smart.smartbalibackpaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.smart.smartbalibackpaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        navView.itemIconTintList = null
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
        navView.drawableState
    }
}