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
        holder.binding.tvHospitalName.text = item.name
        holder.binding.tvDiseaseName.text = item.diagnosis
        holder.binding.tvPrice.text = item.price
        holder.binding.tvDate.text = item.visit_date

        holder.binding.tvDelete.setOnClickListener {
            AlertDialog.Builder(context).setMessage("병원기록을 삭제하시겠습니까?")
                .setPositiveButton("삭제", object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        Toast.makeText(context, "삭제하기", Toast.LENGTH_SHORT).show()
                        //서버에서 삭제하기
                    }
                })
                .setNegativeButton("취소", object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        TODO("Not yet implemented")
                    }
                })

        }//온클릭


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

            val intent = Intent(context, ShowHosActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }//온클릭

        holder.binding.tvDelete.setOnClickListener {
            // hospitalDeleteRequest 사용법
            val params= AddorModifyorDeleteHospital(
                G.user_email, G.user_providerId, G.loginType, G.pet_id, // pet_id는 pet 식별값
                                                    //??? id // 병원 기록 식별 값
             )
            RetrofitProcess(context, params=params, callback = object : RetrofitCallback {
                override fun onResponseListSuccess(response: List<Any>?) {}

                override fun onResponseSuccess(response: Any?) {
                    val code=(response as String)             //  - 4204 서비스 회원 아님, 8300 병원 기록 삭제 성공, 8301 병원 기록 삭제 실패
                    Log.d("hospital delete code","$code")

                }

                override fun onResponseFailure(errorMsg: String?) {
                    Log.d("hospital delete fail",errorMsg!!) // 에러 메시지
                }

            }).hospitalDeleteRequest()
        }//온클릭

    }

}