package com.hsr2024.mungmungdoctortp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.data.Pet
import com.hsr2024.mungmungdoctortp.data.Pet2
import com.hsr2024.mungmungdoctortp.databinding.RecyclerMypagePetBinding

class CareSelectDogAdapter(val context: Context, val pets:List<Pet>):
    RecyclerView.Adapter<CareSelectDogAdapter.caredogVH>() {

    // 초기화할 때 selectedItemPosition을 -1로 설정합니다.
    private var selectedItemPosition = -1

    inner class caredogVH(val binding: RecyclerMypagePetBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): caredogVH {
        return caredogVH(
            RecyclerMypagePetBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = pets.size

    override fun onBindViewHolder(holder: caredogVH, position: Int) {
        val pet = pets[position]
        holder.binding.mypagePetName.text = pet.pet_name

        if (pet.pet_imageUrl != null || pet.pet_imageUrl != "") {
            Glide.with(context).load("https://43.200.163.153/img/${G.user_imageUrl}")
                .into(holder.binding.mypagePetImage)
        }

        if (selectedItemPosition == position) {
            holder.binding.root.setBackgroundResource(R.drawable.bg_button_save)
        } else {
            holder.binding.root.setBackgroundResource(R.drawable.bg_pet_add)
        }

        holder.binding.root.setOnClickListener {

            val prevSelectedItemPosition = selectedItemPosition
            selectedItemPosition = holder.adapterPosition
            // 선택된 아이템이 변경되었을 때만 배경색을 변경합니다.
            if (prevSelectedItemPosition != selectedItemPosition) {
                // 이전에 선택된 아이템의 배경색을 변경합니다.
                if (prevSelectedItemPosition != -1) {
                    notifyItemChanged(prevSelectedItemPosition)
                }
                // 현재 클릭된 아이템의 배경색을 변경합니다.
                notifyItemChanged(selectedItemPosition)

                G.pet_id = pet.pet_id
                G.pet_imageUrl = pet.pet_imageUrl
                G.pet_birthDate = pet.pet_birthDate
                G.pet_gender = pet.pet_gender
                G.pet_neutering = pet.pet_neutering
                G.pet_breed = pet.pet_breed

            }


        }

    }
}
