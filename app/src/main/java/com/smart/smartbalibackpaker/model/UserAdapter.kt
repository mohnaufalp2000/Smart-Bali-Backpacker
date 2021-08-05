package com.smart.smartbalibackpaker.model

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smart.smartbalibackpaker.databinding.ItemRowUserBinding

class UserAdapter(private val listUser: ArrayList<DataUser>) : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    class ListViewHolder(private val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: DataUser) {
            with(binding){
                Glide.with(itemView.context)
                    .load(user.image)
                    .into(ivChatUserPhoto)
                tvChatUserName.text = user.name
                tvChatUserText.text = user.email

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size
}