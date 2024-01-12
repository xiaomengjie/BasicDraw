package com.xiao.today.basicdraw.practice

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.xiao.today.basicdraw.dp2px
import java.text.DecimalFormat
import kotlin.math.roundToInt

class SlidingTapeView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val tapeHeight = 160f.dp2px

    private val lineWidth = 4f.dp2px

    var tapeWeight: Float = 65.5f
        set(value) {
            field = value
            invalidate()
        }

    private val textBounds = Rect()
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        paint.style = Paint.Style.FILL
//        paint.color = Color.parseColor("#f7f9f6")
//        canvas.drawRect(
//            0f, (height - tapeHeight) / 2f, width.toFloat(), (height + tapeHeight) / 2, paint
//        )
//        paint.color = Color.parseColor("#e8e9e7")
//        paint.strokeWidth = 2f.dp2px
//        canvas.drawLine(
//            0f, (height - tapeHeight) / 2f, width.toFloat(), (height - tapeHeight) / 2f, paint
//        )
//
//        paint.textSize = 20f.dp2px
//        paint.textAlign = Paint.Align.CENTER
//        val top = (height - tapeHeight) / 2f
//        for (index in 0 until 32) {
//            paint.color = Color.parseColor("#e8e9e7")
//            val startX = index * (width - lineWidth) / 32
//            if (index % 10 == 1){
//                canvas.drawRect(startX, top, startX + lineWidth, top + 2 * tapeHeight / 5, paint)
//                paint.color = Color.BLACK
//                canvas.drawText("64", startX + lineWidth / 2f, top + 2 * tapeHeight / 5 + 40f.dp2px, paint)
//            }else{
//                canvas.drawRect(startX, top, startX + lineWidth, top + tapeHeight / 5, paint)
//            }
//        }
//
//        paint.strokeWidth = 4f.dp2px
//        paint.strokeCap = Paint.Cap.ROUND
//        paint.color = Color.parseColor("#69b87b")
//        canvas.drawLine(
//            width / 2f,
//            (height - tapeHeight) / 2f,
//            width / 2f,
//            (height - tapeHeight) / 2f + 2 * tapeHeight / 5,
//            paint
//        )
//        paint.textAlign = Paint.Align.CENTER
//        paint.textSize = 40f.dp2px
//        paint.pathEffect = null
//        canvas.drawText(
//            weight.toString(),
//            width / 2f,
//            (height - tapeHeight) / 2f - 40f.dp2px,
//            paint
//        )
//        paint.getTextBounds(weight.toString(), 0, weight.toString().length, textBounds)
//        paint.textSize = 20f.dp2px
//        canvas.drawText(
//            "kg",
//            width / 2f + textBounds.width(),
//            (height - tapeHeight) / 2f - 40f.dp2px + textBounds.top,
//            paint
//        )

        testAnimator(canvas)
    }

    private val scaleList = mutableListOf<Scale>()

    private val animator = ObjectAnimator.ofFloat(
        this, "tapeWeight",  65.5f, 68f
    ).apply {
        duration = 1_000
        startDelay = 1_000
    }

    init {
        animator.start()
    }

    private val decimalFormat = DecimalFormat("0.0")

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (scaleList.isEmpty()){
            for (index in 0 until 32) {
                val scale = Scale()
                if (index == 0){
                    scale.weight = tapeWeight
                    scale.rectF.left = 0f
                    scale.rectF.top = 10f.dp2px
                    scale.rectF.right = scale.rectF.left + lineWidth
                    scale.rectF.bottom = scale.rectF.top +
                            (if (scale.weight % 1 == 0f) 2 * tapeHeight / 5 else tapeHeight / 5)
                }else{
                    val pre = scaleList[index - 1]
                    scale.weight = decimalFormat.format(pre.weight + 0.1f).toFloat()
                    scale.rectF.left = pre.rectF.left + (width - lineWidth) / 32
                    scale.rectF.top = pre.rectF.top
                    scale.rectF.right = scale.rectF.left + lineWidth
                    scale.rectF.bottom = scale.rectF.top +
                            (if (scale.weight % 1 == 0f) 2 * tapeHeight / 5 else tapeHeight / 5)
                }
                println(scale.weight)
                scaleList.add(index, scale)
            }
        }

    }
    private fun testAnimator(canvas: Canvas){
        paint.color = Color.BLACK
        paint.textSize = 20f.dp2px
        paint.textAlign = Paint.Align.CENTER
        for (index in 0 until 32) {
            val scale = scaleList[index]
            if (index == 0){
                scale.weight = tapeWeight
                scale.rectF.left = 0f
                scale.rectF.top = 10f.dp2px
                scale.rectF.right = scale.rectF.left + lineWidth
                scale.rectF.bottom = scale.rectF.top +
                        (if (scale.weight % 1 == 0f) 2 * tapeHeight / 5 else tapeHeight / 5)
            }else{
                val pre = scaleList[index - 1]
                scale.weight = decimalFormat.format(pre.weight + 0.1f).toFloat()
                scale.rectF.left = pre.rectF.left + (width - lineWidth) / 32
                scale.rectF.top = pre.rectF.top
                scale.rectF.right = scale.rectF.left + lineWidth
                scale.rectF.bottom = scale.rectF.top +
                        (if (scale.weight % 1 == 0f) 2 * tapeHeight / 5 else tapeHeight / 5)
            }
        }
        scaleList.forEach{
            canvas.drawRect(it.rectF, paint)
            if (it.weight % 1 == 0f){
                canvas.drawText(it.weight.roundToInt().toString(), it.rectF.left, it.rectF.bottom + 40f.dp2px, paint)
            }
        }
    }

    /* 刻度 */
    data class Scale(
        val rectF: RectF = RectF(0f, 0f, 0f, 0f),
        var weight: Float = 0f
    )

}