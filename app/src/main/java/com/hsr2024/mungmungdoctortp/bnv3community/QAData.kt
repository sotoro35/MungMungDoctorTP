package com.hsr2024.mungmungdoctortp.bnv3community

data class QAData(val imgurl:String, val title:String, val nickname :String, val view_count:String, val comment_count:String)
data class FeedData(val profile_imgurl: String, val nickname: String, val imgurl:String, val favorite:String, val comment: String, val content:String, val create_date:String )
data class CommentData(val profile_imgurl: String, val nickname: String, val content: String, val create_date: String)
