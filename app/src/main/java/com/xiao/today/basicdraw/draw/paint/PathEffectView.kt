package com.xiao.today.basicdraw.draw.paint

import android.content.Context
import android.graphics.Canvas
import android.graphics.ComposePathEffect
import android.graphics.CornerPathEffect
import android.graphics.DashPathEffect
import android.graphics.DiscretePathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathDashPathEffect
import android.graphics.SumPathEffect
import android.util.AttributeSet
import android.view.View
import com.xiao.today.basicdraw.dp2px

/*
* 给图形轮廓设置效果
* */
class PathEffectView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /*
    * CornerPathEffect：将拐角变为圆角
    * radius：圆角半径
    *  */
    private val cornerPathEffect = CornerPathEffect(40.dp2px)

    /*
    * DiscretePathEffect：使用定长线条拼接，并进行随机偏离
    * segmentLength：拼接的每个线段长度
    * deviation：偏离量
    * */
    private val discretePathEffect = DiscretePathEffect(
        10.dp2px, 5.dp2px
    )

    /*
    * DashPathEffect：使用虚线绘制线条
    * intervals：float数组，指定虚线格式（线长、空白长、线长、空白长。。。）
    * phase：虚线偏移量
    * */
    private val dashPathEffect = DashPathEffect(
        floatArrayOf(10.dp2px, 5.dp2px), 0f
    )

    private val path = Path().apply {
        rLineTo(20.dp2px, 0f)
        rLineTo((-20).dp2px, 10.dp2px)
        close()
    }

    /*
       * PathDashPathEffect：使用path绘制线条
       * shape：用于绘制的path（path都会被填充）
       * advance：两段之间起点到起点的距离
       * phase：绘制前的偏移量
       * style：拐角方式
       *    PathDashPathEffect.Style.TRANSLATE：位移
       *    PathDashPathEffect.Style.ROTATE：旋转
       *    PathDashPathEffect.Style.MORPH：变体
       * */
    private val pathDashPathEffect = PathDashPathEffect(
        path, 40.dp2px, 0f, PathDashPathEffect.Style.MORPH
    )

    /*
    * SumPathEffect：按照两种effect分别绘制
    * */
    private val sumPathEffect = SumPathEffect(
        cornerPathEffect, dashPathEffect
    )

    /*
    * ComposePathEffect：先使用innerpe绘制，在应用outerpe改变
    * */
    private val composePathEffect = ComposePathEffect(
        dashPathEffect, cornerPathEffect
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.style = Paint.Style.STROKE

        paint.pathEffect = cornerPathEffect

        paint.pathEffect = discretePathEffect

        paint.pathEffect = dashPathEffect

        paint.pathEffect = pathDashPathEffect

        paint.pathEffect = sumPathEffect

        paint.pathEffect = composePathEffect

        canvas.drawRect(
            10.dp2px, (height - width) / 2f + 10.dp2px,
            width - 10.dp2px, (height + width) / 2f - 10.dp2px, paint
        )
    }
}