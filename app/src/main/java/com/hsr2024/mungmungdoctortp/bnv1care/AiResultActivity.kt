package com.hsr2024.mungmungdoctortp.bnv1care

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.hsr2024.mungmungdoctortp.databinding.ActivityAiResultBinding
import com.hsr2024.mungmungdoctortp.ml.EyeModel
import com.hsr2024.mungmungdoctortp.ml.SkinModel
import com.hsr2024.mungmungdoctortp.network.RetrofitHelper
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess
import com.hsr2024.mungmungdoctortp.network.RetrofitService
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
            testEyeStart()
        }

        if (uriskin != null){
            binding.testImage.setImageURI(uriskin)
            testSkinStart()
        }


        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnResultSave.setOnClickListener { saveOnCareNote() }


    }


    private fun saveOnCareNote(){
        //서버 Insert
    }









    private fun testEyeStart(){

        //1. 모델 객체생성
        val model = EyeModel.newInstance(this)

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
                "conjunctivitis" -> "결막염"
                "cataract" -> "백내장"
                "entropion" -> "안검내반증"
                "blepharitis" -> "안검종양"
                "galactorrhea" -> "유루증"
                "nuclear_hardening" -> "핵경화"
                "ulcerative_corneal_disease" -> "궤양성각막질환"
                "non-ulcerative_corneal_disease" -> "비궤양성각막질환"
                "pigmented_keratitis" -> "색소침착성각막염"
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
        val model = SkinModel.newInstance(this)

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
                "Papule_Plaque" -> "구진/플라크"
                "Pustule_Acne" -> "농포/여드름"
                "erosion_ulcer" -> "미란/궤양"
                "nodule_mass" -> "결절_종괴"
                "dandruff_corneous_Epithelial_ring" -> "비듬/각질/상피성잔고리"
                "Lichenification_Hyperpigmentation" -> "태선화/과다색소침착"
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



        }

        model.close()


    }//testSkin

}