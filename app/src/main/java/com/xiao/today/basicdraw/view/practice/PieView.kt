package com.xiao.today.basicdraw.view.practice

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.xiao.today.basicdraw.dp2px
import com.xiao.today.basicdraw.offsetX
import com.xiao.today.basicdraw.offsetY

/**
 * 饼图
 */
class PieView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val sweepAngles = floatArrayOf(60f, 90f, 150f, 60f)
    private val colors = intArrayOf(
        Color.parseColor("#2879fe"),
        Color.parseColor("#c2175b"),
        Color.parseColor("#009788"),
        Color.parseColor("#ff8e01")
    )

    private val padding = 40f.dp2px

    private val offsetLength = 16f.dp2px

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val rectF = RectF()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectF.left = padding
        rectF.top = (height - width) / 2f + padding
        rectF.right = width - padding
        rectF.bottom = (height + width) / 2f - padding
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var startAngle = 0f
        sweepAngles.forEachIndexed { index, angle ->
            paint.color = colors[index]
            if (index == sweepAngles.lastIndex - 1) {
                canvas.save()
                val offsetAngle = startAngle + angle / 2
                canvas.translate(
                    offsetLength.offsetX(offsetAngle),
                    offsetLength.offsetY(offsetAngle)
                )
            }
            canvas.drawArc(rectF, startAngle, angle, true, paint)
            if (index == sweepAngles.lastIndex - 1) canvas.restore()
            startAngle += angle
        }
    }
}