package com.hsr2024.mungmungdoctortp.login

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.databinding.ActivityPersonRuleBinding

class PersonRuleActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPersonRuleBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.personRuleWebview.webViewClient = WebViewClient()
        binding.personRuleWebview.webChromeClient = WebChromeClient()
        binding.personRuleWebview.settings.javaScriptEnabled = true

        binding.personRuleWebview.loadUrl("https://reliable-sphere-786.notion.site/f1338bf46e36453986e46b970256645f?pvs=4")
    }
}