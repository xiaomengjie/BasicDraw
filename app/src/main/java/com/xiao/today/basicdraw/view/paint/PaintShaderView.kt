package com.xiao.today.basicdraw.view.paint

import android.content.Context
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View
import com.xiao.today.basicdraw.R
import com.xiao.today.basicdraw.dp2px
import com.xiao.today.basicdraw.loadAvatar

/* 着色器 */
class PaintShaderView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var linearGradient: LinearGradient? = null

    private var radialGradient: RadialGradient? = null

    private var sweepGradient: SweepGradient? = null

    private var bitmapShader: BitmapShader? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        linearGradient = LinearGradient(
            0f,
            (height - width) / 2f,
            width.toFloat(),
            (height + width) / 2f,
            Color.RED,
            Color.GREEN,
            Shader.TileMode.REPEAT
        )

        radialGradient = RadialGradient(
            width / 2f, height / 2f, width / 2f,
            Color.RED,
            Color.GREEN,
            Shader.TileMode.REPEAT
        )

        sweepGradient = SweepGradient(
            width / 2f, height / 2f,
            Color.RED,
            Color.GREEN,
        )

        bitmapShader = BitmapShader(
            context.loadAvatar(R.drawable.zdf1, width/4),
            Shader.TileMode.MIRROR,
            Shader.TileMode.MIRROR
        )
    }

    /*
    * 着色规则：
    * Shader.TileMode.CLAMP：端点之外延续端点颜色
    * Shader.TileMode.MIRROR：镜像模式
    * Shader.TileMode.REPEAT：重复模式
    * */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /* LinearGradient：线性渐变
        * x0 y0 x1 y1 ：渐变的两个端点的位置
        * color0 color1：端点的颜色
        * tile：端点范围之外的着色规则
        *  */
        paint.shader = linearGradient

        /* RadialGradient：辐射渐变
        * centerX centerY ：辐射中心的坐标
        * radius ：辐射半径
        * centerColor ：辐射中心的颜色
        * edgeColor ：辐射边缘的颜色
        * tileMode ：辐射范围之外的着色模式
        *  */
        paint.shader = radialGradient

        /* SweepGradient：扫描渐变
        * cx cy ：扫描的中心
        * color0 ：扫描的起始颜色
        * color1 ：扫描的终止颜色
        * */
        paint.shader = sweepGradient

        canvas.drawRect(
            0f, 0f, width.toFloat(), (height - width) / 2f - 1.dp2px, paint
        )
        canvas.drawRect(
            0f,
            (height - width) / 2f,
            width.toFloat(),
            (height + width) / 2f, paint
        )
        canvas.drawRect(
            0f, (height + width) / 2f + 1.dp2px, width.toFloat(), height.toFloat(), paint
        )

        /* BitmapShader：bitmap着色器
        * bitmap：用来做模板的 Bitmap 对象
        * tileX：横向的 TileMode
        * tileY：纵向的 TileMode
        * */
        paint.shader = bitmapShader
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }
}