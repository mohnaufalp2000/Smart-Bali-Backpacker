package com.smart.smartbalibackpaker.guide

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.core.data.source.local.entity.RecordGuideEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.RecordVacationListEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.VacationCountEntity
import com.smart.smartbalibackpaker.core.data.source.remote.response.ListPerjalananItem
import com.smart.smartbalibackpaker.core.viewmodel.ViewModelFactory
import com.smart.smartbalibackpaker.core.vo.Status
import com.smart.smartbalibackpaker.databinding.FragmentGuideBinding
import com.smart.smartbalibackpaker.databinding.FragmentGuideTourBinding


class GuideTourFragment : Fragment() {

    private var binding: FragmentGuideTourBinding? = null
    private val recordGuideViewModel by lazy { ViewModelProvider(this, ViewModelFactory.getInstance(requireActivity())
    ).get(RecordGuideViewModel::class.java)
    }
    private lateinit var outsideAdapter : RecordGuideAdapter
    private var user: FirebaseUser? = null
    private var myUid: String? = null
    private val listIdPerjalanan = ArrayList<VacationCountEntity>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGuideTourBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth()
        populateData()
    }

    private fun firebaseAuth() {
        user = FirebaseAuth.getInstance().currentUser
        myUid = user?.uid
    }

    private fun populateData() {
        recordGuideViewModel.getAllRecordGuide(myUid ?: "").observe(viewLifecycleOwner){
            if(it != null){
                when(it.status){
                    Status.LOADING -> {}
                    Status.SUCCESS -> {
                        Log.d("idguide", myUid ?: "")
                        val record = it.data

                        recordGuideViewModel.getRecordVacation(myUid ?: "").observe(viewLifecycleOwner){ listVac ->

                            Log.d("hahaha1", listVac.toString())
//                            listVac.forEachIndexed { index, _ ->
//                                recordGuideViewModel.getVacationCount(listVac[index].idPerjalanan ?: 0).observe(viewLifecycleOwner){fixList ->
//                                        Log.d("hahaha2", listVac.toString())
//                                        Log.d("hahaha3", fixList.toString())
//                                        fixList.forEachIndexed { fixIndex, _ ->
//                                        if(fixList[fixIndex].idPerjalanan == listVac[index].idPerjalanan){
//                                            listIdPerjalanan.add(fixList[index])
//                                        }
//
//                                        }
//                                }
//                            }
                            outsideAdapter = RecordGuideAdapter(record ?: RecordGuideEntity() , listVac, listIdPerjalanan)
                            showRecyclerView(outsideAdapter)
                        }
                    }
                    Status.ERROR -> {}
                }
            }
        }
    }

    private fun showRecyclerView(outsideAdapter: RecordGuideAdapter) {
        binding?.rvVacationList?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = outsideAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}