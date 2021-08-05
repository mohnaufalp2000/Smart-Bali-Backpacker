package com.smart.smartbalibackpaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.smart.smartbalibackpaker.auth.LoginActivity
import com.smart.smartbalibackpaker.core.preferences.PreferencesSettings
import com.smart.smartbalibackpaker.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private lateinit var db: FirebaseDatabase
    private lateinit var user: FirebaseUser
    private lateinit var dbReference: DatabaseReference
    private var userId: String? = null
    private var binding: FragmentSettingBinding? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.layout_settings_preferences, PreferencesSettings())
            .commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(layoutInflater, container, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        dbReference = db.getReference("users")
        user = auth.currentUser!!
        userId = auth.currentUser?.uid

        val query = dbReference.orderByChild("email").equalTo(user.email)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children){
                    val username = ""+ ds.child("username").value
                    val email = ds.child("email").value
                    val image = ds.child("image").value

                    context?.let {
                        Glide.with(it)
                            .load(image)
                            .into(binding!!.imgViewProfile)
                    }

                    binding?.txtViewEmail?.text = email.toString().trim()
                    binding?.txtViewUsername?.text = username

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error Unknown Object", Toast.LENGTH_LONG).show()
            }
        })

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding?.txtLogOut?.setOnClickListener {
            auth.signOut()
            Intent(context, LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                activity?.startActivity(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}