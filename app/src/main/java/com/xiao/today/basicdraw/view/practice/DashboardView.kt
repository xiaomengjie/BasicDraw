package com.xiao.today.basicdraw.view.practice

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathDashPathEffect
import android.graphics.PathMeasure
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.xiao.today.basicdraw.dp2px
import com.xiao.today.basicdraw.sp2px
import kotlin.math.cos
import kotlin.math.sin

/**
 * 仪表盘
 */
class DashboardView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2.dp2px
    }

    private val rectF by lazy {
        RectF(
            0f,
            (height - width) / 2f,
            width / 1f,
            (height + width) / 2f
        )
    }

    private val dashPath by lazy {
        Path().apply {
            addArc(rectF, 135f, 270f)
        }
    }

    private val pathDashPathEffect by lazy {
        val advance = (pathMeasure.length - 4.dp2px) / 10
        PathDashPathEffect(
            Path().apply {
                addRect(0f, 0f, 4.dp2px, width/20f, Path.Direction.CW)
            }, advance, 0f, PathDashPathEffect.Style.ROTATE
        )
    }

    private val pathMeasure by lazy {
        PathMeasure(dashPath, false)
    }

    private var currentSpeed = 0
        set(value) {
            field = value
            invalidate()
        }

    private val animation = ObjectAnimator.ofInt(this, "currentSpeed", 0, 10, 0).apply {
        interpolator = LinearInterpolator()
        duration = 20_000
        startDelay = 1_000
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.RESTART
    }

    private val pointerLength by lazy {
        width/2f * 0.8f
    }

    init {
        animation.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.CYAN)
        paint.color = Color.BLACK
        canvas.drawArc(rectF, 135f, 270f, false, paint)
        paint.pathEffect = pathDashPathEffect
        canvas.drawPath(dashPath, paint)
        paint.pathEffect = null
        //中间线
        canvas.drawLine(
            width / 2f, height / 2f,
            width / 2f + pointerLength * cos(Math.toRadians((START_ANGLE + SWEEP_ANGLE / 10 * currentSpeed).toDouble())).toFloat(),
            height / 2f + pointerLength * sin(Math.toRadians((START_ANGLE + SWEEP_ANGLE / 10 * currentSpeed).toDouble())).toFloat(),
            paint
        )
        //左边线
        canvas.drawLine(
            width / 2f + pointerLength * cos(Math.toRadians((START_ANGLE + SWEEP_ANGLE / 10 * currentSpeed).toDouble())).toFloat(),
            height / 2f + pointerLength * sin(Math.toRadians((START_ANGLE + SWEEP_ANGLE / 10 * currentSpeed).toDouble())).toFloat(),
            width / 2f + (pointerLength - 16.dp2px) * cos(Math.toRadians((START_ANGLE + SWEEP_ANGLE / 10 * currentSpeed - 5).toDouble())).toFloat(),
            height / 2f + (pointerLength - 16.dp2px) * sin(Math.toRadians((START_ANGLE + SWEEP_ANGLE / 10 * currentSpeed - 5).toDouble())).toFloat(),
            paint
        )
        //右边线
        canvas.drawLine(
            width / 2f + pointerLength * cos(Math.toRadians((START_ANGLE + SWEEP_ANGLE / 10 * currentSpeed).toDouble())).toFloat(),
            height / 2f + pointerLength * sin(Math.toRadians((START_ANGLE + SWEEP_ANGLE / 10 * currentSpeed).toDouble())).toFloat(),
            width / 2f + (pointerLength - 16.dp2px) * cos(Math.toRadians((START_ANGLE + SWEEP_ANGLE / 10 * currentSpeed + 5).toDouble())).toFloat(),
            height / 2f + (pointerLength - 16.dp2px) * sin(Math.toRadians((START_ANGLE + SWEEP_ANGLE / 10 * currentSpeed + 5).toDouble())).toFloat(),
            paint
        )
        // TODO: 画文字，待优化
        paint.textSize = 18.sp2px
        paint.color = Color.RED
        for (i in 0..10) {
            canvas.drawText(i.toString(),
                width / 2f + (pointerLength + width/20f) * cos(Math.toRadians((START_ANGLE + SWEEP_ANGLE / 10 * i).toDouble())).toFloat(),
                height / 2f + (pointerLength + width/20f) * sin(Math.toRadians((START_ANGLE + SWEEP_ANGLE / 10 * i).toDouble())).toFloat(),
                paint)

        }
    }
}

private const val START_ANGLE = 135f
private const val SWEEP_ANGLE = 270f
