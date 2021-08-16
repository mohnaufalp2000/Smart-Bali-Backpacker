package com.smart.smartbalibackpaker.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.smart.smartbalibackpaker.data.TourismRepository
import com.smart.smartbalibackpaker.data.source.local.entity.TourismDataEntity
import com.smart.smartbalibackpaker.vo.Resource

class DetailPlaceViewModel(private val tourismRepository: TourismRepository) : ViewModel() {

    private var placeId = MutableLiveData<Int>()

    fun setSelectedPlace(placeId: Int) {
        this.placeId.value = placeId
    }


    var detailPlace: LiveData<Resource<TourismDataEntity>> = Transformations.switchMap(placeId) {
        tourismRepository.getDetailTourism(it)
    }
}