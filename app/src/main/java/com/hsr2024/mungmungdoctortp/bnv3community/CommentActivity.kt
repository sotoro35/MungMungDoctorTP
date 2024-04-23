package com.hsr2024.mungmungdoctortp.bnv3community

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.data.AddorModifyorDeleteComment
import com.hsr2024.mungmungdoctortp.data.CommentData
import com.hsr2024.mungmungdoctortp.data.CommentDataList
import com.hsr2024.mungmungdoctortp.data.FeedCommentList
import com.hsr2024.mungmungdoctortp.databinding.ActivityCommentBinding
import com.hsr2024.mungmungdoctortp.main.CommentListAdapter
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess

class CommentActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCommentBinding.inflate(layoutInflater) }
    private var commentAdapter: CommentListAdapter? = null
    private val items: MutableList<CommentData> = mutableListOf()
    val feedId by lazy {intent.getStringExtra("feed")}
    var boardType=""
    var commentId=""
    val boardId=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener { finish() }
        commentList()
        binding.tvRegister.setOnClickListener {
            val comment=binding.layoutComment.editText?.text.toString()
            if(comment!="") {
                commentAdd(comment)
                binding.layoutComment.editText?.setText("")
            }
        }
    }

    override fun onResume() {
        super.onResume()

        commentList()
    }

    fun commentList() {
        val params= FeedCommentList("$feedId", "${G.user_email}", "${G.user_providerId}", "${G.loginType}") // 비로그인일 경우 이메일 정보, provider_id, login_type 빈 값 가능
        RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val data=(response as CommentDataList)
                if(data.code=="6300") {
                    items.clear()
                    items.addAll(data.commentDatas)
                    commentAdapter = CommentListAdapter(this@CommentActivity, items)
                    binding.recyclerView.adapter = commentAdapter
                    commentAdapter?.notifyDataSetChanged()
                }
            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("feed comment list fail",errorMsg!!) // 에러 메시지
            }

        }).feedCommentListRequest()
    }

    private fun commentAdd(comment:String) {
        val params= AddorModifyorDeleteComment("${G.user_email}", "${G.user_providerId}", "${G.loginType}", "$feedId", "", "$comment" )

        RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val code=(response as String) //  - 4204 서비스 회원 아님, 6650 feed 댓글 추가 성공, 6651 feed 댓글 추가 실패
                if(code=="6650") Toast.makeText(this@CommentActivity, "댓글 작성을 완료 하였습니다.", Toast.LENGTH_SHORT).show()
                commentList()
            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("feed comment add fail",errorMsg!!) // 에러 메시지
            }

        }).feedCommentAddRequest()
    }
    fun setEditText(text:String) {
        binding.layoutComment.editText?.setText("$text")
    }
}