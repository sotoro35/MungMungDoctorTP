package com.hsr2024.mungmungdoctortp.network

interface RetrofitCallback {
    fun onResponseListSuccess(response:List<Any>? = null)
    fun onResponseSuccess(response:Any? = null)
    fun onResponseFailure(errorMsg:String?=null)
}