package com.hsr2024.mungmungdoctortp.network

import com.hsr2024.mungmungdoctortp.data.AiRequest
import com.hsr2024.mungmungdoctortp.data.AiResponses
import com.hsr2024.mungmungdoctortp.data.LoginData
import com.hsr2024.mungmungdoctortp.data.LoginResponse
import com.hsr2024.mungmungdoctortp.data.NaverSearchPlaceResponse
import com.hsr2024.mungmungdoctortp.data.SignUpData
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface RetrofitService {

    // 네이버, 카카오? 키워드 검색

    //로그인
    @POST("/sign/login.php")
    fun login(@Body loginData: LoginData, @Field("login_type") login_type:String) : Call<LoginResponse> // 응답값 성공 200, 실패 201 //@Url url: String

    //회원가입(이메일)
    @POST("/sign/signup.php")
    fun singUp(@Body signUpData: SignUpData) : Call<String> // 응답값 성공 200, 실패 201

    //파일 업로드
    @Multipart
    @POST("/img/upload.php")
    fun uploadImage(@Part("file") file: MultipartBody.Part) : Call<String> // 응답값 업로드된 이미지 url, 실패 201

    //ai 정보
    @POST("/service/ai.php")
    fun aiService(@Body aiRequest:AiRequest) : Call<AiResponses>

    //@POST("/user/token_generation.php")
    //fun tokenGenrate(@Field("refresh_token") refreshToken:String, @Field("token_check_type") tokenCheckType:String) : Call<UserCheck>//액세스 토큰 요청
    //val call=retrofitService.tokenGenrate(URLEncoder.encode(refreshToken, "UTF-8"),"refresh_token")

    //@POST
    //fun serviceRequest(@Url url: String, @Body requestData: requestData) : Call<responseData>
    //val call = retrofitService.serviceRequest(serviceUrl, requestData)

    //@FormUrlEncoded
    //@POST("/sign/email_signup.php")
    //fun getSignUp(@Field("email") email:String, @Field("password") password:String, @Field("name") name:String, @Field("nickname") nickname: String) : Call<String>// POST 방식으로 전달
    //val call=retrofitService.getSignUp(email,password,name,nickName)



    //네이버- naver 지역검색api
    //클라이언트id : XP42scZcoCa__nfLeunL
    //클라이언트 sectret : xTQrwVAfl8
    @Headers("X-Naver-Client-Id: XP42scZcoCa__nfLeunL", "X-Naver-Client-Secret: xTQrwVAfl8")
    @GET("/v1/search/local.json")
    fun getNaverLocal(@Query("query") aniHospital:String, @Query("display") display:Int) : Call<NaverSearchPlaceResponse>

}