package com.xiao.today.basicdraw.practice

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.toColorInt
import androidx.core.graphics.withSave
import com.xiao.today.basicdraw.dp2px

class BatteryView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = "#99ffffff".toColorInt()
        textSize = 24.dp2px.toFloat()
    }

    private val strokeWidth =  4.dp2px.toFloat()
    private val radius =  2.dp2px.toFloat()

    var batteryLevel = 0
        set(value) {
            field = value
            batteryText = "$value%"
            invalidate()
        }

    private var batteryText = "$batteryLevel%"

    private var batteryChipWidth = 28.dp2px
    private var batteryChipHeight = 12.dp2px

    private val rect = Rect()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSize(104.dp2px.toInt(), widthMeasureSpec)
        val height = resolveSize(30.dp2px.toInt(), heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.BLACK)
        canvas.withSave {
            paint.style = Paint.Style.FILL
            translate(24f, 24f)
            //绘制电池芯
            drawRect(0f, 0f, batteryChipWidth * batteryLevel / 100f, batteryChipHeight, paint)
            //绘制电池边框
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = strokeWidth
            drawRoundRect(-16f, -16f, batteryChipWidth + 16f, batteryChipHeight + 16f, radius, radius, paint)
            //绘制电池头
            paint.style = Paint.Style.FILL
            drawRect(batteryChipWidth + 24f, 0f, batteryChipWidth + 36f, batteryChipHeight, paint)
            //绘制电池文字
            paint.style = Paint.Style.FILL
            paint.getTextBounds(batteryText, 0, batteryText.length, rect)
            val offset = (rect.top + rect.bottom) / 2f
            drawText(batteryText, batteryChipWidth + 54f, batteryChipHeight / 2f - offset, paint)
        }
    }
}