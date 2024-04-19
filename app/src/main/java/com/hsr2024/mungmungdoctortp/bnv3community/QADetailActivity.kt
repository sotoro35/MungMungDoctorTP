package com.hsr2024.mungmungdoctortp.bnv3community

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.hsr2024.mungmungdoctortp.FeedG
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.QAG
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.data.AddorModifyorDeleteComment
import com.hsr2024.mungmungdoctortp.data.AddorModifyorDeleteQA
import com.hsr2024.mungmungdoctortp.data.CommentData
import com.hsr2024.mungmungdoctortp.data.CommentDataList
import com.hsr2024.mungmungdoctortp.data.QA
import com.hsr2024.mungmungdoctortp.data.QABoard
import com.hsr2024.mungmungdoctortp.data.QACommentList
import com.hsr2024.mungmungdoctortp.data.QAData
import com.hsr2024.mungmungdoctortp.databinding.ActivityQadetailBinding
import com.hsr2024.mungmungdoctortp.main.CommentListAdapter
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class QADetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityQadetailBinding.inflate(layoutInflater) }
    private var commentAdapter: CommentListAdapter? = null
    private val items: MutableList<CommentData> = mutableListOf()

    var profile_imgurl: String? = null
    var nickname: String? = null
    var title: String? = null
    var content: String? = null
    var img: String? = null
    var comment_count: String? = null
    var view_count: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        load()
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_insert -> {
                    val intent = Intent(this@QADetailActivity, QAInsertActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.menu_delete -> {
                    AlertDialog.Builder(this)
                        .setTitle("삭제 하시겠습니까?")
                        .setMessage("삭제하시면 복구하실 수 없습니다")
                        .setPositiveButton("확인") { dialog, which ->
                            Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("취소") { dialog, which ->
                            dialog.dismiss()
                        }
                        .create().show()
                    true
                }

                else -> false

            }
        }


    }//oncreate()

    override fun onResume() {
        super.onResume()
        // 임시 데이터 추가
        load()

    }


    private fun setting(){

        profile_imgurl = intent.getStringExtra("profileImg")
        nickname = intent.getStringExtra("nickname")
        title = intent.getStringExtra("title")
        content = intent.getStringExtra("content")
        img = intent.getStringExtra("img")
        comment_count = intent.getStringExtra("comment_count")
        view_count = intent.getStringExtra("view_count")

        Log.d("dsfasdfl", profile_imgurl.toString())

        Glide.with(this).load(profile_imgurl).into(binding.circleIv)
        binding.tvNickname.text = nickname
        binding.tvTitle.text = title
        Glide.with(this).load(img).into(binding.iv)
        binding.tvComment.text = comment_count
        binding.tvWatch.text= view_count
        binding.tvContent.text = content

    }//테이크인포메이션


    private fun load(){
        val params= QA("${QAG.QAId}", "${G.user_email}", "${G.user_providerId}", "email") // qa_id는 qa 식별자
        RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val data=(response as QABoard)//  - 4204 서비스 회원 아님, 7220 qa 성공, 7221 qa 실패

                if (data.code == "7220"){
                    profile_imgurl = "http://43.200.163.153/img/${data.profile_imgurl}"
                    nickname = data.nickname
                    title = data.title
                    content = data.content
                    img = "http://43.200.163.153/img/${data.imgurl}"
                    comment_count = data.comment_count
                    view_count = data.view_count

                    Glide.with(this@QADetailActivity).load(profile_imgurl).into(binding.circleIv)
                    binding.tvNickname.text = nickname
                    binding.tvTitle.text = title
                    Glide.with(this@QADetailActivity).load(img).into(binding.iv)
                    binding.tvComment.text = comment_count
                    binding.tvWatch.text= view_count
                    binding.tvContent.text = content
                }

            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("qa fail",errorMsg!!) // 에러 메시지
            }

        }).QARequest()

    }

    private fun commentload(){

        val s=intent.getStringExtra("QAData")
        val qaData= Gson().fromJson(s, QAData::class.java)
        //댓글 목록불러오기
        val params= QACommentList("${qaData.qa_id}", "${G.user_email}", "${G.user_providerId}", "email") // 비로그인일 경우 이메일 정보, provider_id, login_type 빈 값 가능
        RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val data = (response as CommentDataList)
                Log.d("qa comment list  code", "$data")
                data.code                              // - 7300 qa comment 목록 성공, 7301 qa comment 목록 실패, 4204 서비스 회원 아님
                if(data.code=="7300") {
                    binding.recyclerView.adapter=CommentListAdapter(this@QADetailActivity,data.commentDatas)

                }

            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("qa comment list fail", errorMsg!!) // 에러 메시지
            }
        }).qaCommentListRequest()

        //binding.tvRegister.setOnClickListener { clickregister() }
        commentAdapter = CommentListAdapter(this, items)
        binding.recyclerView.adapter = commentAdapter
        commentAdapter?.notifyDataSetChanged()


    }
}