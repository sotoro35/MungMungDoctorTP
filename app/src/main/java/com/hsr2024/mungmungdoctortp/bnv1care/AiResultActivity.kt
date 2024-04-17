package com.hsr2024.mungmungdoctortp.bnv1care

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.data.AddorDeleteAI
import com.hsr2024.mungmungdoctortp.databinding.ActivityAiResultBinding
import com.hsr2024.mungmungdoctortp.ml.EyeModel
import com.hsr2024.mungmungdoctortp.ml.EyeModel2
import com.hsr2024.mungmungdoctortp.ml.SkinMode2
import com.hsr2024.mungmungdoctortp.ml.SkinModel
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess
import org.tensorflow.lite.support.image.TensorImage
import kotlin.collections.sortedByDescending

class AiResultActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAiResultBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val uriE = intent.getStringExtra("aiEyeImg")
        val uriS = intent.getStringExtra("aiSkinImg")
        val uriskin = uriS?.toUri()
        val urieye = uriE?.toUri()

        if (urieye != null){
            binding.testImage.setImageURI(urieye)
            diagnosis_type = "eye"
            diagnostic_img_url = "$urieye"
            testEyeStart()
        }

        if (uriskin != null){
            binding.testImage.setImageURI(uriskin)
            diagnosis_type = "skin"
            diagnostic_img_url = "$uriskin"
            testSkinStart()
        }


        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnResultSave.setOnClickListener { saveOnCareNote() }


    }

    var diagnosis_type = ""
    var diagnostic_img_url = ""
    var diagnosis_result = ""


    private fun saveOnCareNote(){

        val params= AddorDeleteAI("${G.user_email}", "${G.user_providerId}", "${G.loginType}", "${G.pet_id}", // pet_id는 pet 식별값
                                  "",                                     // ai 기록 식별 값( 안넣어도 됨)
                                  diagnosis_type,                           // 진단한 ai type (eye or skin)
                                  diagnostic_img_url,                       // ai 진단한 반려견 이미지 url
                                  diagnosis_result,                         // ai 진단결과 리스트(결막염 80%, 유루증 70%..)
         )
        RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val code=(response as String)             //  - 4204 서비스 회원 아님, 9100 ai 기록 추가 성공, 9101 ai 기록 추가 실패
                Log.d("ai add code","$code")

            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("ai add fail",errorMsg!!) // 에러 메시지
            }

        }).aiAddRequest()
    }



    private fun testEyeStart(){
        //1. 모델 객체생성
        val model = EyeModel2.newInstance(this)

        //2. 입력이미지 준비
        val image = TensorImage.fromBitmap(
            (binding.testImage.drawable as BitmapDrawable).bitmap
        )

        //3. 추론작업 (동기화)
        val outputs = model.process(image)

        var labelList = mutableMapOf<String,Float>()

        //4. 분류한 라벨링별 확률 출력
        for ( label in outputs.probabilityAsCategoryList ){
            val translatedLabel = when(label.label){
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
        val sortedList = labelList.toList().sortedByDescending{ (_, value) -> value }

        val resultList = mutableMapOf<String,Float>()

        // 상위 2개의 라벨
        for ( i in 0 until minOf(2, sortedList.size)){
            resultList[sortedList[i].first] = sortedList[i].second
        }

        if (resultList.size >= 1 ){
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



    private fun sendEyeOrSkin(label : String){

        val intent = Intent(this, HealthDetailActivity::class.java)
        if (label =="결막염"){
            intent.putExtra("eye1", "eye1")//안검염/각막염/결막염
        }else if (label =="백내장"){
            intent.putExtra("eye5", "eye5")//백내장
        }else if (label =="안검내반증"){
            intent.putExtra("eye1", "eye1")//?
        }else if (label =="안검종양"){
            intent.putExtra("eye2", "eye2")//안검종양
        }else if (label =="유루증"){
            intent.putExtra("eye3", "eye3")//유루증
        }else if (label =="핵경화"){
            intent.putExtra("eye1", "eye1")//?
        }else if (label =="궤양성각막질환"){
            intent.putExtra("eye4", "eye4")//각막궤양
        }else if (label =="비궤양성각막질환"){
            intent.putExtra("eye1", "eye1")//?
        }else if (label =="색소침착성각막염"){
            intent.putExtra("eye1", "eye1")//?
            //-------------- 여기부터 skin-----------
        }else if (label == "구진/플라크"){
            intent.putExtra("skin1", "skin1")
        }else if (label == "농포/여드름"){
            intent.putExtra("skin4", "skin4")
        }else if (label == "미란/궤양"){
            intent.putExtra("skin5", "skin5")
        }else if (label == "결절_종괴"){
            intent.putExtra("skin6", "skin6")
        }else if (label == "비듬/각질/상피성잔고리"){
            intent.putExtra("skin2", "skin2")
        }else if (label == "태선화/과다색소침착"){
            intent.putExtra("skin3", "skin3")
        }

        startActivity(intent)

    }//sendEye()

    private fun testSkinStart(){

        //1. 모델 객체생성
        val model = SkinMode2.newInstance(this)

        //2. 입력이미지 준비
        val image = TensorImage.fromBitmap(
            (binding.testImage.drawable as BitmapDrawable).bitmap
        )

        //3. 추론작업 (동기화)
        val outputs = model.process(image)

        var labelList = mutableMapOf<String,Float>()

        //4. 분류한 라벨링별 확률 출력
        for ( label in outputs.probabilityAsCategoryList ){
            val translatedLabel = when(label.label){
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
        val sortedList = labelList.toList().sortedByDescending{ (_, value) -> value }

        val resultList = mutableMapOf<String,Float>()

        // 상위 2개의 라벨
        for ( i in 0 until minOf(2, sortedList.size)){
            resultList[sortedList[i].first] = sortedList[i].second
        }

        if (resultList.size >= 1 ){
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