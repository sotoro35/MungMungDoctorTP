package com.hsr2024.mungmungdoctortp.bnv1care

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.databinding.ActivityMandatoryVaccineBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MandatoryVaccineActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMandatoryVaccineBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolBar.setNavigationOnClickListener { finish() }

        binding.dateTv.setOnClickListener { showDatePicker() }


        val checkBox1Text = intent.getStringExtra("checkBox1Text") ?: " "
        val checkBox2Text = intent.getStringExtra("checkBox2Text") ?: " "
        val checkBox3Text = intent.getBooleanExtra("checkBox3Text", false)

        binding.checkBox1Text.text = checkBox1Text
        binding.checkBox2Text.text = checkBox2Text
        binding.checkBox3Text.visibility = if (checkBox3Text) View.VISIBLE else View.GONE


    }
    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("날짜 선택")
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val dateFormatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
            binding.dateTv.text = dateFormatter.format(Date(selection))
            binding.dateTv.setTextColor(Color.BLACK)
        }

        datePicker.show(supportFragmentManager, "DATE_PICKER")
    }
}