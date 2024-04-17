package com.hsr2024.mungmungdoctortp

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "a70223c33209f3483730b5960c7232ee")
    }
}