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
    val id: Int? = null,
    val thumbnail: String? = null,
    val address: String? = null,
    val updatedAt: String? = null,
    val latitude: String? = null,
    val longtitude: String? = null,
    val createdAt: String? = null,
    val title: String? = null,
    val type: String? = null,
    val slug: String? = null,
    val desc: String? = null,
//    val pictures: List<String?>? = null,
) : Parcelable