package com.hsr2024.mungmungdoctortp.bnv1care

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.datepicker.MaterialDatePicker
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.databinding.ActivityMandatoryVaccineBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MandatoryVaccineActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMandatoryVaccineBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolBar.setNavigationOnClickListener { finish() }

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("날짜 선택")
            .build()

        binding.dateTv.setOnClickListener { datePicker.show(supportFragmentManager,datePicker.toString()) }

        datePicker.addOnPositiveButtonClickListener {
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateString = dateFormatter.format(Date(it))
            binding.dateTv.text = dateString
        }

        val checkBox1Text = intent.getStringExtra("checkBox1Text") ?: " "
        val checkBox2Text = intent.getStringExtra("checkBox2Text") ?: " "
        val checkBox3Text = intent.getBooleanExtra("checkBox3Text", false)

        binding.checkBox1Text.text = checkBox1Text
        binding.checkBox2Text.text = checkBox2Text
        binding.checkBox3Text.visibility = if (checkBox3Text) View.VISIBLE else View.GONE

    }
}