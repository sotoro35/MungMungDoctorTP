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
        Log.d("dfffff", item.profile_imgurl)
        Glide.with(context).load(imgUrl).into(holder.binding.iv)
        holder.binding.tvTitle.text=item.title
        holder.binding.tvNickname.text=item.nickname
        holder.binding.tvViewcount.text=item.view_count
        holder.binding.tvComment.text=item.comment_count
        Log.d("imgUrl",imgUrl)
        Glide.with(context).load(imgUrl).into(holder.binding.iv)

        holder.binding.root.setOnClickListener {
            val intent= Intent(context, QADetailActivity::class.java)
            intent.putExtra("profileImg", proFileImgUrl)
            intent.putExtra("nickname", item.nickname)
            intent.putExtra("title",item.title)
            intent.putExtra("content", item.content)
            intent.putExtra("img", imgUrl)
            intent.putExtra("comment_count", item.comment_count)
            intent.putExtra("view_count", item.view_count)
            val s= Gson().toJson(item)
            intent.putExtra("QAData",s)
            context.startActivity(intent)

            QAG.QAId = item.qa_id
            QAG.QAText = item.content
            QAG.QAName = item.title
            QAG.QAImg = item.imgurl


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