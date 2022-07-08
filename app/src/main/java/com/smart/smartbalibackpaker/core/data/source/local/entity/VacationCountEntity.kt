package com.smart.smartbalibackpaker.core.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "vacation_count_table")
@Parcelize
data class VacationCountEntity(
    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "id")
    val id : String,

    // list
    @ColumnInfo(name ="id_tempat_wisata")
    val idTempatWisata: Int? = null,

    @ColumnInfo(name = "id_perjalanan")
    val idPerjalanan: Int? = null,

) : Parcelable