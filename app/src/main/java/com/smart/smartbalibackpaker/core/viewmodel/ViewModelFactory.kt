package com.smart.smartbalibackpaker.core.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smart.smartbalibackpaker.dashboard.DetailPlaceViewModel
import com.smart.smartbalibackpaker.dashboard.PlaceViewModel
import com.smart.smartbalibackpaker.core.data.TourismRepository
import com.smart.smartbalibackpaker.core.di.Injection

class ViewModelFactory private constructor(private val tourismRepository: TourismRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PlaceViewModel::class.java) -> {
                PlaceViewModel(tourismRepository) as T
            }
            modelClass.isAssignableFrom(DetailPlaceViewModel::class.java) -> {
                DetailPlaceViewModel(tourismRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }

    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context)).apply {
                    instance = this
                }
            }
    }

}