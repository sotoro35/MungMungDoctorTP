package com.hsr2024.mungmungdoctortp.bnv1care

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.databinding.ActivityShowHosBinding

class ShowHosActivity : AppCompatActivity() {

    private val binding by lazy { ActivityShowHosBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        selectServer()

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
            }
        })
        builder.setNegativeButton("취소", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                
            }
        })

        builder.create().show()



    }






    private fun selectServer(){
        //서버에서 데이터 가져오기
//        val retrofitService =
//            RetrofitHelper.getRetrofitInstance().create(RetrofitService::class.java)

//        binding.tvName
//        binding.tvPrice
//        binding.tvDate
//        binding.tvDiseaseName
//        binding.tvContent
//        binding.ivBill.setImageURI()
//        binding.ivCare.setImageURI()


    }//selectServer()


}//액티비티