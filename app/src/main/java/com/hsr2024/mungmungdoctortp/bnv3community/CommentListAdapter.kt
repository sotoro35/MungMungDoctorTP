package com.hsr2024.mungmungdoctortp.main

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.camera.video.VideoRecordEvent.Resume
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.hsr2024.mungmungdoctortp.FeedG
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.QAG
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.bnv3community.CommentActivity
import com.hsr2024.mungmungdoctortp.bnv3community.FeedInsertActivity
import com.hsr2024.mungmungdoctortp.bnv3community.QADetailActivity
import com.hsr2024.mungmungdoctortp.data.AddorModifyorDeleteComment
import com.hsr2024.mungmungdoctortp.data.CommentData
import com.hsr2024.mungmungdoctortp.databinding.CommentItemBinding
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess
import okio.ByteString.Companion.encode
import okio.ByteString.Companion.encodeUtf8

class CommentListAdapter(val context:Context, var items:List<CommentData>) : Adapter<CommentListAdapter.VH>() {
    inner class VH(val binding: CommentItemBinding) : ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=VH(CommentItemBinding.inflate(LayoutInflater.from(context),parent,false))
    override fun getItemCount()=items.size
    var boardType=""
    var boardId=""
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item=items[position]
        Glide.with(context).load("http://43.200.163.153/img/"+item.profile_imgurl).into(holder.binding.circleIv2)
        holder.binding.tvTitle.text=item.content
        holder.binding.tvNickname.text=item.nickname
        holder.binding.tvDate.text=item.create_date
        holder.binding.toolbar.setOnMenuItemClickListener { toolbar->
            when(toolbar.itemId){
                R.id.menu_insert->{
                    Toast.makeText(context, "$boardType", Toast.LENGTH_SHORT).show()
                    if(boardType=="feed" || boardType=="qa") {
                        (context as CommentActivity).setEditText(holder.binding.tvTitle.text.toString())
                        (context as CommentActivity).methodType="modify"
                        (context as CommentActivity).commentId=item.comment_id
                    } else {
                        (context as QADetailActivity).setEditText(holder.binding.tvTitle.text.toString())
                        (context as QADetailActivity).methodType="modify"
                        (context as QADetailActivity).commentId=item.comment_id
                    }
                    true
                }
                R.id.menu_delete -> {
                    AlertDialog.Builder(context)
                        .setTitle("삭제 하시겠습니까?")
                        .setMessage("삭제하시면 복구하실 수 없습니다")
                        .setPositiveButton("확인") { dialog, which ->
                            Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                            //서버에서 삭제
                            var sss:String = ""
                            sss = item.comment_id
                            commentDelete(item.comment_id)
                            if(boardType=="feed" || boardType=="qa") {
                                (context as CommentActivity).commentList()
                            } else {
                                //qaDetail에서
                            }
                            dialog.dismiss()
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

        if(item.myComment=="0") { // 내가 작성한 것이 아니면 수정, 삭제가 보이지 않음
            holder.binding.toolbar.visibility= View.INVISIBLE
        }

        if(QAG.QAId=="") {
            boardType=(context as CommentActivity).boardType
            boardId=(context as CommentActivity).boardId
        } else {
            boardType="qadetail"
            boardId=QAG.QAId
        }

    }

    private fun commentDelete(commentid:String) {
        if(boardType=="feed") {
            val params= AddorModifyorDeleteComment("${G.user_email}", "${G.user_providerId}", "${G.loginType}", "$boardId", "$commentid", "" )
            // comment_id는 댓글  식별 값
            RetrofitProcess(context, params=params, callback = object : RetrofitCallback {
                override fun onResponseListSuccess(response: List<Any>?) {}

                override fun onResponseSuccess(response: Any?) {
                    val code=(response as String)
                    if(code=="6800") {
                        Toast.makeText(context, "feed 댓글 삭제에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponseFailure(errorMsg: String?) {
                    Log.d("feed comment delete fail",errorMsg!!)
                }

            }).feedCommentDeleteRequest()
        } else {
            val params= AddorModifyorDeleteComment("${G.user_email}", "${G.user_providerId}", "${G.loginType}", "$boardId", "$commentid", "" )
            // comment_id는 댓글  식별 값
            RetrofitProcess(context, params=params, callback = object : RetrofitCallback {
                override fun onResponseListSuccess(response: List<Any>?) {}

                override fun onResponseSuccess(response: Any?) {
                    val code=(response as String)
                    if(code=="7800") {
                        Toast.makeText(context, "feed 댓글 삭제에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onResponseFailure(errorMsg: String?) {
                    Log.d("qa comment delete fail",errorMsg!!)
                }

            }).qaCommentDeleteRequest()
        }
    }

}