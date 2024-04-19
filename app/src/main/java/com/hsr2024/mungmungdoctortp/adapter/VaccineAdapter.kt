package com.hsr2024.mungmungdoctortp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.bnv1care.AddVaccineActivity
import com.hsr2024.mungmungdoctortp.data.AdditionVaccination
import com.hsr2024.mungmungdoctortp.databinding.AddVaccineListBinding

class VaccineAdapter(
    private val context: Context,
    private var vaccineList: List<AdditionVaccination>,
    private val itemClick : (AdditionVaccination) -> Unit) : RecyclerView.Adapter<VaccineAdapter.VH>(){
    inner class VH(val binding: AddVaccineListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                itemClick(vaccineList[adapterPosition])  // 클릭된 아이템의 데이터 전달
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccineAdapter.VH {
        val binding = AddVaccineListBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return VH(binding)
    }


    override fun onBindViewHolder(holder: VH, position: Int) {
        val vaccination = vaccineList[position]
        with(holder.binding){
            vaccineName.text = formatVaccineationNames(vaccination)
            vaccineDate.text = vaccination.date
            vaccineHospital.text = vaccination.hospital
            vaccineMemo.text = vaccination.memo


        }


    }

    override fun getItemCount(): Int {
        return vaccineList.size
    }

    private fun formatVaccineationNames(vaccination : AdditionVaccination) : String {
        val names = mutableListOf<String>()
        if (vaccination.heartworm == "TRUE") names.add("심장사상충")
        if (vaccination.external_parasites == "TRUE") names.add("외부기생충")
        if (vaccination.vaccine.isNotEmpty()) names.add(vaccination.vaccine)
        return names.joinToString ( ", " )
    }
    fun updateData(newVaccineList: List<AdditionVaccination>) {
        vaccineList = newVaccineList
        notifyDataSetChanged()
    }

    //holder.binding.root.setOnClickListener {
    //            val intent = Intent(context,ItemDetailActivity::class.java)
    //            val gson= Gson()
    //            val s:String = gson.toJson(item) // 객체 --> json String
    //            intent.putExtra("place",s)
    //            context.startActivity(intent)
    //        }


}