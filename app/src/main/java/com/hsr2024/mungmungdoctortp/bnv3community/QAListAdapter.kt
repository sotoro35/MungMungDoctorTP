package com.hsr2024.mungmungdoctortp.bnv3community

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.hsr2024.mungmungdoctortp.data.QAData
import com.hsr2024.mungmungdoctortp.databinding.QaitemBinding

class QAListAdapter(val context:Context, var items:List<QAData>) : Adapter<QAListAdapter.VH>() {
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

        holder.binding.root.setOnClickListener {
            val intent= Intent(context, QADetailActivity::class.java)
            intent.putExtra("item",item.title)
            context.startActivity(intent)
        }
    }

    // 데이터 업데이트
    fun setData(newItems: List<QAData>) {
        items = newItems
        notifyDataSetChanged()
    }
}