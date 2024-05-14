package com.hsr2024.mungmungdoctortp.bnv3community

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.hsr2024.mungmungdoctortp.QAG
import com.hsr2024.mungmungdoctortp.data.QAData
import com.hsr2024.mungmungdoctortp.databinding.QaitemBinding

class QAListAdapter(val context:Context, var items:List<QAData>) : Adapter<QAListAdapter.VH>() {
    inner class VH(val binding:QaitemBinding) : ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=VH(QaitemBinding.inflate(LayoutInflater.from(context),parent,false))
    override fun getItemCount()=items.size
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item=items[position]

        val imgUrl= "http://43.200.163.153/img/${item.imgurl}"
        val proFileImgUrl= "http://43.200.163.153/img/${item.profile_imgurl}"
        Glide.with(context).load(imgUrl).into(holder.binding.iv)
        holder.binding.tvTitle.text=item.title
        holder.binding.tvNickname.text=item.nickname
        holder.binding.tvViewcount.text="조회 ${item.view_count}"
        holder.binding.tvComment.text="댓글 ${item.comment_count}"
        Log.d("imgUrl",imgUrl)
        Glide.with(context).load(imgUrl).into(holder.binding.iv)

        holder.binding.root.setOnClickListener {
           val intent= Intent(context, QADetailActivity::class.java)
            QAG.QAId = item.qa_id

            Log.d("내용", item.content)
            Log.d("dsffff", imgUrl)


            context.startActivity(intent)
        }
    }

    // 데이터 업데이트
    fun setData(newItems: List<QAData>) {
        items = newItems
        notifyDataSetChanged()
    }
}