package com.hsr2024.mungmungdoctortp.data

data class QAData(val qa_id:String, val imgurl:String, val title:String, val nickname :String, val view_count:String, val comment_count:String)
data class FeedData(val feed_id:String, val profile_imgurl: String, val nickname: String, val imgurl:String, val favorite:String, var isFavorite:Boolean, val comment: String, val content:String, val create_date:String )
data class CommentData(val comment_id:String, val profile_imgurl: String, val nickname: String, val content: String, val create_date: String)
