package com.smart.smartbalibackpaker.core.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "tourism_table")
@Parcelize
data class TourismDataEntity(
    @PrimaryKey
    @NonNull
    var id: Int? = null,
    var thumbnail: String? = null,
    var address: String? = null,
    var updatedAt: String? = null,
    var latitude: String? = null,
    var longtitude: String? = null,
    var createdAt: String? = null,
    var title: String? = null,
    var type: String? = null,
    var slug: String? = null,
    var desc: String? = null,
    var isFavorite: Boolean = false
//    var pictures: List<String?>? = null,
) : Parcelable