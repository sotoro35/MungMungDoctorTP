package com.hsr2024.mungmungdoctortp.network

import android.content.Context
import com.hsr2024.mungmungdoctortp.data.LoginData
import com.hsr2024.mungmungdoctortp.data.LoginResponse
import com.hsr2024.mungmungdoctortp.data.SignUpData
import com.hsr2024.mungmungdoctortp.data.UserDelete
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.hsr2024.mungmungdoctortp.data.AddDog
import com.hsr2024.mungmungdoctortp.data.AddorModifyorDeleteFeed
import com.hsr2024.mungmungdoctortp.data.AddorModifyorDeleteQA
import com.hsr2024.mungmungdoctortp.data.CommentDataList
import com.hsr2024.mungmungdoctortp.data.DeleteDog
import com.hsr2024.mungmungdoctortp.data.FeedCommentList
import com.hsr2024.mungmungdoctortp.data.FeedDataList
import com.hsr2024.mungmungdoctortp.data.Individual
import com.hsr2024.mungmungdoctortp.data.ModifyDog
import com.hsr2024.mungmungdoctortp.data.PetList
import com.hsr2024.mungmungdoctortp.data.QACommentList
import com.hsr2024.mungmungdoctortp.data.QADataList
import com.hsr2024.mungmungdoctortp.data.UserChange
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part

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

    // access toeken의 경우 URL Encode 필수
    // "${URLEncoder.encode(access_token, "UTF-8")}"
    fun loginRequest() {
        val retrofitService = setRetrofitService()
        // 파라미터를 Json 형태로 변환
        val loginData=(params as LoginData)
        val call = retrofitService.login(loginData)
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
    // 1. 회원 로그인
//    loginRequest 사용법
//    val params= LoginData("이메일","패스워드","${URLEncoder.encode(액세스토큰, "UTF-8")}","naver") // 액세스토큰은 naver, kakao일 경우 작성, email의 경우 액세스토큰 빈 값
//    RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val data=(response as LoginResponse)
//        Log.d("login data","$data") // LoginResponse 데이터 출력(email, provider_id, nickname, userImgUrl, pet_id, pet_name, petImgUrl, pet_birth_date, pet_gender, pet_neutered, code)
//    }                               // code 값 4200 로그인 성공, 4203 로그인 실패 3200 간편 로그인 사용자 조회 완료, 3204 간편 로그인 사용자 조회 불가
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("login fail",errorMsg!!) // 에러 메시지
//    }
//
//  }).loginRequest()

    //회원가입
    fun signupRequest() {

        val retrofitService = setRetrofitService()
        // 파라미터를 Json 형태로 변환
        val signUpData=(params as SignUpData)
        val call = retrofitService.singUp(signUpData)
        call.enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }

            override fun onFailure(p0: Call<String>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }

        })
    }
    // 2. 회원 가입
// signupRequest 사용법
//val params= SignUpData("이메일정보","패스워드 정보","닉네임")
//RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val code=(response as String)
//        Log.d("signup code","$code") //1200 회원추가 성공, 1201 회원 추가 실패
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("signup fail",errorMsg!!) // 에러 메시지
//    }
//
//}).signupRequest()

    //중복 체크
    fun dupliCheckRequest() {
        val retrofitService = setRetrofitService()
        // 파라미터를 Json 형태로 변환
        val nickname=(params as String)
        val call = retrofitService.dupliCheck(nickname)
        call.enqueue(object : Callback<String>{
            override fun onResponse(p0: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }

            override fun onFailure(p0: Call<String>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }

        })
    }
    // 3. 닉네임 중복 체크
// dupliCheckRequest 사용법
//RetrofitProcess(this,params="중복체크할닉네임", callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val code=(response as String)
//        Log.d("nickname DupliCheck code","$code") // 4320 닉네임 중복, 4300 중복x
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("nickname DupliCheck fail",errorMsg!!) // 에러 메시지
//    }
//
//}).dupliCheckRequest()

    fun userWithDrawRequest(){
        val retrofitService = setRetrofitService()
        // 파라미터를 Json 형태로 변환
        val userDelete=(params as UserDelete)
        val call = retrofitService.withdraw(userDelete)
        call.enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }
            override fun onFailure(p0: Call<String>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }

        })
    }
    // 4. 회원 탈퇴
// userWithDrawRequest 사용법
//val params= UserDelete("이메일정보","패스워드 정보","provider_id", "로그인 타입")
//RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val code=(response as String)
//        Log.d("User withdraw code","$code") //1220 회원탈퇴 성공, 1230 회원탈퇴 실패, 4204 서비스 회원 아님, 4203 이메일 로그인 시 입력 정보 잘못되어 로그인 실패
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("User withdraw fail",errorMsg!!) // 에러 메시지
//    }
//
//}).userWithDrawRequest()

    fun onefileUploadRequest(){
//        val uri=(params as Uri)
//        val imgPath=onegetRealPathfromUri(uri)
//        val file=File(imgPath)
//        val requestBody:RequestBody=RequestBody.create(MediaType.parse("image/*"),file) //일종의 진공팩
        val retrofitService = setRetrofitService()

//        val part=MultipartBody.Part.createFormData("img1",file.name,requestBody)
        val part:MultipartBody.Part = (params as Part)
        val call = retrofitService.onefileuploadImage(part)
        call.enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }
            override fun onFailure(p0: Call<String>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }

        })
    }
    // 5. 이미지 업로드
// onefileUploadRequest 사용법
//    // val intent = if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU) Intent(MediaStore.ACTION_PICK_IMAGES) else Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/^*")
//    // resultLauncher.launch(intent)
//    // }
//    // private val resultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ imgUri ->
//    // val params= Uri("위의 imgUri 정보")
//    RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val code=(response as String)
//        Log.d("one file upload code","$code") // 실패 시 5404, 성공 시 이미지 경로
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("one file upload fail",errorMsg!!) // 에러 메시지
//    }
//
//}).onefileUploadRequest()

    fun userModifyRequest(){
        val retrofitService = setRetrofitService()
        // 파라미터를 Json 형태로 변환
        val userChange=(params as UserChange)
        val call = retrofitService.userModify(userChange)
        call.enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }
            override fun onFailure(p0: Call<String>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }

        })
    }
    // 6. 회원 정보 수정
// userModifyRequest 사용법
//val params= UserChange("이메일정보", "패스워드", "provider_id", "userModifyRequest으로 나온 url값", "로그인 타입")
//RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val code=(response as String)
//        Log.d("User modify code","$code") // 1220 회원 정보 수정 성공, 1230 회원 정보 수정 실패, 4204 서비스 회원 아님
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("User modify fail",errorMsg!!) // 에러 메시지
//    }
//
//}).userModifyRequest()

    fun petAddRequest(){
        val retrofitService = setRetrofitService()
        // 파라미터를 Json 형태로 변환
        val addDog=(params as AddDog)
        val call = retrofitService.addDog(addDog)
        call.enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }
            override fun onFailure(p0: Call<String>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }

        })
    }
    // 7. 반려견 추가
// petAddRequest 사용법
//val params= AddDog("이메일정보", "provider_id", "펫 이름", "펫 프로필", "펫 생년월일", "펫 성별", "펫 중성화 여부 TRUE or FALSE", "펫 견종", "로그인 타입")
//RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val code=(response as String)
//        Log.d("Add Pet code","$code") //  - 5200 펫 추가 성공, 5201 펫 추가 실패, 4204 서비스 회원 아님
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("Add Pet fail",errorMsg!!) // 에러 메시지
//    }
//
//}).petAddRequest()

    fun petModifyRequest(){
        val retrofitService = setRetrofitService()
        // 파라미터를 Json 형태로 변환
        val modifyDog=(params as ModifyDog)
        val call = retrofitService.modifyDog(modifyDog)
        call.enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }
            override fun onFailure(p0: Call<String>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }

        })
    }
    // 8. 반려견 정보 수정
// petModifyRequest 사용법
//val params= ModifyDog("이메일정보", "provider_id", "펫 식별값", "펫 이름", "펫 프로필", "펫 생년월일", "펫 성별", "펫 중성화 여부 TRUE or FALSE", "펫 견종", "로그인 타입")
//RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val code=(response as String)
//        Log.d("Modify Pet code","$code") //  - 5300 펫 수정 성공, 5301 펫 수정 실패, 4204 서비스 회원 아님
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("Modify Pet fail",errorMsg!!) // 에러 메시지
//    }
//
//}).petModifyRequest()

    fun petDeleteRequest(){
        val retrofitService = setRetrofitService()
        // 파라미터를 Json 형태로 변환
        val deleteDog=(params as DeleteDog)
        val call = retrofitService.deleteDog(deleteDog)
        call.enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }
            override fun onFailure(p0: Call<String>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }

        })
    }
    // 9. 반려견 삭제
// petDeleteRequest 사용법
//val params= DeleteDog("이메일정보", "provider_id", "펫 식별값", "로그인 타입")
//RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val code=(response as String)
//        Log.d("Delete Pet code","$code") //  - 5400 펫 삭제 성공, 5401 펫 삭제 실패, 4204 서비스 회원 아님
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("Delete Pet fail",errorMsg!!) // 에러 메시지
//    }
//
//}).petDeleteRequest()

    fun petListRequest(){
        val retrofitService = setRetrofitService()
        // 파라미터를 Json 형태로 변환
        val individual=(params as Individual)
        val call = retrofitService.petList(individual)
        call.enqueue(object : Callback<PetList> {
            override fun onResponse(p0: Call<PetList>, response: Response<PetList>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }
            override fun onFailure(p0: Call<PetList>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }

        })
    }
    // 10. 반려견 목록 불러오기
// petListRequest 사용법
//val params= Individual("이메일정보", "provider_id", "로그인 타입")
//RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val data=(response as PetList)
//        Log.d("pet list code","$data")
//        data.code                              // - 5500 펫 목록 성공, 5501 펫 목록 실패, 4204 서비스 회원 아님
//        data.petList.forEach{pet ->           // forEach문을 돌면서 펫 정보 가져올 수 있음
//            pet.pet_id
//            pet.pet_name
//            pet.pet_imageUrl
//            pet.pet_birthDate
//            pet.pet_gender
//            pet.pet_neutering
//            pet.pet_breed
//        }
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("pet list fail",errorMsg!!) // 에러 메시지
//    }
//
//}).petListRequest()

    fun feedListRequest(){
        val retrofitService = setRetrofitService()
        val individual=(params as Individual)
        val call = retrofitService.feedList(individual)
        call.enqueue(object : Callback<FeedDataList> {
            override fun onResponse(p0: Call<FeedDataList>, response: Response<FeedDataList>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }
            override fun onFailure(p0: Call<FeedDataList>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }
        })
    }
    // 11. feed 목록 불러오기
// feedListRequest 사용법
//val params= Individual("이메일정보", "provider_id", "로그인 타입") // 비 로그인 상태일 경우 Individual()으로 생성가능
//RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val data=(response as FeedDataList)
//        Log.d("feed list code","$data")
//        data.code                              // - 6200 feed 목록 성공, 6201 feed 목록 실패, 4204 서비스 회원 아님
//        data.feedDatas.forEach{feed ->           // forEach문을 돌면서 feed 정보 가져올 수 있음
//            feed.feed_id
//            feed.profile_imgurl
//            feed.nickname
//            feed.imgurl
//            feed.favorite
//            feed.isFavorite
//            feed.comment
//            feed.content
//            feed.create_date
//        }
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("feed list fail",errorMsg!!) // 에러 메시지
//    }
//
//}).feedListRequest()

    fun qaListRequest(){
        val retrofitService = setRetrofitService()
        val individual=(params as Individual)
        val call = retrofitService.qaList(individual)
        call.enqueue(object : Callback<QADataList> {
            override fun onResponse(p0: Call<QADataList>, response: Response<QADataList>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }
            override fun onFailure(p0: Call<QADataList>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }
        })
    }
    // 12. qa 목록 불러오기
// qaListRequest 사용법
//val params= Individual("이메일정보", "provider_id", "로그인 타입") // 비 로그인 상태일 경우 Individual()으로 생성가능
//RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val data=(response as QADataList)
//        Log.d("qa list code","$data")
//        data.code                              // - 7200 qa 목록 성공, 7201 qa 목록 실패, 4204 서비스 회원 아님
//        data.feedDatas.forEach{qa ->           // forEach문을 돌면서 qa 정보 가져올 수 있음
//            qa.qa_id
//            qa.imgurl
//            qa.title
//            qa.nickname
//            qa.view_count
//            qa.comment_count
//        }
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("qa list fail",errorMsg!!) // 에러 메시지
//    }
//
//}).qaListRequest()

    fun feedCommentListRequest(){
        val retrofitService = setRetrofitService()
        val feedCommentList=(params as FeedCommentList)
        val call = retrofitService.feedCommentList(feedCommentList)
        call.enqueue(object : Callback<CommentDataList> {
            override fun onResponse(p0: Call<CommentDataList>, response: Response<CommentDataList>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }
            override fun onFailure(p0: Call<CommentDataList>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }
        })
    }
    // 13. feed 댓글 목록 불러오기
// feedCommentListRequest 사용법
//val params= FeedCommentList(,"feed 식별 값", "이메일정보", "provider_id", "로그인 타입") // 비로그인일 경우 이메일 정보, provider_id, login_type 빈 값 가능
//RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val data=(response as CommentDataList)
//        Log.d("feed comment list  code","$data")
//        data.code                              // - 6300 feed comment 목록 성공, 6301 feed comment 목록 실패, 4204 서비스 회원 아님
//        data.commentDatas.forEach{comment ->           // forEach문을 돌면서 feed 정보 가져올 수 있음
//            comment.comment_id
//            comment.profile_imgurl
//            comment.nickname
//            comment.content
//            comment.create_date
//        }
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("feed comment list fail",errorMsg!!) // 에러 메시지
//    }
//
//}).feedCommentListRequest()

    fun qaCommentListRequest(){
        val retrofitService = setRetrofitService()
        val qaCommentList=(params as QACommentList)
        val call = retrofitService.qaCommentList(qaCommentList)
        call.enqueue(object : Callback<CommentDataList> {
            override fun onResponse(p0: Call<CommentDataList>, response: Response<CommentDataList>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }
            override fun onFailure(p0: Call<CommentDataList>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }
        })
    }
    // 14. qa 댓글 목록 불러오기
// qaCommentListRequest 사용법
//val params= QACommentList("qa 식별 값", "이메일정보", "provider_id", "로그인 타입") // 비로그인일 경우 이메일 정보, provider_id, login_type 빈 값 가능
//RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val data=(response as CommentDataList)
//        Log.d("qa comment list  code","$data")
//        data.code                              // - 7300 qa comment 목록 성공, 7301 qa comment 목록 실패, 4204 서비스 회원 아님
//        data.commentDatas.forEach{comment ->           // forEach문을 돌면서 qa 정보 가져올 수 있음
//            comment.comment_id
//            comment.profile_imgurl
//            comment.nickname
//            comment.content
//            comment.create_date
//        }
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("qa comment list fail",errorMsg!!) // 에러 메시지
//    }
//
    fun feedAddRequest(){
        val retrofitService = setRetrofitService()
        val addFeed=(params as AddorModifyorDeleteFeed)
        val call = retrofitService.feedAdd(addFeed)
        call.enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }
            override fun onFailure(p0: Call<String>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }
        })
    }
    // 15. feed 추가하기
// feedAddRequest 사용법
//val params= AddorModifyorDeleteFeed("이메일정보", "provider_id", "로그인 타입", "", "Feed에 들어갈 이미지 url", "Feed 내용") // 비로그인일 경우 이메일 정보, provider_id, login_type 빈 값 가능
//RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val code=(response as String) //  - 6400 feed 추가 성공, 6401 feed 추가 실패
//        Log.d("feed add code","$code")
//
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("feed add fail",errorMsg!!) // 에러 메시지
//    }
//
//}).feedAddRequest()

    fun qaAddRequest(){
        val retrofitService = setRetrofitService()
        val addQA=(params as AddorModifyorDeleteQA)
        val call = retrofitService.qaAdd(addQA)
        call.enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }
            override fun onFailure(p0: Call<String>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }
        })
    }
    // 16. qa 추가하기
// qaAddRequest 사용법
//val params= AddorModifyorDeleteQA("이메일정보", "provider_id", "로그인 타입", "", "qa에 들어갈 이미지 url", "qa 제목", "qa 내용") // 비로그인일 경우 이메일 정보, provider_id, login_type 빈 값 가능
//RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val code=(response as String) //  - 7400 qa 추가 성공, 7401 qa 추가 실패
//        Log.d("feed add code","$code")
//
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("feed add fail",errorMsg!!) // 에러 메시지
//    }
//
//}).qaAddRequest()

    fun feedModifyRequest(){
        val retrofitService = setRetrofitService()
        val modifyFeed=(params as AddorModifyorDeleteFeed)
        val call = retrofitService.feedModify(modifyFeed)
        call.enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }
            override fun onFailure(p0: Call<String>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }
        })
    }
    // 17. feed 수정하기
// feedModifyRequest 사용법
//val params= AddorModifyorDeleteFeed("이메일정보", "provider_id", "로그인 타입", "feed_id", "Feed에 들어갈 이미지 url", "Feed 내용") // feed_id는 feed 식별값 비로그인일 경우 이메일 정보, provider_id, login_type 빈 값 가능
//RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val code=(response as String) //  - 6500 feed 수정 성공, 6501 feed 수정 실패
//        Log.d("feed add code","$code")
//
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("feed add fail",errorMsg!!) // 에러 메시지
//    }
//
//}).feedModifyRequest()

    fun qaModifyRequest(){
        val retrofitService = setRetrofitService()
        val modifyQA=(params as AddorModifyorDeleteQA)
        val call = retrofitService.qaModify(modifyQA)
        call.enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }
            override fun onFailure(p0: Call<String>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }
        })
    }
    // 18. qa 수정하기
// qaModifyRequest 사용법
//val params= AddorModifyorDeleteQA("이메일정보", "provider_id", "로그인 타입", "qa_id", "qa에 들어갈 이미지 url", "qa 제목", "qa 내용") // qa_id는 qa 식별값 비로그인일 경우 이메일 정보, provider_id, login_type 빈 값 가능
//RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val code=(response as String) //  - 7500 qa 수정 성공, 7501 qa 수정 실패
//        Log.d("feed add code","$code")
//
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("feed add fail",errorMsg!!) // 에러 메시지
//    }
//
//}).qaModifyRequest()

    fun feedDeleteRequest(){
        val retrofitService = setRetrofitService()
        val deleteFeed=(params as AddorModifyorDeleteFeed)
        val call = retrofitService.feedDelete(deleteFeed)
        call.enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }
            override fun onFailure(p0: Call<String>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }
        })
    }
    // 19. feed 삭제하기
// feedDeleteRequest 사용법
//val params= AddorModifyorDeleteFeed("이메일정보", "provider_id", "로그인 타입", "feed_id") // 비로그인일 경우 이메일 정보, provider_id, login_type 빈 값 가능, feed_id는 feed 식별값
//RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val code=(response as String) //  - 6600 feed 삭제 성공, 6601 feed 삭제 실패
//        Log.d("feed add code","$code")
//
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("feed add fail",errorMsg!!) // 에러 메시지
//    }
//
//}).feedDeleteRequest()

    fun qaDeleteRequest(){
        val retrofitService = setRetrofitService()
        val deleteQA=(params as AddorModifyorDeleteQA)
        val call = retrofitService.qaDelete(deleteQA)
        call.enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }
            override fun onFailure(p0: Call<String>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }
        })
    }
    // 20. qa 삭제하기
// qaDeleteRequest 사용법
//val params= AddorModifyorDeleteQA("이메일정보", "provider_id", "로그인 타입", "qa_id") // 비로그인일 경우 이메일 정보, provider_id, login_type 빈 값 가능, qa_id는 qa 식별값
//RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val code=(response as String) //  - 7600 qa 삭제 성공, 7601 qa 삭제 실패
//        Log.d("feed add code","$code")
//
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("feed add fail",errorMsg!!) // 에러 메시지
//    }
//
//}).qaDeleteRequest()

    fun userLoadRequest(){
        val retrofitService = setRetrofitService()
        val individual=(params as Individual)
        val call = retrofitService.userLoad(individual)
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
    // 21. 단일 회원정보 로드하기
//    userLoadRequest 사용법
//val params= Individual("이메일정보", "provider_id", "로그인 타입")
//    RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val data=(response as LoginResponse)
//        Log.d("login data","$data") // LoginResponse 데이터 출력(email, provider_id, nickname, userImgUrl, pet_id, pet_name, petImgUrl, pet_birth_date, pet_gender, pet_neutered, code)
//    }                               // code 값 4200 로그인 성공, 4203 로그인 실패 3200 간편 로그인 사용자 조회 완료, 3204 간편 로그인 사용자 조회 불가
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("login fail",errorMsg!!) // 에러 메시지
//    }
//
//  }).userLoadRequest()

//    private fun onegetRealPathfromUri(uri:Uri) : String? {
//        //android 10 버전 부터는 Uri를 통해 파일의 실제 경로를 얻을 수 있는 방법이 없어졌음
//        //그래서 uri에 해당하는 파일을 복사하여 임시로 파일을 만들고 그 파일의 경로를 이용하여 서버에 전송
//
//        //Uri[미디어저장소의 DB 주소]로부터 파일의 이름을 얻어오기 - DB SELECT 쿼리 작업을 해주는 기능을 가진 객체를 이용
//        // CursorLoader : Content, uri객체, 컬럼 위치, 조건(where), 조건에 해당되는 값, 오름 or 내림차순
//        val cursorLoader=CursorLoader(context, uri, null, null, null, null)
//        //비동기로 로딩해라
//        val cursor:Cursor?=cursorLoader.loadInBackground()
//        val fileName:String?=cursor?.run {
//            moveToFirst()
//            // 컬럼 위치 array MediaStore.Images.Media 중 이미지 이름을 담고 있는 멤버변수 DISPLAY_NAME
//            getString(getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
//        }
//        //복사본이 저장될 파일의 경로와 파일명.확장자
//        //externalCacheDir : /stroage/emulated/0/Android/data/패키지이름/cache/
//        //fileName : 1000000076.jpg
//        val file=File((context as MainActivity).externalCacheDir,fileName)
//        //AlertDialog.Builder(this).setMessage(file.absolutePath).create().show()
//
//        //이제 파일 복사 작업 수행(로드된 파일 경로(uri)에서 파일을 가져와(inputStream) 파일을 실제 경로에 저장(outputStream)
//        // contentResolver 컨텐츠 주소 해독
//        val inputStream:InputStream=(context as MainActivity).contentResolver.openInputStream(uri) ?: return null
//        val outputStream:OutputStream=FileOutputStream(file)
//
//        while(true){
//            val buf:ByteArray= ByteArray(1024) // 빈 바이트 배열 [1KB]
//            val len:Int=inputStream.read(buf) //스트림을 통해 읽어들인 바이트들을 buf 배열에 넣기 -- 읽어들인 데이터의 크기를 리턴
//            if(len <= 0) break //읽어들인 데이터 크기가 0일 경우 break
//            outputStream.write(buf, 0, len) //buf 배열에 있는 바이트들을 file경로로 던지기. 첫시작(0) ~ 1023번, 다음 첫시작(1024) ~ 1027번 ..
//        } // 반복문이 끝났으면 복사가 완료
//        inputStream.close()
//        outputStream.close()
//        AlertDialog.Builder(context).setMessage(file.absolutePath).create().show()
//        return file.absolutePath
//    }


} // Class
