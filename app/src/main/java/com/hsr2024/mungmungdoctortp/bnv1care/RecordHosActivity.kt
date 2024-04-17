package com.hsr2024.mungmungdoctortp.bnv1care

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.content.CursorLoader
import com.google.android.material.datepicker.MaterialDatePicker
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.data.AddorModifyorDeleteHospital
import com.hsr2024.mungmungdoctortp.databinding.ActivityRecordHosBinding
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
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

    private lateinit var name: String
    private lateinit var price: String
    private lateinit var date: String
    private lateinit var diseaseName: String
    private lateinit var content: String


    var filePartBillPicture: MultipartBody.Part? = null
    var filePartCarePicture: MultipartBody.Part? = null

    var billUrl: String = ""
    var careUrl: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.etDate.setOnClickListener { selectDate() }
        binding.btnSave.setOnClickListener { save() }
        binding.tvBillInsert.setOnClickListener { getBillPicture() }
        binding.tvPictureInsert.setOnClickListener { getCarePicture() }


    }//oncreate()


    private fun selectDate() {

        //달력에서 날짜선택
        val builder = MaterialDatePicker.Builder.datePicker()

        builder.setTitleText("날짜 선택")

        val picker = builder.build()
        picker.show(supportFragmentManager, picker.toString())

        picker.addOnPositiveButtonClickListener {
            val selectedDate = Date(it)
            val dateFamatter = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
            val formattedDate = dateFamatter.format(selectedDate)
            binding.etDate.setText(formattedDate)
        }


        //binding.etDate.text에 선택한 날짜 보이게하기
    }//selectDate()


    private fun save() {
        binding.apply {
            name = etName.text.toString()
            price = etPrice.text.toString()
            date = etDate.text.toString()
            diseaseName = etDiseaseName.text.toString()
            content = etContent.editText!!.text.toString()
        }

        if (date == "" || date.isBlank()) {
            Toast.makeText(this, "진료일은 필수 입력 사항입니다.", Toast.LENGTH_SHORT).show()
            return
        }

        //1.서버에 새로운 이미지 업데이트 요청
        val dataPart: MutableMap<String, String> = mutableMapOf()
        dataPart["name"] = name
        dataPart["price"] = price
        dataPart["date"] = date
        dataPart["diseaseName"] = diseaseName
        dataPart["content"] = content
        Log.d("a1", date)

        //영수증절대경로가 null이 아니면 --> 영수증사진 filePart에 담기
        absolutePath1?.also {
            val file = File(it)
            val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            filePartBillPicture = MultipartBody.Part.createFormData("img1", file.name, requestBody)
            Log.d("파일파트 빌픽처 보낸당", filePartBillPicture.toString())

            readyFilePartPicture(filePartBillPicture!!) { imageUrl ->
                // 이미지 URL이 콜백을 통해 전달되면 해당 위치에서 처리할 코드 작성
                billUrl = imageUrl
                Log.d("우앙ㅇㅇㅇㅇㅇㅇ", billUrl)//202404171055531000010658.jpg

                //비동기라서..이때..진료사진 파일 처리, sendTotalToServer  해야함.


                //진료사진 filePart에 담기
                absolutePath2?.also {
                    val file = File(it)
                    val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                    filePartCarePicture =
                        MultipartBody.Part.createFormData("img1", file.name, requestBody)
                    Log.d("파일파트 케어픽처 보낸당", filePartCarePicture.toString())
                    readyFilePartPicture(filePartCarePicture!!) { imageUrl ->
                        // 이미지 URL이 콜백을 통해 전달되면 해당 위치에서 처리할 코드 작성
                        careUrl = imageUrl
                        Log.d("캐어유알엘에 잘들어왔나용", careUrl)//202404171055531000010658.jpg

                        //billUrl과 careUrl이 준비되었으니. 빈값이든 들어왔든... 이제 서버로 또 보내보자
                        Log.d("aaaaaㅇㅇaaaa", "영수증유알엘 : $billUrl , 진료사진유알엘 : $careUrl")
                        sendTotalToServer(billUrl, careUrl)

                    }//콜백함수
                }

                if (absolutePath2 == null) {
                    careUrl = ""
                    Log.d("aaaaaㅇㅇaaaaㅇㅇ", "영수증유알엘 : $billUrl , 진료사진유알엘 : $careUrl")
                    sendTotalToServer(billUrl, careUrl)
                }//진료사진 절대경로가 null이면


            }//readyFilePartPicture콜백함수
        }//absolutePath1?.also


        if (absolutePath1 == null) {
            billUrl = ""

            if (absolutePath2 == null) {
                careUrl = ""
                sendTotalToServer(billUrl, careUrl)
            }//진료사진 절대경로가 널이다.

            absolutePath2?.also {
                val file = File(it)
                val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                filePartCarePicture =
                    MultipartBody.Part.createFormData("img1", file.name, requestBody)
                Log.d("파일파트 케어픽처 보낸당", filePartCarePicture.toString())
                readyFilePartPicture(filePartCarePicture!!) { imageUrl ->
                    // 이미지 URL이 콜백을 통해 전달되면 해당 위치에서 처리할 코드 작성
                    careUrl = imageUrl
                    Log.d("캐어유알엘에 잘들어왔나용", careUrl)//202404171055531000010658.jpg

                    //billUrl과 careUrl이 준비되었으니. 빈값이든 들어왔든... 이제 서버로 또 보내보자
                    Log.d("aaaaaㅇㅇaaaa", "영수증유알엘 : $billUrl , 진료사진유알엘 : $careUrl")
                    sendTotalToServer(billUrl, careUrl)

                }//콜백함수
            }


        }//영수증절대경로가 널이면..

    }//save()


    //이미지를 파일파트에 잘 담았으면 실행하는 메소드
    private fun readyFilePartPicture(
        filePartPicture: MultipartBody.Part,
        callback: (imageUrl: String) -> Unit// 이미지 URL을 전달하기 위한 콜백
    ) {
        Log.d("파일파트빌픽처스 파라미터로 잘 받았낭", filePartPicture.toString())
        RetrofitProcess(this, params = filePartPicture, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}
            override fun onResponseSuccess(response: Any?) {
                val code = (response as String)
                Log.d("one file upload code", "$code") // 실패 시 5404, 성공 시 이미지 경로
                val imageUrl = if (code != "5404") {
                    code //이미지경로 반환 202404171040131000010659.jpg
                } else {
                    ""//실패 시 빈 문자열 반환
                }
                callback(imageUrl)

            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("one file upload fail", errorMsg!!) // 에러 메시지
                callback("")//실패시 빈 문자열 반환
            }
        }).onefileUploadRequest()

    }//파일파트 사진 준비완료


    //두개의 이미지가 서버에 한번 갔다와서 이제  imgUrl로 왔건 빈값이건.. 여튼 이제 진짜 서버에 보내기
    private fun sendTotalToServer(billUrl: String, careUrl: String) {
        Log.d("우앙ㅇㅇㅇㅇㅇㅇㅇㅇㅇ", billUrl + "\n" + careUrl)


        val params = AddorModifyorDeleteHospital(
            G.user_email, G.user_providerId, G.loginType, G.pet_id, // pet_id는 pet 식별값
            "",                                             // 병원 기록 식별 값( 안넣어도 됨)
            name,                                           // 병원명
            price,                                          // 진단가격
            diseaseName,                                      // 진단명
            date,                                     // 진료일
            content,                                    // 진료내용
            billUrl,                                // 영수증 이미지 url
            careUrl,                               // 진료사진 이미지 url
        )
        Log.d("우ddddddd", "$name, ${price},$date $billUrl, $careUrl")

        RetrofitProcess(this, params = params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val code =
                    (response as String)             //  - 4204 서비스 회원 아님, 8100 병원 기록 추가 성공, 8101 병원 기록 추가 실패
                Log.d("hospital add code", "$code")
                Toast.makeText(this@RecordHosActivity, "병원기록을 추가하셨습니다", Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("hospital add fail", errorMsg!!) // 에러 메시지
                Toast.makeText(this@RecordHosActivity, "서버에 오류가 생겼습니다.", Toast.LENGTH_SHORT).show()
            }

        }).hospitalAddRequest()

    }//sendTotalToServer()


    //영수증사진 - 서버로보낼 절대경로
    private var absolutePath1: String? = null

    private fun getBillPicture(): String? {
        val intent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Intent(MediaStore.ACTION_PICK_IMAGES) else
                Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*")
        resultLauncher.launch(intent)
        return absolutePath1
    }//getBillPicture()


    val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
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
    private var absolutePath2: String? = null

    private fun getCarePicture(): String? {
        val intent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Intent(MediaStore.ACTION_PICK_IMAGES)
            } else Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*")

        resultLauncher2.launch(intent)
        return absolutePath2
    }//getCarePicture()

    val resultLauncher2 =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val uri = it.data?.data ?: null
            if (uri != null) {
                binding.ivPicture.setImageURI(uri)
                absolutePath2 = getRealPathFromUri(uri)
            }
        }





}//class