package com.hsr2024.mungmungdoctortp.data



//한 개인이 피드글 하나 작성에서 데이터베이스에 인서트시키기
data class FeedAddOrUpdate(val email:String="", val provider_id:String="", val login_type:String, val imgurl:String, val content:String, val create_date:String)

//유저가 자기 피드글 중에서 어떠한 글 하나를 삭제
data class FeedDelete(val email:String="", val provider_id:String="", val login_type:String, val feed_id:String )

//QA 작성한걸 데이터베이스에 인서트시키기
data class QAAddOrUpdate(val email:String="", val provider_id:String="", val login_type:String, val imgurl:String, val title:String, val content:String)
//유저가 자기 질문글 삭제 데이터베이스 delete
data class QADelete(val email:String="", val provider_id:String="", val login_type:String, val qa_id:String )

//Comment 작성한걸 데이터베이스에 추가&수정 시키기
data class CommentAddOrUpdate(val email:String="", val provider_id:String="", val login_type:String, val content: String, val create_date: String)
//Comment 자기 질문글 삭제 데이터베이스 delete
data class CommentDelete(val email:String="", val provider_id:String="", val login_type:String, val comment_id:String)





