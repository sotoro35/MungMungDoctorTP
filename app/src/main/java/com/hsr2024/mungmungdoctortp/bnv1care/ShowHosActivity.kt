package com.hsr2024.mungmungdoctortp.bnv1care

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.data.AddorModifyorDeleteHospital
import com.hsr2024.mungmungdoctortp.databinding.ActivityShowHosBinding
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess

class ShowHosActivity : AppCompatActivity() {

    private val binding by lazy { ActivityShowHosBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setting()

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.toolbar.setOnMenuItemClickListener {
            deleteHosRecord()
            true
        }
    }//온크리





    private fun deleteHosRecord(){

        val builder = AlertDialog.Builder(this)
        builder.setMessage("해당 병원기록을 삭제하시겠습니까?")
        builder.setPositiveButton("삭제",object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                Toast.makeText(this@ShowHosActivity, "삭제하기", Toast.LENGTH_SHORT).show()
                //서버에서 데이터 지우기.delete
                deleteFromServer()
            }
        })
        builder.setNegativeButton("취소", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                
            }
        })

        builder.create().show()



    }
    
    
    private fun deleteFromServer(){
        val receivedBundle = intent.extras
        val id = receivedBundle?.getString("id") ?: ""

        //hospitalDeleteRequest 사용법
        val params= AddorModifyorDeleteHospital(G.user_email, G.user_providerId, G.loginType, G.pet_id, // pet_id는 pet 식별값
            id// 병원 기록 식별 값
         )
        RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val code=(response as String)             //  - 4204 서비스 회원 아님, 8300 병원 기록 삭제 성공, 8301 병원 기록 삭제 실패
                Log.d("hospital delete codeaa","$code")
                Toast.makeText(this@ShowHosActivity, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ShowHosActivity, NoteActivity::class.java)
                startActivity(intent)

            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("hospital delete fail",errorMsg!!) // 에러 메시지
                Toast.makeText(this@ShowHosActivity, "서버문제로 삭제 실패했습니다.", Toast.LENGTH_SHORT).show()
            }

        }).hospitalDeleteRequest()

    }//deleteFromServer()





    private fun setting(){

        val receivedBundle = intent.extras
        receivedBundle?.apply {
            if (this.getString("type") == "hospital"){
                binding.tvName.text = this.getString("name")
                binding.tvPrice.text = this.getString("price")
                binding.tvDate.text = this.getString("date")
                binding.tvDiseaseName.text = this.getString("disease_name")
                binding.tvContent.text = this.getString("content")
                val img1 = "http://43.200.163.153/img/"+this.getString("bill_img")
                Glide.with(this@ShowHosActivity).load(img1).into(binding.ivBill)
                val img2 = "http://43.200.163.153/img/"+this.getString("clinic_img")
                Glide.with(this@ShowHosActivity).load(img2).into(binding.ivCare)
                

            }

        }



    }//selectServer()


}//액티비티