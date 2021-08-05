package com.smart.smartbalibackpaker.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.smart.smartbalibackpaker.DetailActivity
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var user: FirebaseUser
    private lateinit var dbReference: DatabaseReference
    private var userId: String? = null
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_allplace,
            R.string.tab_touristplace,
            R.string.tab_hotelvilla,
            R.string.tab_worshipplace
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(layoutInflater, container, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        dbReference = db.getReference("users")
        user = auth.currentUser!!
        userId = auth.currentUser?.uid

        val query = dbReference.orderByChild("email").equalTo(user.email)
        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children){
                    val username = ""+ ds.child("username").value
                    val image = ds.child("image").value

                    binding?.tvWelcomeUsername?.text = "Welcome $username"
                    context?.let {
                        Glide.with(it)
                            .load(image)
                            .apply(RequestOptions().override(55, 55))
                            .into(binding!!.ivWelcomePhoto)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        return binding?.root
//        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO -> REMOVE AFTER SIMULATING LAYOUT
        binding?.sampleItem?.setOnClickListener {
            startActivity(Intent(context, DetailActivity::class.java))
        }

        val dashboardPlaceAdapter = activity?.let { DashboardPlaceAdapter(it) }

            binding?.vpDashboard?.adapter = dashboardPlaceAdapter
            TabLayoutMediator(
                binding?.layoutTabLayout!!,
                binding?.vpDashboard!!
            ) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()


        val imageList = ArrayList<SlideModel>() // Create image list

        // imageList.add(SlideModel("String Url" or R.drawable)
        // imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title
        imageList.add(
            SlideModel(
                R.drawable.onboardingone,
                "Your plan is your best choice ever",
                ScaleTypes.CENTER_CROP
            )
        )
        imageList.add(
            SlideModel(
                R.drawable.onboardingtwo,
                "Got your experience with the new friends",
                ScaleTypes.CENTER_CROP
            )
        )
        imageList.add(
            SlideModel(
                R.drawable.onboardingthree,
                "Enjoy your time everywhere you go",
                ScaleTypes.CENTER_CROP
            )
        )
        val imageSlider = binding?.imageSlider
        imageSlider?.setImageList(imageList)

    }

}