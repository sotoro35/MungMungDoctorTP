package com.hsr2024.mungmungdoctortp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.bnv1care.AddVaccineActivity
import com.hsr2024.mungmungdoctortp.data.AdditionVaccination
import com.hsr2024.mungmungdoctortp.data.EssentialVaccination
import com.hsr2024.mungmungdoctortp.databinding.AddVaccineListBinding
import com.hsr2024.mungmungdoctortp.databinding.EssentialVaccineListBinding

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
}
class EssentialVaccinationAdapter(
    private val evContext: Context,
    private var vaccinations: List<EssentialVaccination>,
    private val itemClick: (EssentialVaccination) -> Unit
) : RecyclerView.Adapter<EssentialVaccinationAdapter.VaccinationViewHolder>() {

    inner class VaccinationViewHolder(val binding: EssentialVaccineListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                itemClick(vaccinations[adapterPosition])
            }
        }

        fun bind(vaccination: EssentialVaccination) {
            with(binding) {
                shotNumber.text = "${vaccination.shot_number}차 접종"
                vaccineName.text = formatVaccinationDetails(vaccination)
                vaccineDate.text = vaccination.date
                vaccineHospital.text = vaccination.hospital
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccinationViewHolder {
        val binding = EssentialVaccineListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VaccinationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VaccinationViewHolder, position: Int) {
        holder.bind(vaccinations[position])
    }

    override fun getItemCount() = vaccinations.size

    private fun formatVaccinationDetails(vaccination: EssentialVaccination): String {
        return listOfNotNull(
            vaccination.comprehensive.ifEmpty { null },
            vaccination.corona_enteritis.ifEmpty { null },
            vaccination.kennel_cough.ifEmpty { null },
            vaccination.influenza.ifEmpty { null },
            vaccination.antibody_titer.ifEmpty { null },
            vaccination.rabies.ifEmpty { null }
        ).joinToString(", ")
    }

    fun updateData(newVaccinations: List<EssentialVaccination>) {
        vaccinations = newVaccinations
        notifyDataSetChanged()
    }
}




