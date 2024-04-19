package com.hsr2024.mungmungdoctortp.bnv1care

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.style.LineBackgroundSpan
import android.util.Log
import android.widget.Toast
import kotlin.coroutines.coroutineContext

class CustomMultipleDotSpan : LineBackgroundSpan {

    private val radius: Float
    private var color: Int


    constructor(radius: Float, color: Int) {
        this.radius = radius
        this.color = color
    }

    override fun drawBackground(
        canvas: Canvas,
        paint: Paint,
        left: Int,
        right: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        charSequence: CharSequence,
        start: Int,
        end: Int,
        lineNum: Int
    ) {

        Log.d("aaaf", color.toString())

        if (color == -2142642) {
            //빨간점이면 - 병원갔다온날은 왼쪽에
            paint.color = color
            canvas.drawCircle(
                ((left + right) / 2 + 15).toFloat(),
                bottom + radius,
                radius,
                paint
            )
        }



        if (color == -11053855) {
            //파란점이면 - 병원갔다온날은 왼쪽에
            paint.color = color
            canvas.drawCircle(
                ((left + right) / 2 - 10).toFloat(),
                bottom + radius,
                radius,
                paint
            )
        }


    }//drawBackground()


}//CustomMultipleDotSpan class


