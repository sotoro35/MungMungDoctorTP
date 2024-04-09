package com.hsr2024.mungmungdoctortp.bnv1care

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.databinding.FragmentCareBinding

class CareFragment:Fragment() {

    private val binding by lazy { FragmentCareBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectPet.setOnClickListener {  }
        binding.note.setOnClickListener {  }
        binding.eyeAi.setOnClickListener {  }
        binding.skinAi.setOnClickListener {  }
        binding.btn.setOnClickListener {  }


        if (G.pet_id != null && G.pet_id != ""){
            binding.petName.text = G.pet_name
            binding.petBreed.text = G.pet_breed
            binding.petBirthDate.text = G.pet_birthDate
            binding.petGender.text = G.pet_gender
            binding.petNeutering.text = "중성화 ${G.pet_neutering}"
        }


        if (G.pet_imageUrl != null && G.pet_imageUrl != ""){
            Glide.with(this).load(G.pet_imageUrl).into(binding.petImageUrl)
        }


    }

}