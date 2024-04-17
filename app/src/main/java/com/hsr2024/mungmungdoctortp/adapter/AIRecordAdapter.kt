package com.hsr2024.mungmungdoctortp.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hsr2024.mungmungdoctortp.bnv1care.AiResultActivity
import com.hsr2024.mungmungdoctortp.bnv1care.HealthDetailActivity
import com.hsr2024.mungmungdoctortp.bnv1care.ShowHosActivity
import com.hsr2024.mungmungdoctortp.data.AIRecordData
import com.hsr2024.mungmungdoctortp.databinding.RecyclerItemAiRecordBinding

class AIRecordAdapter(val context:Context, val itemList:List<AIRecordData>) : Adapter<AIRecordAdapter.VH>() {

    inner class VH(val binding : RecyclerItemAiRecordBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(context)
        val binding = RecyclerItemAiRecordBinding.inflate(inflater)
        return VH(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = itemList[position]
        holder.binding.tvCategory.text = item.diagnosis_type
        holder.binding.tvDiseaseName1.text = item.diagnosis_result
        holder.binding.tvDiseaseName2.text = item.diagnosis_result
        holder.binding.tvDate.text = item.id

        holder.binding.tvDelete.setOnClickListener {
            // x누르면 삭제
        }//온클릭리스너



        holder.binding.root.setOnClickListener {
            //바인딩루트누르면 상네내용으로 이동
            val bundle = Bundle()
            bundle.putString("type", "ai")
            bundle.putString("result1", item.diagnosis_result)
            bundle.putString("result2", item.diagnosis_result)
            bundle.putString("img", item.diagnostic_img_url)

            val intent = Intent(context, AiResultActivity::class.java)

            intent.putExtras(bundle)
            context.startActivity(intent)
        }//온클릭리스너






    }//onBindViewHolder()
}
