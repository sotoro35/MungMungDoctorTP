package com.hsr2024.mungmungdoctortp.bnv1care

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.datepicker.MaterialDatePicker
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.data.AdditionVaccinationList
import com.hsr2024.mungmungdoctortp.data.AddorModifyorDeleteAdditionVaccination
import com.hsr2024.mungmungdoctortp.data.DeleteDog
import com.hsr2024.mungmungdoctortp.databinding.ActivityAddVaccineBinding
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddVaccineActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAddVaccineBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.toolBar.setNavigationOnClickListener { finish() }

        binding.dateTv.setOnClickListener { showDatePicker() }
        binding.btnSave.setOnClickListener { addVaccine() }
        binding.addVaccineRemove.setOnClickListener { addVaccineRemove() }

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
    private fun addVaccine() {
        val heartworm = binding.checkBox1Text.isChecked.toString()
        val external_parasites = binding.checkBox2Text.isChecked.toString()
        val vaccine = binding.et.text.toString()
        val date = binding.dateTv.text.toString().replace("-","")
        val hospital = binding.etHospital.text.toString()
        val memo = binding.etMemo.text.toString()

        val params= AddorModifyorDeleteAdditionVaccination("${G.user_email}", "${G.user_providerId}", "email", "${G.pet_id}",
                    "",                                    // 접종 기록 식별 값
                    heartworm,                             // 심상사상충 접종여부 TRUE OR FALSE
                    external_parasites,                    // 외부기생충 접종여부 TRUE OR FALSE
                    vaccine,                               // 기타 접종 이름
                    date,                                  // 접종날짜
                    hospital,                              // 접종할 병원 이름
                    memo,                                  // 접종 시 메모정보
         )
        RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val  code=(response as String)
                Log.d("AdditionVaccination Add code","$code")  //  - 4204 서비스 회원 아님, 8500 추가 접종 추가 성공, 8501 추가 접종 추가 실패
                when(code){
                    "4204" -> Toast.makeText(this@AddVaccineActivity, "관리자에게 문의하세요", Toast.LENGTH_SHORT).show()
                    "8501" -> Toast.makeText(this@AddVaccineActivity, "관리자에게 문의하세요", Toast.LENGTH_SHORT).show()
                    "8500" -> {
                        Toast.makeText(this@AddVaccineActivity, "등록이 완료되었습니다", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("AdditionVaccination Add fail",errorMsg!!) // 에러 메시지
            }

        }).addAdditionVaccinationRequest()
    }

    private fun addVaccineRemove() {


    }
    private fun additionList() {

        val params= DeleteDog("${G.user_email}", "${G.user_providerId}", "", "") // pet_id는 pet 식별값
        RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val data=(response as AdditionVaccinationList)
                data.code                                                  //  - 4204 서비스 회원 아님, 8400 추가 접종 목록 성공, 8401 추가 접종 목록 실패
                Log.d("AdditionVaccination List code","data")
                data.vaccinationList.forEach{vaccination ->               // forEach문을 돌면서 추가 접종 목록 정보를 가져올 수 있음
                    vaccination.id                                        // 접종 기록 식별 값
                    vaccination.heartworm                                 // 심상사상충 접종여부 TRUE OR FALSE
                    vaccination.external_parasites                        // 외부기생충 접종여부 TRUE OR FALSE
                    vaccination.vaccine                                   // 기타 접종 이름
                    vaccination.date                                      // 접종날짜
                    vaccination.hospital                                  // 접종할 병원 이름
                    vaccination.memo                                      // 접종 시 메모정보
                }
            }
            override fun onResponseFailure(errorMsg: String?) {
                Log.d("AdditionVaccination List fail",errorMsg!!) // 에러 메시지
            }

        }).listAdditionVaccinationRequest()
    }


}