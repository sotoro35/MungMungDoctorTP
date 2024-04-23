package com.hsr2024.mungmungdoctortp.main

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.hsr2024.mungmungdoctortp.FeedG
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.bnv3community.CommentActivity
import com.hsr2024.mungmungdoctortp.bnv3community.FeedInsertActivity
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
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item=items[position]
        Glide.with(context).load("http://43.200.163.153/img/"+item.profile_imgurl).into(holder.binding.circleIv2)
        holder.binding.tvTitle.text=item.content
        holder.binding.tvNickname.text=item.nickname
        holder.binding.tvDate.text=item.create_date
        holder.binding.toolbar.setOnMenuItemClickListener { toolbar->
            when(toolbar.itemId){
                R.id.menu_insert->{
                    (context as CommentActivity).setEditText(holder.binding.tvTitle.text.toString())
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

                            (context as CommentActivity).commentList()
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
    }

    private fun commentDelete(commentid:String) {
        val boardId=(context as CommentActivity).feedId
        val params= AddorModifyorDeleteComment("${G.user_email}", "${G.user_providerId}", "${G.loginType}", "$boardId", "$commentid", "" ) // board_id는 feed, qa 식별값
        // comment_id는 댓글  식별 값
        RetrofitProcess(context, params=params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val code=(response as String) //  - 4204 서비스 회원 아님, 6800 feed 댓글 삭제 성공, 6801 feed 댓글 삭제 실패
                Log.d("feed comment delete code","$code")

            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("feed comment delete fail",errorMsg!!) // 에러 메시지
            }

        }).feedCommentDeleteRequest()
    }

}