package com.hsr2024.mungmungdoctortp.login

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.databinding.ActivityDogAddBinding

class DogAddActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDogAddBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnAddPetImage.paintFlags = Paint.UNDERLINE_TEXT_FLAG // 밑줄
    }
}