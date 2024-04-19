package com.hsr2024.mungmungdoctortp.bnv1care

import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.data.AddorDeleteAI
import com.hsr2024.mungmungdoctortp.databinding.ActivityAiResultBinding
import com.hsr2024.mungmungdoctortp.ml.EyeModel
import com.hsr2024.mungmungdoctortp.ml.EyeModel2
import com.hsr2024.mungmungdoctortp.ml.EyeModel419
import com.hsr2024.mungmungdoctortp.ml.SkinMode2
import com.hsr2024.mungmungdoctortp.ml.SkinModel
import com.hsr2024.mungmungdoctortp.ml.SkinModel419
import com.hsr2024.mungmungdoctortp.network.RetrofitHelper
import com.hsr2024.mungmungdoctortp.network.RetrofitService
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.tensorflow.lite.support.image.TensorImage
import java.io.File
import kotlin.collections.sortedByDescending

class AiResultActivity : AppCompatActivity() {


    private val binding by lazy { ActivityAiResultBinding.inflate(layoutInflater) }
    private var receivedBundle : Bundle? = null
    private var id :String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val uriE = intent.getStringExtra("aiEyeImg")
        val uriS = intent.getStringExtra("aiSkinImg")
        val urieye = uriE?.toUri()
        val uriskin = uriS?.toUri()
        val uploadeye = intent.getStringExtra("aiEyeImg2")
        val uploadskin = intent.getStringExtra("aiSkinImg2")



        if (urieye != null) {
            binding.testImage.setImageURI(urieye)
            diagnosis_type = "eye"
            diagnostic_img_url = "$uploadeye"
            testEyeStart()
        }

        if (uriskin != null) {
            binding.testImage.setImageURI(uriskin)
            diagnosis_type = "skin"
            diagnostic_img_url = "$uploadskin"
            testSkinStart()
        }

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnResultSave.setOnClickListener { saveOnCareNote(diagnostic_img_url) }

        Log.d("fffsss", receivedBundle?.getString("type").toString())
        //리사이클러뷰 아이템 클릭했을때(아답타에서 보낸 번들)
        receivedBundle = intent?.extras
        if (receivedBundle?.getString("type") != null) {

            onClickFromAdapter()

        }//if



    }//oncreate()



    //관리수첩에서 AI기록 리사이클러 아이템 클릭했을때 이쪽으로 넘어온다.
    private fun onClickFromAdapter(){

        receivedBundle?.apply {
            if (this.getString("type") == "ai") {

                id = this.getString("id")
                val parts = this.getString("result1")!!.split(",")
                // 두 개의 변수로 나누어 저장
                val firstPart = parts[0].trim() // 앞 부분 "유루증:50%"
                val secondPart = parts[1].trim() // 뒷 부분 "안검염/각막염/결막염:19%"

                binding.result1.text = firstPart//"유루증:50%"
                binding.result2.text = secondPart//"안검염/각막염/결막염:19%"

                val p1 = firstPart.split(":")
                val p1_1= p1[0].trim() // "유루증"
                //val p1_2= p1[1].trim() // "50%"
                val p2 = secondPart.split(":")
                val p2_1 = p2[0].trim() // "안검염/각막염/결막염"
                //val p2_2 = p2[1].trim() //"19%"

                binding.result1Go.text = p1_1
                binding.result2Go.text = p2_1

                binding.result1Go.setOnClickListener { sendEyeOrSkin(p1_1) }
                binding.result2Go.setOnClickListener { sendEyeOrSkin(p2_1) }

                var imgUrl = "http://43.200.163.153/img/" + this.getString("img")
                Glide.with(this@AiResultActivity).load(imgUrl).into(binding.testImage)

                binding.btnResultSave.text = "관리수첩에서 삭제하기"
                binding.btnResultSave.setOnClickListener {
                    val builder = AlertDialog.Builder(this@AiResultActivity)
                        .setMessage("AI검사 기록을 삭제하시겠습니까?")
                        .setPositiveButton("삭제하기", DialogInterface.OnClickListener { dialog, which ->
                            deleteFromServer()
                        })
                        .setNegativeButton("취소하기", DialogInterface.OnClickListener { dialog, which ->
                             dialog.cancel()
                        })
                        builder.create().show()
                }//삭제하기버튼클릭했을때
            }//Bundle의 type이 ai로 왔을 때
        }

    }//onClickFromAdapter()



    private fun deleteFromServer(){
         //37. ai 기록 삭제하기
         //aiDeleteRequest 사용법
        val params= AddorDeleteAI(G.user_email, G.user_providerId, G.loginType, G.pet_id, // pet_id는 pet 식별값
                                id!!                                      // ai 기록 식별 값
         )
        RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val code=(response as String)             //  - 4204 서비스 회원 아님, 9300 ai 기록 삭제 성공, 9301 ai 기록 삭제 실패
                Log.d("ai delete code","$code")
                Toast.makeText(this@AiResultActivity, "AI검사 기록이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
                startActivity( Intent(this@AiResultActivity, NoteActivity::class.java) )
            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("ai delete fail",errorMsg!!) // 에러 메시지
                Toast.makeText(this@AiResultActivity, "서버 문제로 삭제 실패되었습니다. ", Toast.LENGTH_SHORT).show()
            }

        }).aiDeleteRequest()

    }//deleteFromServer()









    var diagnosis_type = ""
    var diagnostic_img_url = ""
    var diagnosis_result = ""


    private fun saveOnCareNote(image: String) {

        //이미지파일을 MutipartBody.Part 로 포장하여 전송: @Part
        val filePart: MultipartBody.Part? = image?.let { //널이 아니면...
            val file = File(it) // 생선손질..
            val requestBody: RequestBody =
                RequestBody.create("image/*".toMediaTypeOrNull(), file) // 진공팩포장
            MultipartBody.Part.createFormData("img1", file.name, requestBody) // 택배상자 포장.. == 리턴되는 값
        }

        RetrofitProcess(this, params = filePart!!, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val code = (response as String)
                Log.d("one file upload code", "$code") // 실패 시 5404, 성공 시 이미지 경로
                if (code != "5404") {
                    val params = AddorDeleteAI(
                        "${G.user_email}",
                        "${G.user_providerId}",
                        "${G.loginType}",
                        "${G.pet_id}", // pet_id는 pet 식별값
                        "",                                     // ai 기록 식별 값( 안넣어도 됨)
                        diagnosis_type,                           // 진단한 ai type (eye or skin)
                        code,                       // ai 진단한 반려견 이미지 url
                        diagnosis_result,                         // ai 진단결과 리스트(결막염 80%, 유루증 70%..)
                    )
                    RetrofitProcess(
                        this@AiResultActivity,
                        params = params,
                        callback = object : RetrofitCallback {
                            override fun onResponseListSuccess(response: List<Any>?) {}

                            override fun onResponseSuccess(response: Any?) {
                                val code =
                                    (response as String)             //  - 4204 서비스 회원 아님, 9100 ai 기록 추가 성공, 9101 ai 기록 추가 실패
                                Log.d("ai add code", "$code")
                                when (code) {
                                    "4204" -> Toast.makeText(
                                        this@AiResultActivity,
                                        "관리자에게 문의하세요",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    "9101" -> Toast.makeText(
                                        this@AiResultActivity,
                                        "관리자에게 문의하세요",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    "9100" -> {
                                        Toast.makeText(
                                            this@AiResultActivity,
                                            "기록이 완료되었습니다",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        finish()
                                        startActivity(Intent(this@AiResultActivity, NoteActivity::class.java))
                                    }
                                }

                            }

                            override fun onResponseFailure(errorMsg: String?) {
                                Log.d("ai add fail", errorMsg!!) // 에러 메시지
                            }

                        }).aiAddRequest()

                }
            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("one file upload fail", errorMsg!!) // 에러 메시지
            }

        }).onefileUploadRequest()

    }


    private fun sendEyeOrSkin(label: String) {

        val intent = Intent(this, HealthDetailActivity::class.java)
        if (label == "안검염/각막염/결막염") {
            intent.putExtra("eye1", "eye1")//안검염/각막염/결막염
        } else if (label == "안검종양") {
            intent.putExtra("eye2", "eye2")//안검종양
        }  else if (label == "유루증") {
            intent.putExtra("eye3", "eye3")//유루증
        } else if (label == "각막궤양") {
            intent.putExtra("eye4", "eye4")//각막궤양
        }else if (label == "백내장") {
            intent.putExtra("eye5", "eye5")//백내장
        }
            //-------------- 여기부터 skin-----------
         else if (label == "구진/플라크") {
            intent.putExtra("skin1", "skin1")
        } else if (label == "농포/여드름") {
            intent.putExtra("skin4", "skin4")
        } else if (label == "미란/궤양") {
            intent.putExtra("skin5", "skin5")
        } else if (label == "결절_종괴") {
            intent.putExtra("skin6", "skin6")
        } else if (label == "비듬/각질/상피성잔고리") {
            intent.putExtra("skin2", "skin2")
        } else if (label == "태선화/과다색소침착") {
            intent.putExtra("skin3", "skin3")
        }
        startActivity(intent)


    }//sendEye()

    private fun testEyeStart() {
        //1. 모델 객체생성
        val model = EyeModel419.newInstance(this)

        //2. 입력이미지 준비
        val image = TensorImage.fromBitmap(
            (binding.testImage.drawable as BitmapDrawable).bitmap
        )

        //3. 추론작업 (동기화)
        val outputs = model.process(image)

        var labelList = mutableMapOf<String, Float>()

        //4. 분류한 라벨링별 확률 출력
        for (label in outputs.probabilityAsCategoryList) {
            val translatedLabel = when (label.label) {
                "1" -> "안검염/각막염/결막염"
                "2" -> "안검종양"
                "3" -> "유루증"
                "4" -> "각막궤양"
                "5" -> "백내장"
                else -> label.label
            }
            labelList[translatedLabel] = label.score
        }

        // 점수를 기준으로 내림차순으로 정렬하고 리스트로 변환
        val sortedList = labelList.toList().sortedByDescending { (_, value) -> value }

        val resultList = mutableMapOf<String, Float>()

        // 상위 2개의 라벨
        for (i in 0 until minOf(2, sortedList.size)) {
            resultList[sortedList[i].first] = sortedList[i].second
        }

        if (resultList.size >= 1) {
            val firstLabel = resultList.keys.toList()[0]
            val firstScore = (resultList[firstLabel]!! * 100).toInt()
            val secondLabel = resultList.keys.toList()[1]
            val secondScore = (resultList[secondLabel]!! * 100).toInt()
            binding.result1.text = "$firstLabel : $firstScore%"
            binding.result2.text = "$secondLabel : $secondScore%"
            binding.result1Go.text = "$firstLabel 보러가기"
            binding.result2Go.text = "$secondLabel 보러가기"

            binding.result1Go.setOnClickListener {
                sendEyeOrSkin(firstLabel)
            }
            binding.result2Go.setOnClickListener {
                sendEyeOrSkin(secondLabel)
            }

            diagnosis_result = "$firstLabel : $firstScore%,$secondLabel : $secondScore%"
        }


        model.close()


    }// testEye


    private fun testSkinStart() {

        //1. 모델 객체생성
        val model = SkinModel419.newInstance(this)

        //2. 입력이미지 준비
        val image = TensorImage.fromBitmap(
            (binding.testImage.drawable as BitmapDrawable).bitmap
        )

        //3. 추론작업 (동기화)
        val outputs = model.process(image)

        var labelList = mutableMapOf<String, Float>()

        //4. 분류한 라벨링별 확률 출력
        for (label in outputs.probabilityAsCategoryList) {
            val translatedLabel = when (label.label) {
                "1" -> "구진/플라크"
                "2" -> "비듬/각질/상피성잔고리"
                "3" -> "태선화/과다색소침착"
                "4" -> "농포/여드름"
                "5" -> "미란/궤양"
                "6" -> "결절_종괴"
                else -> label.label
            }
            labelList[translatedLabel] = label.score
        }

        // 점수를 기준으로 내림차순으로 정렬하고 리스트로 변환
        val sortedList = labelList.toList().sortedByDescending { (_, value) -> value }

        val resultList = mutableMapOf<String, Float>()

        // 상위 2개의 라벨
        for (i in 0 until minOf(2, sortedList.size)) {
            resultList[sortedList[i].first] = sortedList[i].second
        }

        if (resultList.size >= 1) {
            val firstLabel = resultList.keys.toList()[0]
            val firstScore = (resultList[firstLabel]!! * 100).toInt()
            val secondLabel = resultList.keys.toList()[1]
            val secondScore = (resultList[secondLabel]!! * 100).toInt()
            binding.result1.text = "$firstLabel : $firstScore%"
            binding.result2.text = "$secondLabel : $secondScore%"
            binding.result1Go.text = "$firstLabel 보러가기"
            binding.result2Go.text = "$secondLabel 보러가기"

            binding.result1Go.setOnClickListener {
                sendEyeOrSkin(firstLabel)
            }
            binding.result2Go.setOnClickListener {
                sendEyeOrSkin(secondLabel)
            }

            diagnosis_result = "$firstLabel : $firstScore%,$secondLabel : $secondScore%"
        }

        model.close()


    }//testSkin

}