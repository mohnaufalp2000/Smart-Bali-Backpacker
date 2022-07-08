package com.smart.smartbalibackpaker.guide

import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smart.smartbalibackpaker.core.data.source.local.entity.RecordGuideEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.RecordVacationListEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.VacationCountEntity
import com.smart.smartbalibackpaker.core.data.source.remote.response.DetailGuideResponse
import com.smart.smartbalibackpaker.core.data.source.remote.response.ListPerjalananItem
import com.smart.smartbalibackpaker.databinding.ItemVacationListBinding

class RecordGuideAdapter(private val record : RecordGuideEntity, private val list : List<RecordVacationListEntity?>?, private val countList : List<VacationCountEntity>) : RecyclerView.Adapter<RecordGuideAdapter.ListViewHolder>() {

    class ListViewHolder(val binding: ItemVacationListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = ItemVacationListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.apply {
            Log.d("balaraja", list?.get(position)?.idTempatWisata?.toString() ?: "")
            var listIdTempatWisata = list?.get(position)?.idTempatWisata
            if (listIdTempatWisata != null) {
                listIdTempatWisata = listIdTempatWisata
                    .replace("[", "")
                    .replace("]", "")
                    .trim()
            }
            val fixListIdTempatWisata = listIdTempatWisata?.split(",")?.toList()
            tvVacationCount.text =  fixListIdTempatWisata?.size.toString()
//                idWisata?.size.toString()
            tvDateVacation.text = "${list?.get(position)?.tglPerjalanan} - ${list?.get(position)?.tglPulang}"
        }
    }

    override fun getItemCount(): Int = list?.size ?: 0
}