package com.hsr2024.mungmungdoctortp.bnv1care

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.databinding.ActivityShowHosBinding

class ShowHosActivity : AppCompatActivity() {

    private val binding by lazy { ActivityShowHosBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        selectServer()
    }//온크리




    private fun selectServer(){
        //서버에서 데이터 가져오기
//        val retrofitService =
//            RetrofitHelper.getRetrofitInstance().create(RetrofitService::class.java)

//        binding.tvName
//        binding.tvPrice
//        binding.tvDate
//        binding.tvDiseaseName
//        binding.tvContent
//        binding.ivBill.setImageURI()
//        binding.ivCare.setImageURI()


    }//selectServer()


}//액티비티