package com.hsr2024.mungmungdoctortp.bnv2map

import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.databinding.ActivityMapDetailBinding

class MapDetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMapDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        //인텐트가 가져온 json형태를 다시 객체로..
        val url: String? = intent.getStringExtra("url")

        //웹뷰 사용할 때 반드시 해야할 3가지 설정
        binding.webView.webViewClient = WebViewClient()//웹문서를 열리도록 하는 설정
        binding.webView.webChromeClient = WebChromeClient()//다이얼로그를 보여주는 경우도 있을 수있으니..
        //그리고 팝업같은 것들이 있을 수 있으니 발동하도록 하는 설정
        binding.webView.settings.javaScriptEnabled = true
        //웹뷰는 기본적으로 보안때문에 js 막아놨다. 그걸 허용하도록..
        if (url != null) {
            binding.webView.loadUrl(url)
        }

    }
}