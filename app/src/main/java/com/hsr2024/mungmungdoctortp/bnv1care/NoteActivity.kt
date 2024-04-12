package com.hsr2024.mungmungdoctortp.bnv1care

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.adapter.AIRecordAdapter
import com.hsr2024.mungmungdoctortp.adapter.HospitalRecordAdapter
import com.hsr2024.mungmungdoctortp.data.AIRecordData
import com.hsr2024.mungmungdoctortp.data.HospitalRecordData
import com.hsr2024.mungmungdoctortp.databinding.ActivityNoteBinding

class NoteActivity : AppCompatActivity() {

    private val binding by lazy { ActivityNoteBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            Toast.makeText(this, "$year, ${month + 1}, $dayOfMonth", Toast.LENGTH_SHORT).show()
        }

        val itemlist:MutableList<HospitalRecordData> = mutableListOf()
        val item1 = HospitalRecordData("제1병원", "목아파", "10만원", "오늘당장")
        val item2 = HospitalRecordData("제2병원", "어깨아파", "10만원", "오늘당장")
        val item3 = HospitalRecordData("제3병원", "댄나아팤ㅋ", "10만원", "오늘당장")
        itemlist.add(0, item1)
        itemlist.add(1, item2)
        itemlist.add(2,item3)
        binding.recyclerViewHospital.adapter = HospitalRecordAdapter(this, itemlist)


        val aiList:MutableList<AIRecordData> = mutableListOf()
        val ai1 = AIRecordData("안구", "눈충혈80푸로", "눈튀나옴80프로", "2024/03/24")
        val ai2 = AIRecordData("피부", "푸석거림30프루", "기미79프로", "2024/03/24")
        val ai3 = AIRecordData("안구", "눈충혈80푸로", "눈튀나옴80프로", "2024/03/24")
        aiList.add(ai1)
        aiList.add(ai2)
        aiList.add(ai3)

        binding.recyclerViewAI.adapter = AIRecordAdapter(this, aiList)

        binding.tvRecordPlus.setOnClickListener {
            //기록하기 액티비티로 이동
            val intent = Intent(this, HealthDetailActivity::class.java)
            startActivity(intent)
        }





    }//oncreate









}//activity