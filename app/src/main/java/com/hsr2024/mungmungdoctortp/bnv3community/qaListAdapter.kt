package com.hsr2024.mungmungdoctortp.bnv3community

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.hsr2024.mungmungdoctortp.bnv3community.QAData
import com.hsr2024.mungmungdoctortp.databinding.QaitemBinding

class qaListAdapter(val context:Context, val items:List<QAData>) : Adapter<qaListAdapter.VH>() {
    inner class VH(val binding:QaitemBinding) : ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=VH(QaitemBinding.inflate(LayoutInflater.from(context),parent,false))
    override fun getItemCount()=items.size
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item=items[position]
        Glide.with(context).load(item.imgurl).into(holder.binding.iv)
        holder.binding.tvTitle.text=item.title
        holder.binding.tvNickname.text=item.nickname
        holder.binding.tvViewcount.text=item.view_count
        holder.binding.tvComment.text=item.comment_count
    }
}