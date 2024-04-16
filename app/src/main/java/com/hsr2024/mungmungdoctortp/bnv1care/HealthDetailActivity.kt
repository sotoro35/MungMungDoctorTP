package com.hsr2024.mungmungdoctortp.bnv1care

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.data.Eye1
import com.hsr2024.mungmungdoctortp.data.Eye2
import com.hsr2024.mungmungdoctortp.data.Eye3
import com.hsr2024.mungmungdoctortp.data.Eye4
import com.hsr2024.mungmungdoctortp.data.Eye5
import com.hsr2024.mungmungdoctortp.data.Skin1
import com.hsr2024.mungmungdoctortp.data.Skin2
import com.hsr2024.mungmungdoctortp.data.Skin3
import com.hsr2024.mungmungdoctortp.data.Skin4
import com.hsr2024.mungmungdoctortp.data.Skin5
import com.hsr2024.mungmungdoctortp.data.Skin6
import com.hsr2024.mungmungdoctortp.databinding.ActivityHealthDetailBinding

class HealthDetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHealthDetailBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }


        if (intent.getStringExtra("eye1") == "eye1") eye1()//안검염/각막염/결막염
        if (intent.getStringExtra("eye2") == "eye2") eye2()//안검종양
        if (intent.getStringExtra("eye3") == "eye3") eye3()//유루증
        if (intent.getStringExtra("eye4") == "eye4") eye4()//각막궤양
        if (intent.getStringExtra("eye5") == "eye5") eye5()//백내장


        if (intent.getStringExtra("skin1") == "skin1") skin1()//구진/플라크
        if (intent.getStringExtra("skin2") == "skin2") skin2()//비듬각질상피성잔고리
        if (intent.getStringExtra("skin3") == "skin3") skin3()//태선화.과다색소침착
        if (intent.getStringExtra("skin4") == "skin4") skin4()//농포여드름
        if (intent.getStringExtra("skin5") == "skin5") skin5()//미란궤양
        if (intent.getStringExtra("skin6") == "skin6") skin6()//결절종괴






    }//oncreate()



    private fun eye1(){//안검염.각막염.결막염

        val eye1 = Eye1()

        binding.apply {
            tvName.setText(eye1.name)
            iv.setImageResource(eye1.image)
            tvCategory.setText(eye1.category)
            tvDefinition.setText(eye1.definition)
            tvCause.setText(eye1.cause)
            tvSymptom.setText(eye1.symptom)
            tvMethod.setText(eye1.method)
            toolbar.setTitle(eye1.name)
        }
    }


    private fun eye2(){//안검종양

        val eye2 = Eye2()

        binding.apply {
            tvName.setText(eye2.name)
            iv.setImageResource(eye2.image)
            tvCategory.setText(eye2.category)
            tvDefinition.setText(eye2.definition)
            tvCause.setText(eye2.cause)
            tvSymptom.setText(eye2.symptom)
            tvMethod.setText(eye2.method)
            toolbar.setTitle(eye2.name)
        }
    }


    private fun eye3(){//유루증

        val eye3 = Eye3()

        binding.apply {
            tvName.setText(eye3.name)
            iv.setImageResource(eye3.image)
            tvCategory.setText(eye3.category)
            tvDefinition.setText(eye3.definition)
            tvCause.setText(eye3.cause)
            tvSymptom.setText(eye3.symptom)
            tvMethod.setText(eye3.method)
            toolbar.setTitle(eye3.name)
        }
    }


    private fun eye4(){//각막궤양

        val eye4 = Eye4()

        binding.apply {
            tvName.setText(eye4.name)
            iv.setImageResource(eye4.image)
            tvCategory.setText(eye4.category)
            tvDefinition.setText(eye4.definition)
            tvCause.setText(eye4.cause)
            tvSymptom.setText(eye4.symptom)
            tvMethod.setText(eye4.method)
            toolbar.setTitle(eye4.name)
        }
    }


    private fun eye5(){//백내장

        val eye5 = Eye5()

        binding.apply {
            tvName.setText(eye5.name)
            iv.setImageResource(eye5.image)
            tvCategory.setText(eye5.category)
            tvDefinition.setText(eye5.definition)
            tvCause.setText(eye5.cause)
            tvSymptom.setText(eye5.symptom)
            tvMethod.setText(eye5.method)
            toolbar.setTitle(eye5.name)
        }
    }



















    private fun skin1(){//구진플라크
        val skin1 = Skin1()
        binding.apply {
            tvName.setText(skin1.name)
            iv.setImageResource(skin1.image)
            tvCategory.setText(skin1.category)
            tvDefinition.setText(skin1.definition)
            tvCause.setText(skin1.cause)
            tvSymptom.setText(skin1.symptom)
            tvMethod.setText(skin1.method)
            toolbar.setTitle(skin1.name)
        }
    }


    private fun skin2(){//비듬각질상피성잔고리
        val skin2 = Skin2()
        binding.apply {
            tvName.setText(skin2.name)
            iv.setImageResource(skin2.image)
            tvCategory.setText(skin2.category)
            tvDefinition.setText(skin2.definition)
            tvCause.setText(skin2.cause)
            tvSymptom.setText(skin2.symptom)
            tvMethod.setText(skin2.method)
            toolbar.setTitle(skin2.name)
        }
    }


    private fun skin3(){//태선화.과다색소침착
        val skin3 = Skin3()
        binding.apply {
            tvName.setText(skin3.name)
            iv.setImageResource(skin3.image)
            tvCategory.setText(skin3.category)
            tvDefinition.setText(skin3.definition)
            tvCause.setText(skin3.cause)
            tvSymptom.setText(skin3.symptom)
            tvMethod.setText(skin3.method)
            toolbar.setTitle(skin3.name)
        }
    }


    private fun skin4(){//농포여드름
        val skin4 = Skin4()
        binding.apply {
            tvName.setText(skin4.name)
            iv.setImageResource(skin4.image)
            tvCategory.setText(skin4.category)
            tvDefinition.setText(skin4.definition)
            tvCause.setText(skin4.cause)
            tvSymptom.setText(skin4.symptom)
            tvMethod.setText(skin4.method)
            toolbar.setTitle(skin4.name)
        }
    }


    private fun skin5(){//미란궤양
        val skin5 = Skin5()
        binding.apply {
            tvName.setText(skin5.name)
            iv.setImageResource(skin5.image)
            tvCategory.setText(skin5.category)
            tvDefinition.setText(skin5.definition)
            tvCause.setText(skin5.cause)
            tvSymptom.setText(skin5.symptom)
            tvMethod.setText(skin5.method)
            toolbar.setTitle(skin5.name)
        }
    }


    private fun skin6(){//결절종괴
        val skin6 = Skin6()
        binding.apply {
            tvName.setText(skin6.name)
            iv.setImageResource(skin6.image)
            tvCategory.setText(skin6.category)
            tvDefinition.setText(skin6.definition)
            tvCause.setText(skin6.cause)
            tvSymptom.setText(skin6.symptom)
            tvMethod.setText(skin6.method)
            toolbar.setTitle(skin6.name)
        }
    }






}//activity