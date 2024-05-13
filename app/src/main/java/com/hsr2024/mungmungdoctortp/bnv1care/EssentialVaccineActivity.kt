package com.hsr2024.mungmungdoctortp.bnv1care

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.datepicker.MaterialDatePicker
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.data.AddorModifyorDeleteEssentialVaccination
import com.hsr2024.mungmungdoctortp.databinding.ActivityEssentialVaccineBinding
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.log

class EssentialVaccineActivity : AppCompatActivity() {

    var comprehensive = ""
    var corona_enteritis = ""
    var kennel_cough = ""
    var influenza = ""
    var antibody_titer = ""
    var rabies = ""

    private var shotNumber = 0

    private val binding by lazy { ActivityEssentialVaccineBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.toolBar.setNavigationOnClickListener { finish() }
        binding.btnSave.setOnClickListener { mandatoryVaccine() }
        binding.dateTv.setOnClickListener { showDatePicker() }
        setupSpinner()

    }

    private fun setupSpinner() {
        val stages = listOf("1차 접종", "2차 접종", "3차 접종", "4차 접종", "5차 접종", "6차 접종")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, stages)
        binding.shotNumber.adapter = adapter

        binding.shotNumber.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                updateCheckboxes(position)
                shotNumber = position + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
    private fun updateCheckboxes(position: Int) {
        val vaccineOptions = listOf(
            listOf("종합백신 1차", "코로나 장염 1차"),
            listOf("종합백신 2차", "코로나 장염 2차"),
            listOf("종합백신 3차", "켄넬코프 1차"),
            listOf("종합백신 4차", "켄넬코프 2차"),
            listOf("종합백신 5차", "인플루엔자 1차"),
            listOf("광견병", "인플루엔자 2차", "항체가검사")
        )

        val options = vaccineOptions[position]
        binding.checkBox1Text.text = options.getOrNull(0) ?: ""  // 첫 번째 옵션, 없으면 비움
        binding.checkBox2Text.text = options.getOrNull(1) ?: ""  // 두 번째 옵션, 없으면 비움
        binding.checkBox3Text.text = options.getOrNull(2) ?: ""  // 세 번째 옵션, 없으면 비움

        // 세 번째 체크박스의 보이기/숨기기
        binding.checkBox3Text.visibility = if (position == 5) View.VISIBLE else View.GONE
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


        val shot_number = binding.shotNumber.selectedItem.toString().filter { it.isDigit() }
        val date = binding.dateTv.text.toString()
        val hospital = binding.etHospital.text.toString()
        val memo = binding.etMemo.text.toString()

        when(shot_number) {
            "1" -> {
                if (binding.checkBox1Text.isChecked) comprehensive = binding.checkBox1Text.text.toString()
                if (binding.checkBox2Text.isChecked) corona_enteritis = binding.checkBox2Text.text.toString()
            }
            "2" -> {
                if (binding.checkBox1Text.isChecked) comprehensive = binding.checkBox1Text.text.toString()
                if (binding.checkBox2Text.isChecked) corona_enteritis = binding.checkBox2Text.text.toString()
            }
            "3" -> {
                if (binding.checkBox1Text.isChecked) comprehensive = binding.checkBox1Text.text.toString()
                if (binding.checkBox2Text.isChecked) kennel_cough = binding.checkBox2Text.text.toString()
            }
            "4" -> {
                if (binding.checkBox1Text.isChecked) comprehensive = binding.checkBox1Text.text.toString()
                if (binding.checkBox2Text.isChecked) kennel_cough = binding.checkBox2Text.text.toString()
            }
            "5" -> {
                if (binding.checkBox1Text.isChecked) comprehensive = binding.checkBox1Text.text.toString()
                if (binding.checkBox2Text.isChecked) influenza = binding.checkBox2Text.text.toString()
            }
            "6" -> {
                if (binding.checkBox1Text.isChecked) rabies = binding.checkBox1Text.text.toString()
                if (binding.checkBox2Text.isChecked) influenza = binding.checkBox2Text.text.toString()
                if (binding.checkBox3Text.isChecked) antibody_titer = binding.checkBox3Text.text.toString()
            }
        }
        
        if (comprehensive.isEmpty() && corona_enteritis.isEmpty() && kennel_cough.isEmpty() && influenza.isEmpty() && antibody_titer.isEmpty() && rabies.isEmpty()){
            Toast.makeText(this, "접종 정보를 하나 이상 체크하세요.", Toast.LENGTH_SHORT).show()
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
                    "4204", "9501" -> Toast.makeText(this@EssentialVaccineActivity, "오류 발생: 관리자에게 문의하세요", Toast.LENGTH_SHORT).show()
                    "9500" -> {
                        Toast.makeText(this@EssentialVaccineActivity, "접종 정보가 성공적으로 추가되었습니다", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }

            override fun onResponseFailure(errorMsg: String?) {
                Toast.makeText(this@EssentialVaccineActivity, "실패: $errorMsg", Toast.LENGTH_SHORT).show()
            }
        }).addEssentialVaccinationRequest()



    }






}