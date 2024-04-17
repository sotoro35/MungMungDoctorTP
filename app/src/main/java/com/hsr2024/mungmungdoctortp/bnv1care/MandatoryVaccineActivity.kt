package com.hsr2024.mungmungdoctortp.bnv1care

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
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

    private val binding by lazy { ActivityMandatoryVaccineBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolBar.setNavigationOnClickListener { finish() }

        binding.dateTv.setOnClickListener { showDatePicker() }
        binding.btnSave.setOnClickListener { mandatoryVaccine() }


        val titleText = intent.getStringExtra("titleText") ?: ""
        val checkBox1Text = intent.getStringExtra("checkBox1Text") ?: " "
        val checkBox2Text = intent.getStringExtra("checkBox2Text") ?: " "
        val checkBox3Text = intent.getBooleanExtra("checkBox3Text", false)

        binding.toolBar.title = titleText
        binding.checkBox1Text.text = checkBox1Text
        binding.checkBox2Text.text = checkBox2Text
        binding.checkBox3Text.visibility = if (checkBox3Text) View.VISIBLE else View.GONE


    }
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
        val shot_number = binding.toolBar.title.toString()
        val comprehensive = binding.checkBox1Text.isChecked.toString()
        val corona_enteritis = binding.checkBox2Text.isChecked.toString()
        val date = binding.dateTv.text.toString().replace("-","")
        val hospital = binding.etHospital.text.toString()
        val memo = binding.etMemo.text.toString()

        val params= AddorModifyorDeleteEssentialVaccination("${G.user_email}", "${G.user_providerId}", "email", "${G.pet_id}",
            "",                                    // 접종 기록 식별 값
            shot_number,                           // 몇회차 접종(1 ~ 6)
            comprehensive,                         // 종합백신 1차, 종합백신 2차, 종합백신 3차, 종합백신 4차, 종합백신 5차, ""
            corona_enteritis,                      // 코로나 장염1차, 코로나 장염 2차, ""
//            kennel_cough,                          // 켄넬코프 1차, 켄넬코프 2차, ""
//            influenza,                             // 인플루엔자 1차, 인플루엔자 2차, ""
//            antibody_titer,                        // 항체가검사, ""
//            rabies,                                // 광견병, ""
            date,                                  // 접종날짜
            hospital,                              // 접종할 병원 이름
            memo,                                  // 접종 시 메모정보
 )
RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
    override fun onResponseListSuccess(response: List<Any>?) {}

    override fun onResponseSuccess(response: Any?) {
        val data=(response as EssentialVaccinationList)
        Log.d("EssentialVaccination Add code","data")                //  - 4204 서비스 회원 아님, 9500 필수 접종 추가 성공, 9501 필수 접종 추가 실패
    }

    override fun onResponseFailure(errorMsg: String?) {
        Log.d("EssentialVaccination Add fail",errorMsg!!) // 에러 메시지
    }

}).addEssentialVaccinationRequest()
    }
}