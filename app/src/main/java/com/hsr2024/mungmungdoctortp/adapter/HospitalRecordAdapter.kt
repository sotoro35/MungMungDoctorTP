package com.hsr2024.mungmungdoctortp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hsr2024.mungmungdoctortp.bnv1care.HealthDetailActivity
import com.hsr2024.mungmungdoctortp.data.HospitalRecordData
import com.hsr2024.mungmungdoctortp.databinding.RecyclerItemHospitalRecordBinding

class HospitalRecordAdapter(val context: Context,val itemlist:List<HospitalRecordData> ) : Adapter<HospitalRecordAdapter.VH>() {
    inner class VH(val binding: RecyclerItemHospitalRecordBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutinflater = LayoutInflater.from(context)
        val binding = RecyclerItemHospitalRecordBinding.inflate(layoutinflater)


        return VH(binding)
    }

    override fun getItemCount(): Int {
        return  itemlist.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = itemlist[position]
        holder.binding.tvHospitalName.text = item.hospital_name
        holder.binding.tvDiseaseName.text = item.disease_name
        holder.binding.tvPrice.text = item.price
        holder.binding.tvDate.text = item.date

        holder.binding.tvDelete.setOnClickListener {
            //삭제하기
        }//온클릭



        holder.binding.root.setOnClickListener {

            //바인딩루트 클릭시 상세페이지이동
            val intent = Intent(context, HealthDetailActivity::class.java)
            context.startActivity(intent)

        }//온클릭

    }

}