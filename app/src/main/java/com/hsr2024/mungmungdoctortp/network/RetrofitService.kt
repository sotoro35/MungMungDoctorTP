package com.hsr2024.mungmungdoctortp.network

import com.hsr2024.mungmungdoctortp.data.AiRequest
import com.hsr2024.mungmungdoctortp.data.AiResponses
import com.hsr2024.mungmungdoctortp.data.KakaoSearchPlaceResponse
import com.hsr2024.mungmungdoctortp.data.LoginData
import com.hsr2024.mungmungdoctortp.data.LoginResponse
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
    fun login(@Body loginData: LoginData) : Call<LoginResponse> // 4200 회원, 4204 회원 아님,
    // 4203 이메일 로그인 정보 틀림, 1200 간편 회원 추가 성공, 1201 간편회원 추가 실패

    //회원가입(이메일)
    @POST("/sign/signup.php")
    fun singUp(@Body signUpData: SignUpData) : Call<String> // 1200 회원 추가 성공, 1201 회원 추가 실패, 4330 닉네임 또는 이메일 중복

    //회원가입 시 닉네임 중복체크
    @GET("/sign/email_nickname_dupli_check.php")
    fun dupliCheck(@Query("nickname") nickname:String) : Call<String> // 4320 닉네임 중복, 4300 중복 x


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

    //카카오 로컬 검색API를 해줘~ 명세서쓰기 -- 우선 응답type을 스트링으로
    @Headers("Authorization: KakaoAK 1339b7346295d6208b37fac2e7091de1") //REST API 키: 963ec3326effb762f45c440734baacb6
    @GET("/v2/local/search/keyword.json?sort=distance")
    fun searchPlace(@Query("query")query:String, @Query("x") longitude:String, @Query("y")latitude:String) : Call<KakaoSearchPlaceResponse>

}