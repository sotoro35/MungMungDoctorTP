package com.hsr2024.mungmungdoctortp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.data.Pet
import com.hsr2024.mungmungdoctortp.databinding.RecyclerMypagePetBinding

class MypageDogAdapter(val context: Context, val pets:List<Pet>):Adapter<MypageDogAdapter.dogVH>() {

    inner class dogVH(val binding:RecyclerMypagePetBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): dogVH {
        return dogVH(RecyclerMypagePetBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int = pets.size

    override fun onBindViewHolder(holder: dogVH, position: Int) {
        val pet = pets[position]
        holder.binding.mypagePetName.text = pet.pet_name

        if (pet.pet_imageUrl !=null || pet.pet_imageUrl != ""){
            Glide.with(context).load("https://43.200.163.153/img/${G.user_imageUrl}").into(holder.binding.mypagePetImage)
        }

    }
}