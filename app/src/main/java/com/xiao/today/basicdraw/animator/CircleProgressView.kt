package com.xiao.today.basicdraw.animator

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.xiao.today.basicdraw.dp2px
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

class CircleProgressView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val STROKE_WIDTH = 40.dp2px

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = STROKE_WIDTH
        strokeCap = Paint.Cap.ROUND
        textSize = 40.dp2px
        textAlign = Paint.Align.CENTER
    }

    private val arcRect by lazy {
        RectF(
            STROKE_WIDTH / 2,
            (height - width) / 2f + STROKE_WIDTH / 2,
            width / 1f - STROKE_WIDTH / 2,
            (height + width) / 2f - STROKE_WIDTH / 2,
        )
    }

    var currentProgress: Float = 0f //（0 - 100）
        set(value) {
            field = value
            invalidate()
        }

    private val startAngle = -90f

    private val fontMetrics = Paint.FontMetrics()

    private val animation = ObjectAnimator.ofFloat(this, "currentProgress", 100f).apply {
        duration = 5_000
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.style = Paint.Style.STROKE
        paint.color = Color.parseColor("#90a4ad")
        canvas.drawArc(arcRect, startAngle + currentProgress * 3.6f, 360f - (startAngle + currentProgress * 3.6f), false, paint)
        paint.color = Color.parseColor("#ff4181")
        canvas.drawArc(arcRect, startAngle, currentProgress * 3.6f, false, paint)
        paint.style = Paint.Style.FILL
        paint.getFontMetrics(fontMetrics)
        canvas.drawText("${currentProgress.roundToInt().absoluteValue}%", width/2f, height/2f - (fontMetrics.ascent + fontMetrics.descent) / 2, paint)
        if (!animation.isStarted)animation.start()
    }
}