package com.hsr2024.mungmungdoctortp.bnv1care

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hsr2024.mungmungdoctortp.R



class HealthDetailActivity : AppCompatActivity() {

    private val  binding by lazy { com.hsr2024.mungmungdoctortp.databinding.ActivityHealthDetailBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)






    }//oncreate()






}//activity