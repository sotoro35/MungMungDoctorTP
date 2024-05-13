package com.hsr2024.mungmungdoctortp.bnv1care

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.adapter.EssentialVaccinationAdapter
import com.hsr2024.mungmungdoctortp.adapter.VaccineAdapter
import com.hsr2024.mungmungdoctortp.data.AdditionVaccination
import com.hsr2024.mungmungdoctortp.data.AdditionVaccinationList
import com.hsr2024.mungmungdoctortp.data.DeleteDog
import com.hsr2024.mungmungdoctortp.data.EssentialVaccination
import com.hsr2024.mungmungdoctortp.data.EssentialVaccinationList
import com.hsr2024.mungmungdoctortp.databinding.ActivityVaccineBinding
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess

class VaccineActivity : AppCompatActivity() {

    private val binding by lazy { ActivityVaccineBinding.inflate(layoutInflater) }
    private lateinit var adapter: VaccineAdapter
    private lateinit var essentialAdapter: EssentialVaccinationAdapter
    private var vaccineList = mutableListOf<AdditionVaccination>()
    companion object {
        private const val REQUEST_EDIT_VACCINE = 1001
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        binding.iv.setOnClickListener { finish() }
        binding.btnAdd.setOnClickListener { startActivity(Intent(this,AddVaccineActivity::class.java)) }
        binding.addVc.setOnClickListener { startActivity(Intent(this,EssentialVaccineActivity::class.java)) }

        setupRecyclerView()
        essentialRecyclerView()
        fetchDataFromServer()
        essentialDataFromServer()

    }
    override fun onResume() {
        super.onResume()
        fetchDataFromServer()
        essentialDataFromServer()
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
    private fun essentialRecyclerView() {

        val esRecyclerManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.essentialVaccine.layoutManager = esRecyclerManager

        // EssentialVaccination의 빈 리스트를 명시적으로 타입과 함께 전달
        essentialAdapter = EssentialVaccinationAdapter(this,emptyList()) { essentialVaccination ->
            val intent = Intent(this,MandatoryVaccineActivity::class.java).apply {
                putExtra("id",essentialVaccination.id)
                putExtra("shot_number",essentialVaccination.shot_number.filter { it.isDigit() })
                putExtra("comprehensive",essentialVaccination.comprehensive)
                putExtra("corona_enteritis",essentialVaccination.corona_enteritis)
                putExtra("kennel_cough",essentialVaccination.kennel_cough )
                putExtra("influenza",essentialVaccination.influenza  )
                putExtra("antibody_titer",essentialVaccination.antibody_titer )
                putExtra("rabies",essentialVaccination.rabies )
                putExtra("date",essentialVaccination.date )
                putExtra("hospital",essentialVaccination.hospital )
                putExtra("memo",essentialVaccination.memo)
            }
            startActivityForResult(intent, REQUEST_EDIT_VACCINE)
        }
        binding.essentialVaccine.adapter = essentialAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDIT_VACCINE && resultCode == RESULT_OK) {
            fetchDataFromServer()  // 데이터를 다시 불러와 리스트를 갱신
            binding.rvAddVaccine.adapter?.notifyDataSetChanged()
        }
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
        binding.rvAddVaccine.scrollToPosition(0)
    }

    private fun essentialDataFromServer(){
        val params= DeleteDog("${G.user_email}", "${G.user_providerId}", "${G.pet_id}", "email")
        RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val data=(response as EssentialVaccinationList)
                when (data.code) {
                    "9401" -> Toast.makeText(this@VaccineActivity, "기록된 접종이 없습니다.", Toast.LENGTH_SHORT).show()
                    "4204" -> Toast.makeText(this@VaccineActivity, "회원 정보 확인 필요", Toast.LENGTH_SHORT).show()
                    "9400" -> {Toast.makeText(this@VaccineActivity, "필수 접종 목록 성공", Toast.LENGTH_SHORT).show()
                        val vaccines: List<EssentialVaccination> = data.vaccinationList.sortedByDescending { it.id }
                        updateEssentialVaccinationList(vaccines)
                    }
                }
            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("EssentialVaccination List fail",errorMsg!!) // 에러 메시지
            }

        }).listEssentialVaccinationRequest()
    }
    private fun updateEssentialVaccinationList(vaccines: List<EssentialVaccination>) {
        essentialAdapter.updateData(vaccines)
        binding.essentialVaccine.adapter = essentialAdapter
        binding.essentialVaccine.scrollToPosition(0)

    }









}
