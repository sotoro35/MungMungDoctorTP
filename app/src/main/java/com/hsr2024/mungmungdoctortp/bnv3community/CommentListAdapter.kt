package com.hsr2024.mungmungdoctortp.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.hsr2024.mungmungdoctortp.data.CommentData
import com.hsr2024.mungmungdoctortp.databinding.CommentItemBinding

class CommentListAdapter(val context:Context, val items:List<CommentData>) : Adapter<CommentListAdapter.VH>() {
    inner class VH(val binding: CommentItemBinding) : ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=VH(CommentItemBinding.inflate(LayoutInflater.from(context),parent,false))
    override fun getItemCount()=items.size
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item=items[position]
        Glide.with(context).load(item.profile_imgurl).into(holder.binding.circleIv2)
        holder.binding.tvTitle.text=item.content
        holder.binding.tvNickname.text=item.nickname
        holder.binding.tvDate.text=item.create_date
    }

}