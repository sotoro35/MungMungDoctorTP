package com.hsr2024.mungmungdoctortp.bnv1care

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.data.AddorModifyorDeleteEssentialVaccination
import com.hsr2024.mungmungdoctortp.data.EssentialVaccinationList
import com.hsr2024.mungmungdoctortp.databinding.ActivityMandatoryVaccineBinding
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MandatoryVaccineActivity : AppCompatActivity() {

    private var comprehensive = ""
    private var corona_enteritis = ""
    private var kennel_cough = ""
    private var influenza = ""
    private var antibody_titer = ""
    private var rabies = ""


    private val binding by lazy { ActivityMandatoryVaccineBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolBar.setNavigationOnClickListener { finish() }

        binding.dateTv.setOnClickListener { showDatePicker() }


        val shotNumber = intent.getIntExtra("shotNumber", 1)
        var titleText = intent.getStringExtra("titleText") ?: ""
        val checkBox1Text = intent.getStringExtra("checkBox1Text") ?: " "
        val checkBox2Text = intent.getStringExtra("checkBox2Text") ?: " "
        val checkBox3Text = intent.getBooleanExtra("checkBox3Text", false)

        when(shotNumber){
            1 -> titleText = "1차 접종"
            2 -> titleText = "2차 접종"
            3 -> titleText = "3차 접종"
            4 -> titleText = "4차 접종"
            5 -> titleText = "5차 접종"
            6 -> titleText = "6차 접종"
        }

        binding.toolBar.title = titleText
        binding.checkBox1Text.text = checkBox1Text
        binding.checkBox2Text.text = checkBox2Text
        binding.checkBox3Text.visibility = if (checkBox3Text) View.VISIBLE else View.GONE

        binding.btnSave.setOnClickListener { mandatoryVaccine() }


    }//온크리




    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("날짜 선택")
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            binding.dateTv.text = dateFormatter.format(Date(selection))
            binding.dateTv.setTextColor(Color.BLACK)
        }

        datePicker.show(supportFragmentManager, "DATE_PICKER")
    }
    private fun mandatoryVaccine(){

        val shot_number = binding.toolBar.title.toString().filter { it.isDigit() }
        val date = binding.dateTv.text.toString()
        val hospital = binding.etHospital.text.toString()
        val memo = binding.etMemo.text.toString()
        when(binding.toolBar.title){
            "1차 접종" -> {comprehensive = binding.checkBox1Text.text.toString()
                corona_enteritis = binding.checkBox2Text.text.toString()}
            "2차 접종" -> {comprehensive = binding.checkBox1Text.text.toString()
                corona_enteritis = binding.checkBox2Text.text.toString()}
            "3차 접종" -> {comprehensive = binding.checkBox1Text.text.toString()
                kennel_cough = binding.checkBox2Text.text.toString()}
            "4차 접종" -> {comprehensive = binding.checkBox1Text.text.toString()
                kennel_cough = binding.checkBox2Text.text.toString()}
            "5차 접종" -> {comprehensive = binding.checkBox1Text.text.toString()
                influenza = binding.checkBox2Text.text.toString()}
            "6차 접종" -> {rabies = binding.checkBox1Text.text.toString()
                influenza = binding.checkBox2Text.text.toString()
                antibody_titer = binding.checkBox3Text.text.toString()}
        }

        if (comprehensive.isEmpty() && corona_enteritis.isEmpty() && kennel_cough.isEmpty() && influenza.isEmpty() && antibody_titer.isEmpty() && rabies.isEmpty()){
            Toast.makeText(this, "접종 정보를 하나 이상 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if (date.isEmpty()){
            Toast.makeText(this, "접종 날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if (hospital.isEmpty()){
            Toast.makeText(this, "접종병원을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val params= AddorModifyorDeleteEssentialVaccination("${G.user_email}", "${G.user_providerId}", "${G.loginType}", "${G.pet_id}",
            "",                                    // 접종 기록 식별 값
            shot_number,                           // 몇회차 접종(1 ~ 6)
            comprehensive,                         // 종합백신 1차, 종합백신 2차, 종합백신 3차, 종합백신 4차, 종합백신 5차, ""
            corona_enteritis,                      // 코로나 장염1차, 코로나 장염 2차, ""
            kennel_cough,                          // 켄넬코프 1차, 켄넬코프 2차, ""
            influenza,                             // 인플루엔자 1차, 인플루엔자 2차, ""
            antibody_titer,                        // 항체가검사, ""
            rabies,                                // 광견병, ""
            date,                                  // 접종날짜
            hospital,                              // 접종할 병원 이름
            memo,                                  // 접종 시 메모정보
 )
        RetrofitProcess(this, params = params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val data = response as String
                Log.d("EssentialVaccination Add code",data)
                when (data) {
                    "4204", "9501" -> Toast.makeText(this@MandatoryVaccineActivity, "오류 발생: 관리자에게 문의하세요", Toast.LENGTH_SHORT).show()
                    "9500" -> {
                        Toast.makeText(this@MandatoryVaccineActivity, "접종 정보가 성공적으로 추가되었습니다", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }

            override fun onResponseFailure(errorMsg: String?) {
                Toast.makeText(this@MandatoryVaccineActivity, "실패: $errorMsg", Toast.LENGTH_SHORT).show()
            }
        }).addEssentialVaccinationRequest()
    }
}