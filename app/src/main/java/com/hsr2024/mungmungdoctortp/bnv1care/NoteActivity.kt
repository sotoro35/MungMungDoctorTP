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
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.adapter.AIRecordAdapter
import com.hsr2024.mungmungdoctortp.adapter.HospitalRecordAdapter
import com.hsr2024.mungmungdoctortp.data.AIRecordData
import com.hsr2024.mungmungdoctortp.data.HospitalRecordData
import com.hsr2024.mungmungdoctortp.databinding.ActivityNoteBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import retrofit2.Call
import retrofit2.Retrofit
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




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //뒤로가기버튼클릭시
        binding.toolbar.setNavigationOnClickListener { finish() }

        //화면 켜졌을때 서버에서 기록들 다 가져와서 리사이클러에 보여주기
        getHospitalRecord("")
        getAIRecord("")


        //기록하기 버튼 눌렀을때 액티비티 이동
        binding.tvRecordPlus.setOnClickListener {
            val intent = Intent(this, HealthDetailActivity::class.java)
            startActivity(intent)
        }


        //화면 처음에 칼렌다 세팅하기(유저가 저장한 병원기록,AI기록 날짜 다 받아와서 칼렌다에 쩜찍기)
        setCalendar()



        //선택한 날짜 클릭시 이벤트처리
        calendar.setOnDateChangedListener { widget, date, selected ->
            //date.date 가  CalendarDay{2024-3-25} 로 나옴. 4월인데 3월로나옴.
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())//심플데이트포맷이 알아서 1달 올려주나봄
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





    private fun setCalendar(){
        calendar = binding.calendarview
        //오늘날짜 데코레이터
        calendar.addDecorators(TodayDecorator(this))
        //처음 보여질 날짜 - 오늘날짜로 셀렉
        calendar.setSelectedDate(CalendarDay.today())

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())



        //-------------병원방문-------------------------


        //1.병원방문 컬러(빨간색)
        val hospitalColor = Color.parseColor("#DF4E4E")


        //서버에서 가지고 온 날짜
        //레트로핏 작업

        val dateStrings1 = listOf("2024-04-01", "2024-04-02")
        val hospitalDays = hashSetOf<CalendarDay>()

        //각 날짜 문자열에 대해 처리하기
        dateStrings1.forEach{ dateString->
            //날짜 문자열을 Date객체로 파싱
            val date = format.parse(dateString)
            //Date객체를 CalendarDay로 변환
            val calendar = Calendar.getInstance()
            calendar.time = date
            val calendarDay = CalendarDay.from(calendar)
            //HashSet에 추가하기
            hospitalDays.add(calendarDay)
        }//forEach











        //-------------검사한날-----------------------------
        //2.검사한날 컬러(파란색)
        val inspectionColor = Color.parseColor("#5754E1")

        //서버에서 가지고 온 날짜
        //레트로핏 작업

        val dateStrings2 = listOf("2024-04-01", "2024-04-03")
        val inspectionDays = hashSetOf<CalendarDay>()

        //각 날짜 문자열에 대해 처리하기
        dateStrings2.forEach{ dateString->
            //날짜 문자열을 Date객체로 파싱
            val date = format.parse(dateString)
            //Date객체를 CalendarDay로 변환
            val calendar = Calendar.getInstance()
            calendar.time = date
            val calendarDay = CalendarDay.from(calendar)
            //HashSet에 추가하기
            inspectionDays.add(calendarDay)
        }//forEach




        calendar.addDecorators(EventDecorator1(hospitalDays, hospitalColor))
        calendar.addDecorators(EventDecorator2(inspectionDays, inspectionColor))


    }//setCalendar()












    //특정날짜 클릭시 - 서버 - 병원기록 특정날짜꺼 받아오기
    private fun getHospitalRecord(clickedDate: String?){
        val date = clickedDate
        Log.d("ff", date.toString())
        // 3단콤보. 개정보. 데이트(전체-빈값)
        //병원기록 받아오기  clickedDate==""이면 모든데이터가 오고, 특정날짜가 있으면 그거에 맞는것만 데이터가 오고..

        val itemlist:MutableList<HospitalRecordData> = mutableListOf()



        // 아이템 리스트
        val items = listOf(
            HospitalRecordData("제1병원", "목아파", "10만원", "오늘당장"),
            HospitalRecordData("제2병원", "어깨아파", "10만원", "오늘당장"),
            HospitalRecordData("제3병원", "댄나아팤ㅋ", "10만원", "오늘당장")
        )

        // 각 아이템을 리스트에 추가합니다.
        items.forEach { item ->
            itemlist.add(item)
        }



        //아답터에 대량의 리스트 넣어주기
        binding.recyclerViewHospital.adapter = HospitalRecordAdapter(this, itemlist)
    }







    //서버 - AI검사기록 특정날짜꺼 받아오기
    private fun getAIRecord(clickedDate: String?){
        val date = clickedDate

        //AI검사기록 받아오기  clickedDate==""이면 모든데이터가 오고, 특정날짜가 있으면 그거에 맞는것만 데이터가 오고..

        val aiList:MutableList<AIRecordData> = mutableListOf()
        val ai1 = AIRecordData("안구", "눈충혈80푸로", "눈튀나옴80프로", "2024/03/24")
        val ai2 = AIRecordData("피부", "푸석거림30프루", "기미79프로", "2024/03/24")
        val ai3 = AIRecordData("안구", "눈충혈80푸로", "눈튀나옴80프로", "2024/03/24")
        aiList.add(ai1)
        aiList.add(ai2)
        aiList.add(ai3)

        // 이후 아답터에 대량의 리스트 넣어주기
        binding.recyclerViewAI.adapter = AIRecordAdapter(this, aiList)
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