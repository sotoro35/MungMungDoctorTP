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
import java.util.Locale

class NoteActivity : AppCompatActivity() {

    lateinit var calendar: MaterialCalendarView
    private val binding by lazy { ActivityNoteBinding.inflate(layoutInflater) }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        //화면 켜졌을때 서버에서 기록들 다 가져와서 리사이클러에 보여주기
        getAllHospitalRecord()
        getAllAIRecord()




        val itemlist:MutableList<HospitalRecordData> = mutableListOf()
        val item1 = HospitalRecordData("제1병원", "목아파", "10만원", "오늘당장")
        val item2 = HospitalRecordData("제2병원", "어깨아파", "10만원", "오늘당장")
        val item3 = HospitalRecordData("제3병원", "댄나아팤ㅋ", "10만원", "오늘당장")
        itemlist.add(0, item1)
        itemlist.add(1, item2)
        itemlist.add(2,item3)
        //binding.recyclerViewHospital.adapter = HospitalRecordAdapter(this, itemlist)


        val aiList:MutableList<AIRecordData> = mutableListOf()
        val ai1 = AIRecordData("안구", "눈충혈80푸로", "눈튀나옴80프로", "2024/03/24")
        val ai2 = AIRecordData("피부", "푸석거림30프루", "기미79프로", "2024/03/24")
        val ai3 = AIRecordData("안구", "눈충혈80푸로", "눈튀나옴80프로", "2024/03/24")
        aiList.add(ai1)
        aiList.add(ai2)
        aiList.add(ai3)



        binding.tvRecordPlus.setOnClickListener {
            //기록하기 액티비티로 이동
            val intent = Intent(this, HealthDetailActivity::class.java)
            startActivity(intent)
        }






















        calendar = binding.calendarview
        //오늘날짜 데코레이터
        calendar.addDecorators(TodayDecorator(this))


        //처음 보여질 날짜 - 오늘날짜로 셀렉
        calendar.setSelectedDate(CalendarDay.today())

        //1.병원방문 컬러(빨간색)
        val hospitalColor = Color.parseColor("#DF4E4E")

        //1.병원방문 날들 서버에서 받아오기
        val day1 = CalendarDay.from(2024, 3, 19)
        val day2 = CalendarDay.from(2024,3,20)
        val day3 = CalendarDay.from(2024,3,21)
        val hospitalDays = hashSetOf(day1, day2, day3)




        //2.검사한날 컬러(파란색)
        val inspectionColor = Color.parseColor("#5754E1")


        //2.검사한 날들
        val day4 = CalendarDay.from(2024, 3, 19)
        val day5 = CalendarDay.from(2024,3,20)
        val day6 = CalendarDay.from(2024,3,29)
        val inspectionDays = hashSetOf(day4, day5, day6)


        calendar.addDecorators(EventDecorator1(hospitalDays, hospitalColor))
        calendar.addDecorators(EventDecorator2(inspectionDays, inspectionColor))








        //어떤 날짜 클릭시 이벤트처리
        calendar.setOnDateChangedListener { widget, date, selected ->
            //date.date 가  CalendarDay{2024-3-25} 로 나옴. 4월인데 3월로나옴.
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())//심플데이트포맷이 알아서 1달 올려주나봄
            val formattedDate = dateFormat.format(date.date)
            binding.tvDate.text = formattedDate //이제 2024.04.27로 나옴
            //서버에서 병원간날, 검사한날 date 모두 가져오기
            //예로, 4월25 클릭하면  서버에 날짜 보내고, 그거에 해당하는 데이터를 리스트로 가져와서 리사이클러로 보여주기.
            getHospitalRecord(formattedDate) //클릭한 날짜
            getAIRecord(formattedDate) //클릭한 날짜
        }//온클릭 리스너


        binding.btnAll.setOnClickListener {
            getHospitalRecord("")
            getAIRecord("")
            //서버에서 유저의 병원기록, AI검사기록 전부 받아온 리스트..를 아답터에..
            binding.recyclerViewHospital.adapter = HospitalRecordAdapter(this, itemlist)
            binding.recyclerViewAI.adapter = AIRecordAdapter(this, aiList)
        }












    }//oncreate






    //서버 - 병원기록 특정날짜꺼 받아오기
    private fun getHospitalRecord(clickedDate: String?){
        val date = clickedDate
        3단콤보. 개정보. 데이트(전체-빈값)
        //레트로핏으로 유저가 저장한 병원기록  전부~ 받아와서 clickedDate랑 같은 날짜를 리스트에 넣기.


        //이후 아답터에 대량의 리스트 넣어주기
        //binding.recyclerViewHospital.adapter = HospitalRecordAdapter(this, itemlist)
    }


    //서버 - AI검사기록 특정날짜꺼 받아오기
    private fun getAIRecord(clickedDate: String?){
        val date = clickedDate

        //레트로핏으로 유저가 저장한 AI기록 전부~~ 받아와서 clickedDate랑 같은 날짜를 리스트에 넣기
        @GET("---")
        fun getAIRecord(@Query("query") id:String, @Query("query") dog:String, @Query("query") date:String) : Call<>


        // 이후 아답터에 대량의 리스트 넣어주기
        //binding.recyclerViewAI.adapter = AIRecordAdapter(this, aiList)
    }








    //서버 - 병원기록 전부 받아오기
    private fun getAllHospitalRecord(){
        //레트로핏으로 유저가 저장한 병원기록  전부~ 받아와서 clickedDate랑 같은 날짜를 리스트에 넣기.


        //이후 아답터에 대량의 리스트 넣어주기
        //binding.recyclerViewHospital.adapter = HospitalRecordAdapter(this, itemlist)
    }


    //서버 - AI검사기록 전부 받아오기
    private fun getAllAIRecord(){
        //레트로핏으로 유저가 저장한 AI기록 전부~~ 받아와서 clickedDate랑 같은 날짜를 리스트에 넣기

        // 이후 아답터에 대량의 리스트 넣어주기
        //binding.recyclerViewAI.adapter = AIRecordAdapter(this, aiList)
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