package com.hsr2024.mungmungdoctortp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hsr2024.mungmungdoctortp.data.EssentialVaccination
import com.hsr2024.mungmungdoctortp.databinding.MandatoryVaccineListBinding

class MandatoryAdapter(
    private var vaccineList: List<EssentialVaccination>,
    private val itemClick: (EssentialVaccination) -> Unit
) : RecyclerView.Adapter<MandatoryAdapter.VHolder>() {

    inner class VHolder(val binding: MandatoryVaccineListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) { // 안전한 클릭 처리
                    itemClick(vaccineList[adapterPosition])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        val binding =
            MandatoryVaccineListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VHolder(binding)
    }

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        val vaccination = vaccineList[position]
        with(holder.binding) {
            vaccineNum.text = vaccination.shot_number + "차 접종"
            vaccineDate.text = vaccination.date
            vaccineHospital.text = vaccination.hospital
            vaccineName.text = formatVaccinationDetails(vaccination)
        }
    }

    override fun getItemCount(): Int = vaccineList.size

    private fun formatVaccinationDetails(vaccination: EssentialVaccination): String {
        return listOfNotNull(
            vaccination.comprehensive.takeIf { it.isNotEmpty() },
            vaccination.corona_enteritis.takeIf { it.isNotEmpty() },
            vaccination.kennel_cough.takeIf { it.isNotEmpty() },
            vaccination.influenza.takeIf { it.isNotEmpty() },
            vaccination.antibody_titer.takeIf { it.isNotEmpty() },
            vaccination.rabies.takeIf { it.isNotEmpty() }
        ).joinToString(separator = "\n")
    }
}