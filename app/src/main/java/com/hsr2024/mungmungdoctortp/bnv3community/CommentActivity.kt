package com.hsr2024.mungmungdoctortp.bnv3community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hsr2024.mungmungdoctortp.databinding.ActivityCommentBinding
import com.hsr2024.mungmungdoctortp.main.CommentListAdapter

class CommentActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCommentBinding.inflate(layoutInflater) }
    private var commentAdapter: CommentListAdapter? = null
    private val items: MutableList<CommentData> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        commentAdapter = CommentListAdapter(this, items)
        binding.recyclerView.adapter = commentAdapter

    }

    override fun onResume() {
        super.onResume()
        // 임시 데이터 추가

        items.clear()
        items.add(CommentData("1", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ_bPVLRowjzhD-ZGGFR4030vnxuvqueINKSNcbtg5Lpg&s", "commentnickname",
            "Content 내용", "2024-01-01"))
        items.add(
            CommentData(
                "2",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ_bPVLRowjzhD-ZGGFR4030vnxuvqueINKSNcbtg5Lpg&s",
                "anothercommenter",
                "Another comment",
                "2024-01-02"
            )
        )

        // RecyclerView 갱신
        commentAdapter?.notifyDataSetChanged()
    }
}