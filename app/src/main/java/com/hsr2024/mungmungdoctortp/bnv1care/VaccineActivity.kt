package com.hsr2024.mungmungdoctortp.bnv1care

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.databinding.ActivityVaccineBinding

class VaccineActivity : AppCompatActivity() {

    private val binding by lazy { ActivityVaccineBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener { startActivity(Intent(this,AddVaccineActivity::class.java)) }
        binding.vac1.setOnClickListener { toMandatoryActivity("종합백신","코로나 장염",false) }
        binding.vac2.setOnClickListener { toMandatoryActivity("종합백신","코로나 장염",false) }
        binding.vac3.setOnClickListener { toMandatoryActivity("종합백신","켄넬코프",false) }
        binding.vac4.setOnClickListener { toMandatoryActivity("종합백신","켄넬코프",false) }
        binding.vac5.setOnClickListener { toMandatoryActivity("종합백신","인플루엔자",false) }
        binding.vac6.setOnClickListener { toMandatoryActivity("광견병","인플루엔자",true) }

    }
    private fun toMandatoryActivity(checkBox1Text: String, checkBox2Text: String,checkBox3Text: Boolean) {
        val intent = Intent(this, MandatoryVaccineActivity::class.java).apply {
            putExtra("checkBox1Text", checkBox1Text)
            putExtra("checkBox2Text", checkBox2Text)
            putExtra("checkBox3Text", checkBox3Text)
        }
        startActivity(intent)
    }
}