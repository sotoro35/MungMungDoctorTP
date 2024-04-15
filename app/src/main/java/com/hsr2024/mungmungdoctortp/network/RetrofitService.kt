package com.hsr2024.mungmungdoctortp.network

import com.hsr2024.mungmungdoctortp.data.AiRequest
import com.hsr2024.mungmungdoctortp.data.AiResponses
import com.hsr2024.mungmungdoctortp.data.FeedDataList
import com.hsr2024.mungmungdoctortp.data.KakaoSearchPlaceResponse
import com.hsr2024.mungmungdoctortp.data.LoginData
import com.hsr2024.mungmungdoctortp.data.LoginResponse
import com.hsr2024.mungmungdoctortp.data.PetList
import com.hsr2024.mungmungdoctortp.data.QADataList
import com.hsr2024.mungmungdoctortp.data.SignUpData
import com.hsr2024.mungmungdoctortp.data.UserChange
import com.hsr2024.mungmungdoctortp.data.UserDelete
import com.hsr2024.mungmungdoctortp.data.AddDog
import com.hsr2024.mungmungdoctortp.data.AddorModifyorDeleteComment
import com.hsr2024.mungmungdoctortp.data.AddorModifyorDeleteFeed
import com.hsr2024.mungmungdoctortp.data.AddorModifyorDeleteQA
import com.hsr2024.mungmungdoctortp.data.CommentDataList
import com.hsr2024.mungmungdoctortp.data.DeleteDog
import com.hsr2024.mungmungdoctortp.data.FeedCommentList
import com.hsr2024.mungmungdoctortp.data.FeedFavor
import com.hsr2024.mungmungdoctortp.data.Individual
import com.hsr2024.mungmungdoctortp.data.ModifyDog
import com.hsr2024.mungmungdoctortp.data.Pet
import com.hsr2024.mungmungdoctortp.data.QACommentList
import com.hsr2024.mungmungdoctortp.data.QAView
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
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
    @POST("/upload/onefileupload.php")
    fun onefileuploadImage(@Part file: MultipartBody.Part) : Call<String> // 응답값 업로드된 이미지 url, 실패 5404, 성공시 이미지 url

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

    // feed list 불러오기
    @POST("/feed/feed_list.php")
    fun feedList(@Body individual:Individual) : Call<FeedDataList>

    // qa list 불러오기
    @POST("/qa/qa_list.php")
    fun qaList(@Body individual:Individual) : Call<QADataList>

    // feed comment list 불러오기
    @POST("/feed/comment_list.php")
    fun feedCommentList(@Body feedCommentList:FeedCommentList) : Call<CommentDataList>

    // qa comment list 불러오기
    @POST("/qa/comment_list.php")
    fun qaCommentList(@Body qaCommentList:QACommentList) : Call<CommentDataList>

    // feed 추가
    @POST("/feed/feed_add.php")
    fun feedAdd(@Body addFeed:AddorModifyorDeleteFeed) : Call<String>

    // qa 추가
    @POST("/qa/qa_add.php")
    fun qaAdd(@Body addQA:AddorModifyorDeleteQA) : Call<String>

    // feed 수정
    @POST("/feed/feed_modify.php")
    fun feedModify(@Body modifyFeed:AddorModifyorDeleteFeed) : Call<String>

    // qa 수정
    @POST("/qa/qa_modify.php")
    fun qaModify(@Body modifyQA:AddorModifyorDeleteQA) : Call<String>

    // feed 삭제
    @POST("/feed/feed_delete.php")
    fun feedDelete(@Body deleteFeed:AddorModifyorDeleteFeed) : Call<String>

    // qa 삭제
    @POST("/qa/qa_delete.php")
    fun qaDelete(@Body deleteQA:AddorModifyorDeleteQA) : Call<String>

    // feed 댓글 수정하기
    @POST("/feed/comment_modify.php")
    fun feedCommentModify(@Body modifyComment:AddorModifyorDeleteComment) : Call<String>

    // qa 댓글 수정하기
    @POST("/qa/comment_modify.php")
    fun qaCommentModify(@Body modifyComment:AddorModifyorDeleteComment) : Call<String>

    // feed 댓글 삭제하기
    @POST("/feed/comment_delete.php")
    fun feedCommentDelete(@Body deleteComment:AddorModifyorDeleteComment) : Call<String>

    // feed 찜 기능
    @POST("/feed/favor.php")
    fun feedFavor(@Body feedFavor:FeedFavor) : Call<String>

    // qa 조회 수 기능
    @POST("/qa/view.php")
    fun qaView(@Body qaView:QAView) : Call<String>

    // qa 댓글 삭제하기
    @POST("/qa/comment_delete.php")
    fun qaCommentDelete(@Body deleteComment:AddorModifyorDeleteComment) : Call<String>

    // 회원탈퇴
    @POST("/user/withdraw.php")
    fun withdraw(@Body userDelete: UserDelete) : Call<String>

    // 회원 정보 변경
    @POST("/user/modify.php")
    fun userModify(@Body userChange:UserChange) : Call<String>

    // 회원 1개 불러오기
    @POST("/user/load.php")
    fun userLoad(@Body userload:Individual) : Call<LoginResponse>

    //반려견 선택
    @POST("/user/pet_select.php")
    fun selectDog(@Body selectDog: DeleteDog) : Call<LoginResponse>

    //반려견 추가
    @POST("/pet/add.php")
    fun addDog(@Body addDog: AddDog) : Call<String>

    //반려견 수정
    @POST("/pet/modify.php")
    fun modifyDog(@Body modifyDog: ModifyDog) : Call<String>

    //반려견 삭제
    @POST("/pet/delete.php")
    fun deleteDog(@Body deleteDog: DeleteDog) : Call<String>

    //반려견 정보 불러오기
    @POST("/pet/list.php")
    fun petList(@Body individual:Individual) : Call<PetList>
}