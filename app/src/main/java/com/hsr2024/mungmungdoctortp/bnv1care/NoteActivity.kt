package com.hsr2024.mungmungdoctortp.bnv1care

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.adapter.AIRecordAdapter
import com.hsr2024.mungmungdoctortp.adapter.HospitalRecordAdapter
import com.hsr2024.mungmungdoctortp.data.AIRecordData
import com.hsr2024.mungmungdoctortp.data.AIRecordList
import com.hsr2024.mungmungdoctortp.data.HospitalRecordData
import com.hsr2024.mungmungdoctortp.data.HospitalRecordList
import com.hsr2024.mungmungdoctortp.data.HospitalorAiRecordList
import com.hsr2024.mungmungdoctortp.databinding.ActivityNoteBinding
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NoteActivity : AppCompatActivity() {

    lateinit var calendar: MaterialCalendarView
    private val binding by lazy { ActivityNoteBinding.inflate(layoutInflater) }
    val itemlist: MutableList<HospitalRecordData> = mutableListOf()
    val itemList2: MutableList<AIRecordData> = mutableListOf()
    var hospitalDateList: MutableList<String> = mutableListOf()
    var aiDateList: MutableList<String> = mutableListOf()
    val hospitalDays = hashSetOf<CalendarDay>()
    val inspectionDays = hashSetOf<CalendarDay>()
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        calendar = binding.calendarview

        //뒤로가기버튼클릭시
        binding.toolbar.setNavigationOnClickListener { finish() }

        //기록하기 버튼 눌렀을때 액티비티 이동
        binding.tvRecordPlus.setOnClickListener {
            val intent = Intent(this, RecordHosActivity::class.java)
            startActivity(intent)
        }


        //화면 처음에 모든날짜꺼 병원기록,AI기록 가져와서 점찍고 리사이클러.
        setCalendar()


        //선택한 날짜 클릭시 이벤트처리
        calendar.setOnDateChangedListener { widget, date, selected ->
            //date.date 가  CalendarDay{2024-3-25} 로 나옴. 4월인데 3월로나옴.
            val dateFormat =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())//심플데이트포맷이 알아서 1달 올려주나봄
            val formattedDate = dateFormat.format(date.date)
            binding.tvDate.text = formattedDate //2024-04-27로 나옴

            //선택한 날짜에 해당하는 데이터. 서버에서 가져오기
            getHospitalRecord(formattedDate) //클릭한 날짜
            getAIRecord(formattedDate) //클릭한 날짜
        }//온데이트체인지 리스너


        //"전체보기"버튼 클릭시 서버에서 모든 날짜꺼 전체 가져오기
        binding.btnAll.setOnClickListener {
            getHospitalRecord("")
            getAIRecord("")
        }//온클릭리스너


    }//oncreate


    //화면 처음에 보여질 칼렌더 세팅하기
    private fun setCalendar() {

        //오늘날짜 데코레이터
        calendar.addDecorators(TodayDecorator(this))
        //처음 보여질 날짜 - 오늘날짜로 셀렉
        calendar.setSelectedDate(CalendarDay.today())


        Log.d("ddfdsf", hospitalDateList.toString())

        //-------------병원방문 & AI검사기록-------------------------
        //서버에서 병원기록 갖고오기 -> hospitalDateList에 들어가고 -> 색과 함께 데코레이터 지정
        //비동기라, 아래 메소드 안에서 다시,
        //서버에서 AI기록 갖고오기 -> aiDataList에 들어가고 -> 색과 함꼐 데코레이터 지정
        getHospitalRecord("")
        Log.d("ddfdsf", hospitalDateList.toString())

    }//setCalendar()


    //특정날짜 클릭시 - 서버 - 병원기록 (""빈배열이면 모든날짜꺼 다가져오기)
    private fun getHospitalRecord(clickedDate: String?) {
        // 3단콤보. 개정보. 데이트(전체-빈값)
        //병원기록 받아오기  clickedDate==""이면 모든데이터가 오고, 특정날짜가 있으면 그거에 맞는것만 데이터가 오고..


        // hospitalListRequest 사용법
        val params = HospitalorAiRecordList(
            G.user_email,
            G.user_providerId,
            G.loginType,
            G.pet_id,
            clickedDate!!
        ) // pet_id는 pet 식별값

        // date : 날짜를 선택해서 검색, 전체 날짜 검색할 경우 빈 값
        RetrofitProcess(this, params = params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val data = (response as HospitalRecordList)
                data.code                              //  4204 서비스 회원 아님, 8000 병원 기록 목록 성공, 8001 병원 기록 목록 실패
                Log.d("dddfsdf", data.code) //8000성공^^
                itemlist.clear()
                hospitalDateList.clear()

                data.hospitalRecordList.forEach { hospital -> // forEach문을 돌면서 hospital 기록을 가져올 수 있음

                    var hospitalRecordData = HospitalRecordData(
                        hospital.id,                                         // 병원 기록 식별 값
                        hospital.name,                                       // 병원명
                        hospital.price,                                      // 진단가격
                        hospital.diagnosis,                                  // 진단명
                        hospital.visit_date,                                 // 진료일
                        hospital.description,                                // 진료내용
                        hospital.receipt_img_url,                            // 영수증 이미지 url
                        hospital.clinical_img_url                           // 진료사진 이미지 url
                    )

                    itemlist.add(hospitalRecordData)
                    hospitalDateList.add(hospitalRecordData.visit_date)
                    Log.d("DFdsf", hospitalDateList.toString())

                }//기차완성.

                if (clickedDate == ""){
                    //각 날짜 문자열에 대해 처리하기
                    hospitalDateList.forEach { dateString ->
                        //날짜 문자열을 Date객체로 파싱
                        val date = format.parse(dateString)
                        //Date객체를 CalendarDay로 변환
                        val calendar = Calendar.getInstance()
                        calendar.time = date
                        val calendarDay = CalendarDay.from(calendar)
                        //HashSet에 추가하기
                        hospitalDays.add(calendarDay)
                    }//forEach

                    //병원방문 컬러(빨간색)
                    val hospitalColor = Color.parseColor("#DF4E4E")
                    //달력에 쩜찍기 데코레이터
                    calendar.addDecorators(EventDecorator1(hospitalDays, hospitalColor))
                }//if


                //아답터에 대량의 리스트 넣어주기
                binding.recyclerViewHospital.adapter =
                    HospitalRecordAdapter(this@NoteActivity, itemlist)

                //-------------검사한날-----------------------------
                //서버에서 AI기록 갖고오기 -> aiDataList에 들어가고 -> 색과 함꼐 데코레이터 지정
                getAIRecord("")

            }//레스폰스 석세스

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("feed add fail", errorMsg!!) // 에러 메시지
            }

        }).hospitalListRequest()


    }//getHospitalRecord


    //서버 - AI검사기록 특정날짜꺼 받아오기(날짜가 ""이면 모든날짜가져옴)
    private fun getAIRecord(clickedDate: String?) {
        Log.d("dfsdg", clickedDate.toString())
        //AI검사기록 받아오기  clickedDate==""이면 모든데이터가 오고, 특정날짜가 있으면 그거에 맞는것만 데이터가 오고..

        // 32. ai 기록 목록 불러오기
        // aiListRequest 사용법
        val params = HospitalorAiRecordList(
            G.user_email,
            G.user_providerId,
            G.loginType,
            G.pet_id,
            clickedDate!!
        ) // pet_id는 pet 식별값
        Log.d("dfsdfff", params.toString())
        //date : 날짜를 선택해서 검색, 전체 날짜 검색할 경우 빈 값
        RetrofitProcess(this, params = params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val data = (response as AIRecordList)
                data.code //  4204 서비스 회원 아님, 9000 ai 기록 목록 성공, 9001 ai 기록 목록 실패
                Log.d("dfsf", data.code)
                itemList2.clear()

                data.aiRecordList.forEach { ai -> // forEach문을 돌면서 ai 기록을 가져올 수 있음

                    var aiRecordData = AIRecordData(
                        ai.id,                                         // ai 기록 식별 값
                        ai.diagnosis_type,                             // 진단한 ai type (eype or skin)
                        ai.diagnostic_img_url,                         // ai 진단한 반려견 이미지 url
                        ai.diagnosis_result                           // ai 진단결과 리스트(결막염 80%, 유루증 70%..)
                    )

                    itemList2.add(aiRecordData)
                    Log.d("fdfsdf", itemList2.toString())
                    //aiDateList.add(ai.)
                }//기차완성

//                aiDateList.add("2024-4-10")
//                aiDateList.add("2024-4-11")
//                aiDateList.add("2024-4-12")
//                aiDateList.add("2024-4-13")

                if (clickedDate == ""){

                    Log.d("dfsdfsd", aiDateList.toString())

                    //각 날짜 문자열에 대해 처리하기
                    aiDateList.forEach { dateString ->
                        //날짜 문자열을 Date객체로 파싱
                        val date = format.parse(dateString)
                        //Date객체를 CalendarDay로 변환
                        val calendar = Calendar.getInstance()
                        calendar.time = date
                        val calendarDay = CalendarDay.from(calendar)
                        //HashSet에 추가하기
                        inspectionDays.add(calendarDay)
                    }//forEach

                    //달력에 보일 병원기록 색깔 지정
                    val inspectionColor = Color.parseColor("#5754E1")
                    //쩜찍기 데코레이터 지정
                    calendar.addDecorators(EventDecorator2(inspectionDays, inspectionColor))
                }//if


                // 아답터에 대량의 리스트 넣어주기
                binding.recyclerViewAI.adapter = AIRecordAdapter(this@NoteActivity, itemList2)

            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("feed add fail", errorMsg!!) // 에러 메시지
            }

        }).aiListRequest()


    }


    //병원 방문 데코레이터(빨간점)
    inner class EventDecorator1(val dates: HashSet<CalendarDay>, val color: Int) :
        DayViewDecorator {//EventDecorator

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return dates.contains(day)
        }

        override fun decorate(view: DayViewFacade?) {
            // view?.addSpan(DotSpan(10f, Color.parseColor("#FFA800")))
            view?.addSpan(CustomMultipleDotSpan(5f, color))
            view?.addSpan(StyleSpan(Typeface.BOLD))
            view?.addSpan(RelativeSizeSpan(1.5f))
            view?.addSpan(ForegroundColorSpan(Color.parseColor("#737373")))
        }
    }


    //검사한날 데코레이터(파란점)
    inner class EventDecorator2(val dates: HashSet<CalendarDay>, val color: Int) :
        DayViewDecorator {//EventDecorator

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return dates.contains(day)
        }

        override fun decorate(view: DayViewFacade?) {
            // view?.addSpan(DotSpan(10f, Color.parseColor("#FFA800")))
            view?.addSpan(CustomMultipleDotSpan(5f, color))
            view?.addSpan(StyleSpan(Typeface.BOLD))
            view?.addSpan(RelativeSizeSpan(1.5f))
            view?.addSpan(ForegroundColorSpan(Color.parseColor("#737373")))
        }
    }


    //오늘날짜 데코
    inner class TodayDecorator(context: Context) : DayViewDecorator {//TodayDecorator

        val drawble = resources.getDrawable(R.drawable.sun, null)

        private var date = CalendarDay.today()
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return day?.equals(date)!!
        }

        override fun decorate(view: DayViewFacade?) {
            view?.setBackgroundDrawable(drawble)
            view?.addSpan(StyleSpan(Typeface.BOLD))
            view?.addSpan(RelativeSizeSpan(1.3f))
            view?.addSpan(ForegroundColorSpan(Color.parseColor("#706E6E")))
        }
    }


}//activity