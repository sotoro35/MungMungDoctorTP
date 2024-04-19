package com.hsr2024.mungmungdoctortp.bnv1care

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.hsr2024.mungmungdoctortp.network.RetrofitHelper
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess
import com.hsr2024.mungmungdoctortp.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddVaccineActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAddVaccineBinding.inflate(layoutInflater) }
    private var vaccineId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.toolBar.setNavigationOnClickListener { finish() }

        binding.dateTv.setOnClickListener { showDatePicker() }
        binding.btnSave.setOnClickListener { addVaccine() }
        binding.btnDelete.setOnClickListener { addVaccineDelete() }


        binding.btnDelete.visibility = if (vaccineId == null) View.GONE else View.VISIBLE
        initializeFormData()
        setupSaveButton()

    }
    private fun initializeFormData() {
        vaccineId = intent.getStringExtra("id")
        // 인텐트에서 데이터 받기
        val heartworm = intent.getStringExtra("heartworm") == "TRUE"
        val externalParasites = intent.getStringExtra("external_parasites") == "TRUE"
        val vaccine = intent.getStringExtra("vaccine")
        val date = intent.getStringExtra("date")
        val hospital = intent.getStringExtra("hospital")
        val memo = intent.getStringExtra("memo")

        if (!date.isNullOrEmpty()) {
            binding.dateTv.setText(date)
        } else {
            binding.dateTv.setText("날짜를 선택해주세요")  // 날짜가 비어있으면 초기 텍스트 설정
        }

        // 뷰 업데이트
        binding.checkBox1Text.isChecked = heartworm
        binding.checkBox2Text.isChecked = externalParasites
        binding.et.setText(vaccine)
        binding.dateTv.text = date
        binding.etHospital.setText(hospital)
        binding.etMemo.setText(memo)
    }
    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            if (vaccineId.isNullOrEmpty()) {
                addVaccine()  // 식별 값이 없으면 추가
            } else {
                sendModifiedVaccinationData()  // 식별 값이 있으면 수정
            }
        }
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

        if (heartworm.isEmpty() && external_parasites.isEmpty() && vaccine.isEmpty()){
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

    private fun addVaccineDelete() {
        // AlertDialog.Builder 인스턴스를 생성하고, 메시지, 타이틀, 버튼을 설정합니다.
        val dialog = AlertDialog.Builder(this)
            .setTitle("기록 삭제")  // 다이얼로그의 타이틀 설정
            .setMessage("삭제하시겠습니까?")  // 다이얼로그의 메시지 설정
            .setPositiveButton("삭제") { dialogInterface, which ->
                // 삭제 버튼 클릭 이벤트
                deleteVaccination()
            }
            .setNegativeButton("취소", null)  // 취소 버튼 클릭 이벤트
            .create()  // AlertDialog를 생성합니다.

        dialog.show()  // 다이얼로그를 화면에 표시합니다.
    }

    private fun deleteVaccination() {
        val params = vaccineId?.let {
            AddorModifyorDeleteAdditionVaccination(
                email = G.user_email,
                provider_id = G.user_providerId,
                login_type = "email",
                pet_id = G.pet_id,
                id = it, // 수정할 기록의 식별자

        )}

        if (params != null) {
            RetrofitProcess(this@AddVaccineActivity, params = params, callback = object : RetrofitCallback {
                override fun onResponseListSuccess(response: List<Any>?) {}

                override fun onResponseSuccess(response: Any?) {
                    val code = response as String
                    Log.d("AdditionVaccination Delete code", code)
                    when (code) {
                        "8700" -> {
                            Toast.makeText(this@AddVaccineActivity, "삭제 완료되었습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        else -> Toast.makeText(this@AddVaccineActivity, "삭제 실패: $code", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponseFailure(errorMsg: String?) {
                    Log.d("AdditionVaccination Delete fail", errorMsg ?: "Unknown error")
                    Toast.makeText(this@AddVaccineActivity, "네트워크 오류: $errorMsg", Toast.LENGTH_SHORT).show()
                }
            }).deleteAdditionVaccinationRequest()
        }
    }


    private fun sendModifiedVaccinationData() {
        val params = vaccineId?.let {
            AddorModifyorDeleteAdditionVaccination(
                email = G.user_email,
                provider_id = G.user_providerId,
                login_type = "email",
                pet_id = G.pet_id,
                id = it, // 수정할 기록의 식별자
                heartworm = binding.checkBox1Text.isChecked.toString(),
                external_parasites = binding.checkBox2Text.isChecked.toString(),
                vaccine = binding.et.text.toString(),
                date = binding.dateTv.text.toString(),
                hospital = binding.etHospital.text.toString(),
                memo = binding.etMemo.text.toString()
            )
        }

        // 매개변수 로그 출력
        Log.d("ModifyRequest", "Params: $params")

        if (params != null) {
            RetrofitProcess(this, params = params, callback = object : RetrofitCallback {
                override fun onResponseListSuccess(response: List<Any>?) {
                    TODO("Not yet implemented")
                }

                override fun onResponseSuccess(response: Any?) {
                    Toast.makeText(this@AddVaccineActivity, "수정 성공", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                override fun onResponseFailure(errorMsg: String?) {
                    Toast.makeText(this@AddVaccineActivity, "수정 실패: $errorMsg", Toast.LENGTH_SHORT).show()
                }
            }).modifyAdditionVaccinationRequest()
        }
    }
}






