package com.xiao.today.basicdraw.practice

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.addListener
import com.xiao.today.basicdraw.dp2px

@Deprecated("思路有问题")
class SlidingTapeView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val tapeHeight = 160f.dp2px

    private val textBounds = Rect()

    private val lines = mutableListOf<Line>()

    private var space: Float = 0f

    private var currentWeight: Float = 65.5f

    private var offset: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    init {
        val animator = ObjectAnimator.ofFloat(this, "offset", 2_000f)
        animator.duration = 1_000
        animator.startDelay = 1_000
        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        /* 卡尺上下值 */
        val top = (height - tapeHeight) / 2f
        val bottom = (height + tapeHeight) / 2

        /* 卡尺主体 */
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#f7f9f6")
        canvas.drawRect(0f, top, width.toFloat(), bottom, paint)

        /* 最上面横线 */
        paint.color = Color.BLACK
        paint.strokeWidth = LineWidth.SMALL.size
        canvas.drawLine(0f, top, width.toFloat(), top, paint)

        paint.textSize = TextSize.SMALL.size
        paint.textAlign = Paint.Align.CENTER
        var start = offset
        var weight = currentWeight
        while (start < width){
            val lineWidth: Float = drawScale(start, top, weight, canvas)
            start += (space + lineWidth)
            weight = "%.1f".format(weight + 0.1f).toFloat()
        }
        start = offset
        weight = currentWeight
        while (start > 0){
            val lineWidth: Float = drawScale(start, top, weight, canvas)
            start -= (space + lineWidth)
            weight = "%.1f".format(weight - 0.1f).toFloat()
        }

        /* 中间固定线 */
        paint.strokeWidth = LineWidth.LARGE.size
        paint.strokeCap = Paint.Cap.ROUND
        paint.color = Color.parseColor("#69b87b")
        val screenMiddle = width / 2f
        canvas.drawLine(
            screenMiddle, top, screenMiddle, top + tapeHeight / 2f, paint
        )
        /* 体重值 */
        paint.textSize = TextSize.LARGE.size
        paint.textAlign = Paint.Align.CENTER
        var drawWeight = currentWeight + 1.5f
        drawWeight = "%.1f".format(drawWeight - (offset / (space + LineWidth.MEDIUM.size)) * 0.1).toFloat()
        canvas.drawText(
            drawWeight.toString(), screenMiddle, top - paint.textSize, paint
        )
        /* 单位 */
        paint.getTextBounds(
            currentWeight.toString(), 0, currentWeight.toString().length, textBounds
        )
        paint.textSize = TextSize.SMALL.size
        paint.textAlign = Paint.Align.LEFT
        canvas.drawText(
            "kg",
            screenMiddle + textBounds.width() / 2 + textBounds.height() / 3,
            top - TextSize.LARGE.size - 2 * textBounds.height() / 3, paint
        )
    }

    private fun drawScale(start: Float, top: Float, weight: Float, canvas: Canvas): Float {
        val stopY: Float
        val lineWidth: Float
        if (weight % 1 == 0f) {
            stopY = top + tapeHeight / 2
            lineWidth = LineWidth.MEDIUM.size
            canvas.drawText(weight.toInt().toString(), start, stopY + paint.textSize, paint)
        } else {
            stopY = top + tapeHeight / 4
            lineWidth = LineWidth.MEDIUM.size
        }
        canvas.drawRect(
            start, top, start + lineWidth, stopY, paint
        )
        return lineWidth
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        lines.clear()
        var x = 0f
        var currentValue = 63.9f
        for (i in 0 .. 32) {
            val line = if (currentValue % 1 == 0f) {
                Line(LineWidth.MEDIUM, currentValue, x, tapeHeight / 2)
            } else {
                Line(LineWidth.SMALL, currentValue, x, tapeHeight / 4)
            }
            lines.add(line)
            currentValue = "%.1f".format(currentValue + 0.1f).toFloat()
            x += line.size.size
        }
        space = ((width - lines.sumOf { it.size.size.toDouble() }) / 32).toFloat()
        lines.forEachIndexed { index, line ->
            line.x += space * index
        }
    }

    enum class LineWidth(val size: Float) {
        SMALL(1f.dp2px), MEDIUM(2f.dp2px), LARGE(4f.dp2px)
    }

    enum class TextSize(val size: Float){
        SMALL(20f.dp2px), LARGE(40f.dp2px)
    }

    data class Line(var size: LineWidth, var value: Float, var x: Float, var height: Float)
}