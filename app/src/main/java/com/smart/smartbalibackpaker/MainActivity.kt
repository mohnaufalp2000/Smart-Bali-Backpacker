package com.smart.smartbalibackpaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.smart.smartbalibackpaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var user: FirebaseUser? = null
    private lateinit var dbReference: DatabaseReference
    private var myUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTheme(R.style.SettingsFragmentStyle)

        user = FirebaseAuth.getInstance().currentUser
        myUid = user?.uid

        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        navView.itemIconTintList = null
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
//        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
        navView.drawableState
    }

}