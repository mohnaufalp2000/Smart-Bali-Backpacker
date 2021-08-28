package com.smart.smartbalibackpaker.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smart.smartbalibackpaker.R
import java.util.ArrayList

class GroupChatAdapter(val context: Context, private val listChat: ArrayList<ModelGroupChat>) : RecyclerView.Adapter<GroupChatAdapter.ListViewHolder>() {

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvTitle : TextView = itemView.findViewById(R.id.tv_group_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_group_chat, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val model = listChat[position]
        val title = model.groupTitle

        holder.tvTitle.text = title
    }

    override fun getItemCount(): Int = listChat.size
}