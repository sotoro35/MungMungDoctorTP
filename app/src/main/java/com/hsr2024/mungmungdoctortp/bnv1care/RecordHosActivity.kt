package com.hsr2024.mungmungdoctortp.bnv1care

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.loader.content.CursorLoader
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialDatePicker.Builder
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.data.AddorModifyorDeleteHospital
import com.hsr2024.mungmungdoctortp.databinding.ActivityRecordHosBinding
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitHelper
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecordHosActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRecordHosBinding.inflate(layoutInflater) }

    private lateinit var name:String
    private lateinit var price:String
    private lateinit var date:String
    private lateinit var diseaseName:String
    private lateinit var content:String

    var a1 = ""
    var a2 = ""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.etDate.setOnClickListener { selectDate() }
        binding.btnSave.setOnClickListener { save() }
        binding.tvBillInsert.setOnClickListener { getBillPicture() }
        binding.tvPictureInsert.setOnClickListener { getCarePicture() }



    }//oncreate()


    private fun selectDate(){

        //달력에서 날짜선택
        val builder= MaterialDatePicker.Builder.datePicker()

        builder.setTitleText("날짜 선택")

        val picker = builder.build()
        picker.show(supportFragmentManager, picker.toString())

        picker.addOnPositiveButtonClickListener {
            val selectedDate = Date(it)
            val dateFamatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
            val formattedDate = dateFamatter.format(selectedDate)
            binding.etDate.setText(formattedDate)
        }



        //binding.etDate.text에 선택한 날짜 보이게하기
    }//selectDate()



    private fun save(){
        binding.apply {
            name = etName.text.toString()
            price = etPrice.text.toString()
            date = etDate.text.toString()
            diseaseName = etDiseaseName.text.toString()
            content = etContent.editText!!.text.toString()
        }

        if (date==""){
            Toast.makeText(this, "진료일은 필수 입력 사항입니다.", Toast.LENGTH_SHORT).show()
        }else{

            //1.서버에 새로운 이미지 업데이트 요청
            val dataPart: MutableMap<String, String> = mutableMapOf()
            dataPart["name"] = name
            dataPart["price"] = price
            dataPart["date"] = date
            dataPart["diseaseName"] = diseaseName
            dataPart["content"] = content

            //영수증사진 filePart에 담기
            val filePartBillPicture: MultipartBody.Part? = absolutePath1.let {
                val file = File(it)
                val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                MultipartBody.Part.createFormData("img1", file.name, requestBody)
            }
            //진료사진 filePart에 담기
            val filePartCarePicture: MultipartBody.Part? = absolutePath2.let {
                val file = File(it)
                val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                MultipartBody.Part.createFormData("img1", file.name, requestBody)
            }




            if (filePartBillPicture != null) {
                RetrofitProcess(this,params=filePartBillPicture, callback = object : RetrofitCallback {
                    override fun onResponseListSuccess(response: List<Any>?) {}

                    override fun onResponseSuccess(response: Any?) {
                        val code=(response as String)
                        Log.d("one file upload code","$code") // 실패 시 5404, 성공 시 이미지 경로
                        if(code != "5404"){
                            a1 = code
                        }

                    }

                    override fun onResponseFailure(errorMsg: String?) {
                        Log.d("one file upload fail",errorMsg!!) // 에러 메시지
                    }

                }).onefileUploadRequest()
            }//if





            if (filePartCarePicture != null) {
                RetrofitProcess(this,params=filePartCarePicture, callback = object : RetrofitCallback {
                    override fun onResponseListSuccess(response: List<Any>?) {}

                    override fun onResponseSuccess(response: Any?) {
                        val code=(response as String)
                        Log.d("one file upload code","$code") // 실패 시 5404, 성공 시 이미지 경로
                        if(code != "5404"){
                            a2 = code

                            if (a1==null && a2==null){
                                aaa("", "")
                            }else if (a1==null && a2!=null){
                                aaa("", a2)
                            }else if (a1!=null && a2==null){
                                aaa(a1, "")
                            }else if (a1!=null && a2!=null){
                                aaa(a1, a2)
                            }
                        }

                    }

                    override fun onResponseFailure(errorMsg: String?) {
                        Log.d("one file upload fail",errorMsg!!) // 에러 메시지
                    }

                }).onefileUploadRequest()
            }

        }//else



    }//save()








    private fun aaa(filePartBillPicture:String, filePartCarePicture:String){



                    val params= AddorModifyorDeleteHospital(G.user_email, G.user_providerId, G.loginType, G.pet_id, // pet_id는 pet 식별값
                                      "",                                             // 병원 기록 식별 값( 안넣어도 됨)
                                      name,                                           // 병원명
                                      price,                                          // 진단가격
                                      diseaseName,                                      // 진단명
                                      date,                                     // 진료일
                                      content,                                    // 진료내용
                                      filePartBillPicture,                                // 영수증 이미지 url
                                      filePartCarePicture,                               // 진료사진 이미지 url
             )
            RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
                override fun onResponseListSuccess(response: List<Any>?) {}

                override fun onResponseSuccess(response: Any?) {
                    val code=(response as String)             //  - 4204 서비스 회원 아님, 8100 병원 기록 추가 성공, 8101 병원 기록 추가 실패
                    Log.d("hospital add code","$code")

                }

                override fun onResponseFailure(errorMsg: String?) {
                    Log.d("hospital add fail",errorMsg!!) // 에러 메시지
                }

            }).hospitalAddRequest()

            //-------------------------------------------------



    }//aaa()









    //영수증사진 - 서버로보낼 절대경로
    private var absolutePath1 : String? = null

    private fun getBillPicture(): String? {
        val intent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Intent(MediaStore.ACTION_PICK_IMAGES) else
                Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*")
        resultLauncher.launch(intent)

        return absolutePath1
    }//getBillPicture()


    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val uri = it.data?.data ?: null
        if (uri != null) {
            binding.ivBill.setImageURI(uri)
            absolutePath1 = getRealPathFromUri(uri)
        }

    }


    //절대경로로 변화작업 메소드
    private fun getRealPathFromUri(uri: Uri): String? {
        val cursorLoader: CursorLoader = CursorLoader(this, uri, null, null, null, null)
        val cursor: Cursor? = cursorLoader.loadInBackground()
        val fileName: String? = cursor?.run {
            moveToFirst()
            getString(getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
        }
        val file: File = File(externalCacheDir, fileName)
        val inputStream: InputStream =
            contentResolver?.openInputStream(uri) ?: return null
        val outputStream: OutputStream = FileOutputStream(file)
        while (true) {
            val buf: ByteArray = ByteArray(1024)
            val len: Int = inputStream.read(buf)
            if (len <= 0) break

            outputStream.write(buf, 0, len)
        }
        inputStream.close()
        outputStream.close()
        return file.absolutePath

    }//getReaPathFromUri()



    //진료사진 - 서버로보낼 절대경로
    private var absolutePath2 : String? = null

    private fun getCarePicture() : String? {
        val intent =
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
                Intent(MediaStore.ACTION_PICK_IMAGES)
            }else Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*")

        resultLauncher2.launch(intent)
        return absolutePath2
    }//getCarePicture()

    val resultLauncher2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val uri = it.data?.data ?: null
        if (uri != null){
            binding.ivPicture.setImageURI(uri)
            absolutePath2 = getRealPathFromUri(uri)
        }
    }



}//class