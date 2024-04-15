package com.hsr2024.mungmungdoctortp.bnv1care

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.LineBackgroundSpan

class CustomMultipleDotSpan : LineBackgroundSpan {

    private val radius: Float
    private var color: IntArray

    constructor() {
        radius = DEFAULT_RADIUS
        color = IntArray(1)
        color[0] = 0
    }

    constructor(color: Int) {
        radius = DEFAULT_RADIUS
        this.color = IntArray(1)
        this.color[0] = color
    }

    constructor(radius: Float) {
        this.radius = radius
        color = IntArray(1)
        color[0] = 0
    }

    constructor(radius: Float, color: IntArray) {
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
        val total = if (color.size > 5) 5 else color.size
        var leftMost = (total - 1) * -10

        for (i in 0 until total) {
            val oldColor = paint.color
            if (color[i] != 0) {
                paint.color = color[i]
            }
            canvas.drawCircle(
                (left + right) / 2.toFloat() - leftMost,
                bottom + radius,
                radius,
                paint
            )
            paint.color = oldColor
            leftMost += 20
        }
    }

    companion object {
        private const val DEFAULT_RADIUS = 0f
    }
}