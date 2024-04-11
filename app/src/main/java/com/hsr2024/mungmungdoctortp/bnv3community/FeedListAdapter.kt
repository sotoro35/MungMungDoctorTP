package com.hsr2024.mungmungdoctortp.bnv3community

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.hsr2024.mungmungdoctortp.databinding.CommentItemBinding
import com.hsr2024.mungmungdoctortp.databinding.FeeditemBinding

class FeedListAdapter(val context: Context, val items:List<FeedData>) : Adapter<FeedListAdapter.VH>() {
    inner class VH(val binding: FeeditemBinding) : ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=VH(FeeditemBinding.inflate(LayoutInflater.from(context),parent,false))
    override fun getItemCount()=items.size
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item=items[position]
        Glide.with(context).load(item.profile_imgurl).into(holder.binding.circleIv2)
        holder.binding.tv.text=item.nickname
        Glide.with(context).load(item.imgurl).into(holder.binding.iv)
        holder.binding.tvFavorite.text=item.favorite
        holder.binding.tvComment.text=item.comment
        holder.binding.content.text=item.content
        holder.binding.tvDate.text=item.create_date
    }
}