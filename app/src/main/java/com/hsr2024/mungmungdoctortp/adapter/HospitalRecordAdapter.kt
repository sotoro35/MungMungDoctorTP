package com.hsr2024.mungmungdoctortp.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.bnv1care.HealthDetailActivity
import com.hsr2024.mungmungdoctortp.bnv1care.ShowHosActivity
import com.hsr2024.mungmungdoctortp.data.AddorModifyorDeleteHospital
import com.hsr2024.mungmungdoctortp.data.HospitalRecordData
import com.hsr2024.mungmungdoctortp.databinding.RecyclerItemHospitalRecordBinding
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess

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
        if (item.name == "") {
            holder.binding.tvHospitalName.text = "병원이름 미정"
        }else{
            holder.binding.tvHospitalName.text = item.name
        }
        holder.binding.tvDiseaseName.text = item.diagnosis
        if (item.price.contains("가격 미정")){
            holder.binding.tvPrice.text = "가격 미정"
        }else{
            holder.binding.tvPrice.text = "${item.price}원"
        }
        holder.binding.tvDate.text = item.visit_date




        //바인딩루트 클릭시 상세페이지이동
        holder.binding.root.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("type", "hospital")
            bundle.putString("name", item.name)
            bundle.putString("price", item.price)
            bundle.putString("date", item.visit_date)
            bundle.putString("disease_name", item.diagnosis)//진단명
            bundle.putString("content", item.description)//내용
            bundle.putString("bill_img", item.receipt_img_url)
            bundle.putString("clinic_img", item.clinical_img_url)
            bundle.putString("id", item.id)

            val intent = Intent(context, ShowHosActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }//온클릭


    }

}