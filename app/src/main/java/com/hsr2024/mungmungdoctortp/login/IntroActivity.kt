package com.hsr2024.mungmungdoctortp.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.databinding.ActivityIntroBinding
import com.hsr2024.mungmungdoctortp.main.MainActivity

class IntroActivity : AppCompatActivity() {

    private val binding by lazy { ActivityIntroBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Glide.with(this).load(R.drawable.intro).into(binding.introIv)

        val preferences = getSharedPreferences("UserData", MODE_PRIVATE)
        val email =preferences.getString("user_email","")
        if (email == null || email.equals("")){
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this,LoginActivity::class.java))
                finish()},2000)
        }else{
            G.apply {
                user_email = preferences.getString("user_email","")!!
                user_nickname = preferences.getString("user_nickname","")!!
                user_imageUrl = preferences.getString("user_imageUrl","")!!
                pet_id = preferences.getString("pet_id","")!!
                pet_name = preferences.getString("pet_name","")!!
                pet_imageUrl = preferences.getString("pet_imageUrl","")!!
                pet_birthDate = preferences.getString("pet_birthDate","")!!
                pet_gender = preferences.getString("pet_gender","")!!
                pet_neutering = preferences.getString("pet_neutering","")!!
                pet_breed = preferences.getString("pet_breed","")!!
            }

            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this,MainActivity::class.java))
                finish()},2000)
        }





    }
}