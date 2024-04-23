package com.hsr2024.mungmungdoctortp.bnv1care

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.adapter.VaccineAdapter
import com.hsr2024.mungmungdoctortp.data.AdditionVaccination
import com.hsr2024.mungmungdoctortp.data.AdditionVaccinationList
import com.hsr2024.mungmungdoctortp.data.DeleteDog
import com.hsr2024.mungmungdoctortp.databinding.ActivityVaccineBinding
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess

class VaccineActivity : AppCompatActivity() {

    private val binding by lazy { ActivityVaccineBinding.inflate(layoutInflater) }
    private lateinit var adapter: VaccineAdapter
    private var vaccineList = mutableListOf<AdditionVaccination>()
    companion object {
        private const val REQUEST_EDIT_VACCINE = 1001
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.iv.setOnClickListener { finish() }
        binding.btnAdd.setOnClickListener { startActivity(Intent(this,AddVaccineActivity::class.java)) }
        binding.vac1.setOnClickListener { toMandatoryActivity(1,"차 접종","종합백신 1차","코로나 장염 1차",false) }
        binding.vac2.setOnClickListener { toMandatoryActivity(2,"차 접종","종합백신 2차","코로나 장염 2차",false) }
        binding.vac3.setOnClickListener { toMandatoryActivity(3,"차 접종","종합백신 3차","켄넬코프 1차",false) }
        binding.vac4.setOnClickListener { toMandatoryActivity(4,"차 접종","종합백신 4차","켄넬코프 2차",false) }
        binding.vac5.setOnClickListener { toMandatoryActivity(5,"차 접종","종합백신 5차","인플루엔자 1차",false) }
        binding.vac6.setOnClickListener { toMandatoryActivity(6,"차 접종","광견병","인플루엔자 2차",true) }

        setupRecyclerView()
        fetchDataFromServer()



    }
    override fun onResume() {
        super.onResume()
        fetchDataFromServer()
    }
    private fun setupRecyclerView() {
        adapter = VaccineAdapter(this, vaccineList) { vaccination ->
            val intent = Intent(this, AddVaccineActivity::class.java).apply {
                putExtra("id", vaccination.id)
                putExtra("heartworm", vaccination.heartworm)
                putExtra("external_parasites", vaccination.external_parasites)
                putExtra("vaccine", vaccination.vaccine)
                putExtra("date", vaccination.date)
                putExtra("hospital", vaccination.hospital)
                putExtra("memo", vaccination.memo)
            }
            startActivityForResult(intent, REQUEST_EDIT_VACCINE)
        }
        binding.rvAddVaccine.layoutManager = LinearLayoutManager(this)
        binding.rvAddVaccine.adapter = adapter
    }
    private fun fetchDataFromServer() {
        val params = DeleteDog("${G.user_email}", "${G.user_providerId}", "${G.pet_id}", "email")

        RetrofitProcess(this, params = params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}
            override fun onResponseSuccess(response: Any?) {
                val data = response as AdditionVaccinationList
                when (data.code) {
                    "8401" -> Toast.makeText(this@VaccineActivity, "기록된 접종이 없습니다.", Toast.LENGTH_SHORT).show()
                    "4204" -> Toast.makeText(this@VaccineActivity, "회원 정보 확인 필요", Toast.LENGTH_SHORT).show()
                    "8400" -> {Toast.makeText(this@VaccineActivity, "추가 접종 목록 성공", Toast.LENGTH_SHORT).show()
                        val vaccines: List<AdditionVaccination> = data.vaccinationList.sortedByDescending { it.id }
                        updateVaccinationList(vaccines)
                    }
                }
            }

            override fun onResponseFailure(errorMsg: String?) {
                val errorLog = errorMsg ?: "No additional error information"
                Log.d("AdditionVaccination List fail", errorLog)
                Toast.makeText(this@VaccineActivity, "데이터 로드 실패: $errorLog", Toast.LENGTH_SHORT).show()
            }
        }).listAdditionVaccinationRequest()
    }

    private fun updateVaccinationList(vaccines: List<AdditionVaccination>) {
        adapter.updateData(vaccines)
        binding.rvAddVaccine.adapter = adapter
    }

    private fun toMandatoryActivity(shotNumber:Int, titleText: String, checkBox1Text: String, checkBox2Text: String, checkBox3Text: Boolean) {
        val intent = Intent(this, MandatoryVaccineActivity::class.java).apply {
            putExtra("shotNumber",shotNumber)
            putExtra("titleText", titleText)
            putExtra("checkBox1Text", checkBox1Text)
            putExtra("checkBox2Text", checkBox2Text)
            putExtra("checkBox3Text", checkBox3Text)
        }
        startActivity(intent)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDIT_VACCINE && resultCode == RESULT_OK) {
            fetchDataFromServer()  // 데이터를 다시 불러와 리스트를 갱신
        }
    }





}
