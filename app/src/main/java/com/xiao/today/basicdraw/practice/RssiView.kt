package com.xiao.today.basicdraw.practice

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.toColorInt
import androidx.core.graphics.withSave
import com.xiao.today.basicdraw.dp2px

class RssiView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val rssiMaxHeight = 32.dp2px
    private val rssiWidth = 4.dp2px.toFloat()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = "#99ffffff".toColorInt()
        strokeWidth = rssiWidth
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val h = resolveSize((rssiMaxHeight + 4.dp2px).toInt(), heightMeasureSpec)
        val w = resolveSize(((rssiWidth + 10.dp2px) * (Level.HIGH.value + 1)).toInt(), widthMeasureSpec)
        setMeasuredDimension(w, h)
    }

    var level: Level = Level.MID
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.BLACK)
        canvas.withSave {
            translate(rssiWidth / 2 + 4.dp2px, 4.dp2px)
            for (index in 0..Level.HIGH.value) {
                val startX = 0f + (rssiWidth + 10.dp2px) * index
                val startY = rssiMaxHeight / (Level.HIGH.value + 1) * (Level.HIGH.value - index)
                paint.style = if (index <= level.value) {
                    Paint.Style.FILL
                } else {
                    Paint.Style.STROKE
                }
                canvas.drawLine(startX, startY, startX, rssiMaxHeight, paint)
            }
        }
        paint.style = Paint.Style.STROKE
        canvas.drawLine(
            0f, 0f, width.toFloat(), 16.dp2px, paint
        )
    }

    enum class Level(val value: Int) {
        LOW(0), MID(1), HIGH(2)
    }
}