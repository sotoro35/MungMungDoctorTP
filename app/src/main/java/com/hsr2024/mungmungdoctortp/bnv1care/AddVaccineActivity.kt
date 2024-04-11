package com.hsr2024.mungmungdoctortp.bnv1care

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.datepicker.MaterialDatePicker
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.databinding.ActivityAddVaccineBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddVaccineActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAddVaccineBinding.inflate(layoutInflater) }
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
    }
}