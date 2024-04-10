package com.hsr2024.mungmungdoctortp.bnv1care

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.R
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

        binding.selectPet.setOnClickListener { selectPet() }
        binding.note.setOnClickListener {  }
        binding.eyeAi.setOnClickListener {  }
        binding.skinAi.setOnClickListener {  }
        binding.btnHealth.setOnClickListener {  }


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


    }//onView...

    lateinit var alertDialog: AlertDialog
    private fun selectPet(){
        val dialogV = layoutInflater.inflate(R.layout.dialog_dog_select,null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogV)
        alertDialog = builder.create()

        // 서버에 저장된 등록된 펫 정보 어댑터 연결해서 보여주기
        // 해당 펫 클릭시 보여지는 화면 변경

        dialogV.findViewById<TextView>(R.id.btn_close).setOnClickListener { alertDialog.dismiss() }

        alertDialog.show()

    }

}