package com.hsr2024.mungmungdoctortp.bnv3community

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.hsr2024.mungmungdoctortp.FeedG
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.data.AddorModifyorDeleteFeed
import com.hsr2024.mungmungdoctortp.data.FeedData
import com.hsr2024.mungmungdoctortp.data.FeedFavor
import com.hsr2024.mungmungdoctortp.databinding.FeeditemBinding
import com.hsr2024.mungmungdoctortp.main.MainActivity
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess

class FeedListAdapter(val context: Context, var items:List<FeedData>) : Adapter<FeedListAdapter.VH>() {
    inner class VH(val binding: FeeditemBinding) : ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=VH(FeeditemBinding.inflate(LayoutInflater.from(context),parent,false))
    override fun getItemCount()=items.size
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item=items[position]
        val profile= "http://43.200.163.153/img/${item.profile_imgurl}"
        val imgUrl= "http://43.200.163.153/img/${item.imgurl}"
        Glide.with(context).load(profile).into(holder.binding.circleIv2)
        holder.binding.tv.text=item.nickname
        Glide.with(context).load(imgUrl).into(holder.binding.iv)
        holder.binding.tvFavorite.text=item.favorite
        holder.binding.tvComment.text=item.comment
        holder.binding.content.text=item.content
        holder.binding.tvDate.text=item.create_date

        if (item.isFavorite){
            holder.binding.ivFavorite.setImageResource(R.drawable.favorites)
        }else holder.binding.ivFavorite.setImageResource(R.drawable.favorite)

        Log.d("imgUrl",imgUrl)
        Glide.with(context).load(imgUrl).into(holder.binding.iv)

        if (item.myFeed == "1"){
            holder.binding.toolbar.setOnMenuItemClickListener { toolbar->
                when(toolbar.itemId){
                    R.id.menu_insert->{
                        FeedG.FeedId = item.feed_id
                        FeedG.FeedText = item.content
                        FeedG.FeedImg = item.imgurl
                        val intent = Intent(context,FeedInsertActivity::class.java)
                        context.startActivity(intent)
                        true
                    }
                    R.id.menu_delete -> {
                        AlertDialog.Builder(context)
                            .setTitle("삭제 하시겠습니까?")
                            .setMessage("삭제하시면 복구하실 수 없습니다")
                            .setPositiveButton("확인") { dialog, which ->
                                Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                                //서버에서 삭제
                                sss = item.feed_id
                                feedDelete(item.feed_id)
                                items = items.filter { item.feed_id != sss }
                                notifyDataSetChanged()
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

        }else holder.binding.toolbar.overflowIcon = null


        holder.binding.ivFavorite.setOnClickListener {
            val params= FeedFavor("${G.user_email}", "${G.user_providerId}", "${G.loginType}", "${item.feed_id}") // feed_id feed 식별값
            RetrofitProcess(context, params=params, callback = object : RetrofitCallback {
                override fun onResponseListSuccess(response: List<Any>?) {}

                override fun onResponseSuccess(response: Any?) {
                    val code=(response as String) //  - 4204 서비스 회원 아님, 6900 feed 찜 추가 성공, 6901 feed 찜 삭제, 6902 feed 찜 실패
                    Log.d("feed favor code","$code")

                    when(code){
                        "6900"-> {

                            val currentFavoriteCount = holder.binding.tvFavorite.text.toString().toInt()
                            val newFavoriteCount = if (item.isFavorite)currentFavoriteCount -1 else currentFavoriteCount +1

                            holder.binding.tvFavorite.text= newFavoriteCount.toString()

                            val newHeartImage = if(item.isFavorite) R.drawable.favorite else R.drawable.favorites

                            //val favor = if(item.isFavorite) false else true

                            holder.binding.ivFavorite.setImageResource(newHeartImage)
                            item.isFavorite =! item.isFavorite

                            //holder.binding.tvFavorite.text= newFavoriteCount.toString()

                        }
                        "6901"->{
//                        holder.binding.tvFavorite.text = (item.favorite.toInt() -1 ).toString()
//                        holder.binding.ivFavorite.setImageResource(R.drawable.favorite)
                        }
                    }

                }

                override fun onResponseFailure(errorMsg: String?) {
                    Log.d("feed favor fail",errorMsg!!) // 에러 메시지
                }

            }).feedFavorRequest()

//            val currentFavoriteCount = holder.binding.tvFavorite.text.toString().toInt()
//            val newFavoriteCount = if (item.isFavorite)currentFavoriteCount -1 else currentFavoriteCount +1
//
//            holder.binding.tvFavorite.text= newFavoriteCount.toString()
//
//            val newHeartImage = if(item.isFavorite) R.drawable.favorite else R.drawable.favorites
//
//            val favor = if(item.isFavorite) false else true
//
//            holder.binding.ivFavorite.setImageResource(newHeartImage)
//            item.isFavorite =! item.isFavorite

//            val currentFavoriteCount = holder.binding.tvFavorite.text.toString().toInt()
//            val newFavoriteCount = if (item.isFavorite)currentFavoriteCount -1 else currentFavoriteCount +1
        }



        holder.binding.ivComment.setOnClickListener {
            val intent= Intent(context, CommentActivity::class.java)
            intent.putExtra("feed",item.feed_id)
            context.startActivity(intent)
        }
    }//holder


    // 데이터 업데이트
    fun setData(newItems: List<FeedData>) {
        items = newItems
        notifyDataSetChanged()
    }
    var sss:String = ""
    fun feedDelete(feedid:String){

        val params= AddorModifyorDeleteFeed("${G.user_email}", "${G.user_providerId}", "${G.loginType}", "$feedid") // feed_id는 feed 식별값
        RetrofitProcess(context, params=params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val code=(response as String) //  - 4204 서비스 회원 아님, 6600 feed 삭제 성공, 6601 feed 삭제 실패
                Log.d("feed delete code","$code")

                when (code) {
                    "4204" -> {
                        Toast.makeText(context, "관리자에게 문의하세요.", Toast.LENGTH_SHORT).show()
                        Log.d("feed오류", "서비스 회원 아님")
                    }

                    "6601" -> {
                        Toast.makeText(
                            context,
                            "관리자에게 문의하세요",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("feed오류", "삭제 실패")
                    }

                    "6600" -> {
                        Toast.makeText(context, "삭제 완료되었습니다.", Toast.LENGTH_SHORT)
                            .show()
                        sss= "성공"

                    }
                }

            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("feed delete fail",errorMsg!!) // 에러 메시지
            }

        }).feedDeleteRequest()


    }
}