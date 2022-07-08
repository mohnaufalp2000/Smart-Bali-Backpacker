package com.smart.smartbalibackpaker.guide

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.smart.smartbalibackpaker.core.data.TourismRepository
import com.smart.smartbalibackpaker.core.data.source.local.entity.RecordGuideEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.RecordVacationListEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.VacationCountEntity
import com.smart.smartbalibackpaker.core.vo.Resource

class RecordGuideViewModel(private val tourismRepository: TourismRepository): ViewModel() {

    fun getAllRecordGuide(backpackerId: String) : LiveData<Resource<RecordGuideEntity>> =
        tourismRepository.getAllRecordGuide(backpackerId)

    fun getRecordVacation(backpackerId: String) : LiveData<List<RecordVacationListEntity>> =
        tourismRepository.getRecordVacation(backpackerId)

    fun getVacationCount(idPerjalanan: Int) : LiveData<List<VacationCountEntity>> =
        tourismRepository.getVacationRecordCount(idPerjalanan)
}