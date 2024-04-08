package com.hsr2024.mungmungdoctortp.network

import android.content.Context
import com.hsr2024.mungmungdoctortp.data.LoginData
import com.hsr2024.mungmungdoctortp.data.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitProcess(
    val context: Context,
    val serviceUrl: String = "https://43.200.163.153",
    val params: Any,
    private val callback: RetrofitCallback? = null
)
{
    private fun setRetrofitService() : RetrofitService {    // retrofit 서비스 객체  생성
        val retrofit= RetrofitHelper.getunsafeRetrofitInstance(serviceUrl)
        val retrofitService=retrofit.create(RetrofitService::class.java)
        return retrofitService
    }

    fun loginRequest(loginData:LoginData, login_type:String) {
        val retrofitService = setRetrofitService()
        // 파라미터를 Json 형태로 변환
        val call = retrofitService.login(loginData,login_type)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(p0: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }

            override fun onFailure(p0: Call<LoginResponse>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }

        })
    }
}

// 사용법
// RetrofitProcess(this, params="JSON 데이터", callback=object RetrofitCallback{

// }