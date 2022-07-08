package com.smart.smartbalibackpaker.guide

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.smart.smartbalibackpaker.core.data.TourismRepository
import com.smart.smartbalibackpaker.core.data.source.local.entity.GuideMapsEntity

class DetailGuideViewModel(private val tourismRepository: TourismRepository) : ViewModel() {

    fun insertNode(guide: GuideMapsEntity){
        tourismRepository.insertNode(guide)
    }

    fun getNodes() : LiveData<List<GuideMapsEntity>> {
        return tourismRepository.getNodes()
    }

    fun deleteNode() {
        tourismRepository.deleteNode()
    }

    fun deleteNodes() {
        tourismRepository.deleteNodes()
    }
}