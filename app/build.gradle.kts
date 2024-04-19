plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.hsr2024.mungmungdoctortp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hsr2024.mungmungdoctortp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        viewBinding = true
        mlModelBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    //추가 라이브러리
    implementation(libs.retrofit)
    implementation(libs.glide)
    implementation(libs.circleimageview)
    implementation(libs.converter.scalars)
    implementation(libs.converter.gson)
    
    //네이버 지도 SDK는 Google Play 서비스의 FusedLocationProviderClient와 지자기, 가속도 센서를 활용해 최적의 위치를 반환하는 구현체인 FusedLocationSource를 제공합니다.
    implementation(libs.play.services.location)


    //tensorflow
    implementation(libs.tensorflow.lite.gpu)
    implementation(libs.tensorflow.lite.metadata)
    //
    implementation(libs.tensorflow.lite.support)

    //카메라x
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.video)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.extensions)

    //크롭 라이브러리
    implementation(libs.cropme)

    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // 네이버 지도 SDK
    implementation(libs.map.sdk)

    //머티리얼 칼렌다뷰 의존성
    implementation (libs.material.calendarview)

    // 카카오 로그인
    implementation (libs.v2.user)

    // 네이버 로그인
    implementation(libs.oauth) // jdk 11




}