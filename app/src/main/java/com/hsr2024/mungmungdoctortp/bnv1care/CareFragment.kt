package com.hsr2024.mungmungdoctortp.bnv1care

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.adapter.CareSelectDogAdapter
import com.hsr2024.mungmungdoctortp.adapter.MypageDogAdapter
import com.hsr2024.mungmungdoctortp.data.Pet
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

        load()



    }//onView...

    private fun load(){

        binding.petName.text = G.pet_name
        binding.petBreed.text = G.pet_breed
        binding.petBirthDate.text = G.pet_birthDate
        binding.petGender.text = G.pet_gender
        binding.petNeutering.text = "중성화 ${G.pet_neutering}"


//        if (G.pet_id != null && G.pet_id != ""){
//            binding.petName.text = G.pet_name
//            binding.petBreed.text = G.pet_breed
//            binding.petBirthDate.text = G.pet_birthDate
//            binding.petGender.text = G.pet_gender
//            binding.petNeutering.text = "중성화 ${G.pet_neutering}"
//        }else{
//            binding.petName.text = "마이페이지에서\n반려견을 등록해주세요"
//            binding.petBreed.visibility = View.INVISIBLE
//            binding.petBirthDate.visibility = View.INVISIBLE
//            binding.petGender.visibility = View.INVISIBLE
//            binding.petNeutering.visibility = View.INVISIBLE
//            binding.line1.visibility = View.INVISIBLE
//            binding.line2.visibility = View.INVISIBLE
//
//        }

        if (G.pet_imageUrl != null && G.pet_imageUrl != ""){
            Glide.with(this).load("https://43.200.163.153/img/${G.pet_imageUrl}").into(binding.petImageUrl)
        }
    }

    lateinit var alertDialog: AlertDialog
    @SuppressLint("MissingInflatedId")
    private fun selectPet(){
        val dialogV = layoutInflater.inflate(R.layout.dialog_dog_select,null)
        val recyclerView = dialogV.findViewById<RecyclerView>(R.id.recycler_select_pet)
        val builder = AlertDialog.Builder(requireContext())
        dialogV.findViewById<TextView>(R.id.dog_empty).visibility = View.VISIBLE
        builder.setView(dialogV)
        alertDialog = builder.create()

        var pets:List<Pet> = listOf(
            Pet("aa", "aa", "aa", "aa", "aa", "aa", "aa", "aa"),
            Pet("bb", "bb", "bb", "bb", "bb", "bb", "bb", "bb")

        )
            // 서버에 저장된 등록된 펫 정보 어댑터 연결해서 보여주기
            // 해당 펫 클릭시 보여지는 화면 변경
        if (pets != null){
            dialogV.findViewById<TextView>(R.id.dog_empty).visibility = View.INVISIBLE
            val mypageAdapter = CareSelectDogAdapter(requireContext(),pets)
            recyclerView.adapter = mypageAdapter
            mypageAdapter.notifyDataSetChanged()
        }

        dialogV.findViewById<TextView>(R.id.btn_select_pet).setOnClickListener {
            load()
            alertDialog.dismiss() }
        dialogV.findViewById<TextView>(R.id.btn_close).setOnClickListener { alertDialog.dismiss() }

        alertDialog.show()

    }

}