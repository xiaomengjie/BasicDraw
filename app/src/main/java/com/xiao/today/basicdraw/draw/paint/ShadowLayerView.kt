package com.xiao.today.basicdraw.draw.paint

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.xiao.today.basicdraw.dp2px

/*
* 绘制内容下，添加阴影
* */
class ShadowLayerView(context: Context, attrs: AttributeSet): View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val radius = 50.dp2px

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.BLACK)
        paint.style = Paint.Style.FILL
        /*
        * setShadowLayer：设置阴影
        * radius：阴影的模糊范围
        * dx，dy：阴影的偏移量
        * style == Paint.Style.STROKE，strokeWidth == 0时，没有阴影
        * */
        paint.setShadowLayer(100f, 0f, 0f, Color.RED)
        /*
        * 阴影半径（可能是这么算的，不确定）
        * float Blur::convertRadiusToSigma(float radius) {
        *   return radius > 0 ? 0.57735f * radius + 0.5f : 0.0f;
        * }
        * */
        val shadowWidth = 0.57735f * 100f + 0.5f
        canvas.drawRect(
            width/2f - radius,
            height/2f - radius,
            width/2f + radius,
            height/2f + radius,
            paint)
        paint.style = Paint.Style.STROKE
        paint.color = Color.WHITE
        canvas.drawRect(
            width/2f - radius - shadowWidth,
            height/2f - radius - shadowWidth,
            width/2f + radius + shadowWidth,
            height/2f + radius + shadowWidth,
            paint)
        /*
        * clearShadowLayer：清除阴影
        * */
        paint.clearShadowLayer()
    }
}