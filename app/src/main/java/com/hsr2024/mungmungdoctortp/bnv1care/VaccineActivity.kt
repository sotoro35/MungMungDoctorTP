package com.hsr2024.mungmungdoctortp.bnv1care

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.data.AdditionVaccinationList
import com.hsr2024.mungmungdoctortp.data.DeleteDog
import com.hsr2024.mungmungdoctortp.databinding.ActivityVaccineBinding
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess

class VaccineActivity : AppCompatActivity() {

    private val binding by lazy { ActivityVaccineBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.iv.setOnClickListener { finish() }
        binding.btnAdd.setOnClickListener { startActivity(Intent(this,AddVaccineActivity::class.java)) }
        binding.vac1.setOnClickListener { toMandatoryActivity("1차 접종","종합백신","코로나 장염",false) }
        binding.vac2.setOnClickListener { toMandatoryActivity("2차 접종","종합백신","코로나 장염",false) }
        binding.vac3.setOnClickListener { toMandatoryActivity("3차 접종","종합백신","켄넬코프",false) }
        binding.vac4.setOnClickListener { toMandatoryActivity("4차 접종","종합백신","켄넬코프",false) }
        binding.vac5.setOnClickListener { toMandatoryActivity("5차 접종","종합백신","인플루엔자",false) }
        binding.vac6.setOnClickListener { toMandatoryActivity("6차 접종","광견병","인플루엔자",true) }




    }
    private fun toMandatoryActivity(titleText: String,checkBox1Text: String, checkBox2Text: String,checkBox3Text: Boolean) {
        val intent = Intent(this, MandatoryVaccineActivity::class.java).apply {
            putExtra("titleText",titleText )
            putExtra("checkBox1Text", checkBox1Text)
            putExtra("checkBox2Text", checkBox2Text)
            putExtra("checkBox3Text", checkBox3Text)
        }
        startActivity(intent)
    }


}