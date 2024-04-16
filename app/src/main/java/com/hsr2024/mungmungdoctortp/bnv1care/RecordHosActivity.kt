package com.hsr2024.mungmungdoctortp.bnv1care

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.loader.content.CursorLoader
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialDatePicker.Builder
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.databinding.ActivityRecordHosBinding
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
//        val retrofitService =
//            RetrofitHelper.getRetrofitInstance().create(RetrofitService::class.java)
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

        }//else




    }//save()





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