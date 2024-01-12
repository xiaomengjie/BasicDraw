package com.xiao.today.basicdraw.practice

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import com.xiao.today.basicdraw.dp2px
import kotlin.math.cos
import kotlin.math.sin

/*
* 五角星
* 外层5个点处于一个圆中
* 内层5个点处于一个圆中
* 相邻两点与中心形成的夹脚为36度
* */

//半径
private val BIG_RADIUS = 120.dp2px
private val SMALL_RADIUS = 60.dp2px

class PentacleView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val pentaclePath by lazy {
        Path().apply {
            var sweepAngle = 18.0
            val pointF = PointF()
            for (i in 0..9) {
                if (i % 2 == 0) {
                    pointF.x = (SMALL_RADIUS * cos(Math.toRadians(sweepAngle))).toFloat()
                    pointF.y = (SMALL_RADIUS * sin(Math.toRadians(sweepAngle))).toFloat()
                } else {
                    pointF.x = (BIG_RADIUS * cos(Math.toRadians(sweepAngle))).toFloat()
                    pointF.y = (BIG_RADIUS * sin(Math.toRadians(sweepAngle))).toFloat()
                }
                if (i == 0) moveTo(pointF.x, pointF.y) else lineTo(pointF.x, pointF.y)
                sweepAngle += 36
            }
            close()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = Color.parseColor("#f8e71d")
        canvas.drawColor(Color.parseColor("#4990e2"))
        canvas.save()
        canvas.translate(width / 2f, height / 2f)
        canvas.drawPath(pentaclePath, paint)
        canvas.restore()
    }
}