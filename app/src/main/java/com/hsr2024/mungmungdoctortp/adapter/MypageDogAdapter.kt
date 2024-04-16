package com.hsr2024.mungmungdoctortp.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.bnv4mypage.DogChangeProfileActivity
import com.hsr2024.mungmungdoctortp.data.Pet
import com.hsr2024.mungmungdoctortp.data.Pet2
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
            Glide.with(context).load("http://43.200.163.153/img/${pet.pet_imageUrl}").into(holder.binding.mypagePetImage)
            Log.d("프로필사진","http://43.200.163.153/img/${pet.pet_imageUrl}")
        }

        holder.binding.root.setOnClickListener {
            val intent = Intent(context,DogChangeProfileActivity::class.java)
            val gson = Gson()
            val s:String = gson.toJson(pet)
            intent.putExtra("pet",s)
            context.startActivity(intent)
        }

    }
}