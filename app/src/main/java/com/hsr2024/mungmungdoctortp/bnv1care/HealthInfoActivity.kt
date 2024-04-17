package com.hsr2024.mungmungdoctortp.bnv1care

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.data.Eye1
import com.hsr2024.mungmungdoctortp.data.HealthDetailData
import com.hsr2024.mungmungdoctortp.databinding.ActivityHealthInfoBinding

class HealthInfoActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHealthInfoBinding.inflate(layoutInflater) }
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_health_detail1)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        setContentView(binding.root)



        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.framelayoutEye.visibility = View.VISIBLE

        binding.btnEye.setBackgroundColor(resources.getColor(R.color.health_detail_btn_bgcolor))
        binding.btnEye.setTextColor(resources.getColor(R.color.health_detail_btn_tvcolor))



        binding.btnEye.setOnClickListener {
            binding.framelayoutEye.visibility = View.VISIBLE
            binding.framelayoutSkin.visibility = View.GONE
            binding.framelayoutEtc.visibility = View.GONE

            binding.btnEye.setBackgroundColor(resources.getColor(R.color.health_detail_btn_bgcolor))
            binding.btnEye.setTextColor(resources.getColor(R.color.health_detail_btn_tvcolor))
            binding.btnSkin.setBackgroundColor(Color.WHITE)
            binding.btnSkin.setTextColor(Color.BLACK)
            binding.btnEtc.setBackgroundColor(Color.WHITE)
            binding.btnEtc.setTextColor(Color.BLACK)
        }

        binding.btnSkin.setOnClickListener {
            binding.framelayoutEye.visibility = View.GONE
            binding.framelayoutSkin.visibility = View.VISIBLE
            binding.framelayoutEtc.visibility = View.GONE

            binding.btnSkin.setBackgroundColor(resources.getColor(R.color.health_detail_btn_bgcolor))
            binding.btnSkin.setTextColor(resources.getColor(R.color.health_detail_btn_tvcolor))
            binding.btnEye.setBackgroundColor(Color.WHITE)
            binding.btnEye.setTextColor(Color.BLACK)
            binding.btnEtc.setBackgroundColor(Color.WHITE)
            binding.btnEtc.setTextColor(Color.BLACK)
        }

        binding.btnEtc.setOnClickListener {
            binding.framelayoutEye.visibility = View.GONE
            binding.framelayoutSkin.visibility = View.GONE
            binding.framelayoutEtc.visibility = View.VISIBLE

            binding.btnEtc.setBackgroundColor(resources.getColor(R.color.health_detail_btn_bgcolor))
            binding.btnEtc.setTextColor(resources.getColor(R.color.health_detail_btn_tvcolor))
            binding.btnSkin.setBackgroundColor(Color.WHITE)
            binding.btnSkin.setTextColor(Color.BLACK)
            binding.btnEye.setBackgroundColor(Color.WHITE)
            binding.btnEye.setTextColor(Color.BLACK)

        }


        binding.eyeRelative1.setOnClickListener { goIntent1() }//안검염/각막염/결막염
        binding.eyeRelative2.setOnClickListener { goIntent2() }//안검종양
        binding.eyeRelative3.setOnClickListener { goIntent3() }//유루증
        binding.eyeRelative4.setOnClickListener { goIntent4() }//각막궤양
        binding.eyeRelative5.setOnClickListener { goIntent5() }//백내장

        binding.skinRelative1.setOnClickListener { goIntentSkin1() }//구진/플라크
        binding.skinRelative2.setOnClickListener { goIntentSkin2() }//비듬각질상피성잔고리
        binding.skinRelative3.setOnClickListener { goIntentSkin3() }//태선화.과다색소침착
        binding.skinRelative4.setOnClickListener { goIntentSkin4() }//농포.여드름
        binding.skinRelative5.setOnClickListener { goIntentSkin5() }//미란궤양
        binding.skinRelative6.setOnClickListener { goIntentSkin6() }//결절종궤




    }//oncreate()

    private fun goIntent1(){
        val intent1 = Intent(this, HealthDetailActivity::class.java)
        intent1.putExtra("eye1", "eye1")
        startActivity(intent1)
    }


    private fun goIntent2(){
        val intent2 = Intent(this, HealthDetailActivity::class.java)
        intent2.putExtra("eye2", "eye2")
        startActivity(intent2)
    }

    private fun goIntent3(){
        val intent3 = Intent(this, HealthDetailActivity::class.java)
        intent3.putExtra("eye3", "eye3")
        startActivity(intent3)
    }

    private fun goIntent4(){
        val intent4 = Intent(this, HealthDetailActivity::class.java)
        intent4.putExtra("eye4", "eye4")
        startActivity(intent4)
    }

    private fun goIntent5(){
        val intent5 = Intent(this, HealthDetailActivity::class.java)
        intent5.putExtra("eye5", "eye5")
        startActivity(intent5)
    }










    private fun goIntentSkin1(){
        val intentSkin1 = Intent(this, HealthDetailActivity::class.java)
        intentSkin1.putExtra("skin1", "skin1")
        startActivity(intentSkin1)
    }

    private fun goIntentSkin2(){
        val intentSkin2 = Intent(this, HealthDetailActivity::class.java)
        intentSkin2.putExtra("skin2", "skin2")
        startActivity(intentSkin2)
    }

    private fun goIntentSkin3(){
        val intentSkin3 = Intent(this, HealthDetailActivity::class.java)
        intentSkin3.putExtra("skin3", "skin3")
        startActivity(intentSkin3)
    }

    private fun goIntentSkin4(){
        val intentSkin4 = Intent(this, HealthDetailActivity::class.java)
        intentSkin4.putExtra("skin4", "skin4")
        startActivity(intentSkin4)
    }

    private fun goIntentSkin5(){
        val intentSkin5 = Intent(this, HealthDetailActivity::class.java)
        intentSkin5.putExtra("skin5", "skin5")
        startActivity(intentSkin5)
    }

    private fun goIntentSkin6(){
        val intentSkin6 = Intent(this, HealthDetailActivity::class.java)
        intentSkin6.putExtra("skin6", "skin6")
        startActivity(intentSkin6)
    }





}//acvitity