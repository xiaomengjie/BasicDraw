package com.xiao.today.basicdraw.view.paint

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.LightingColorFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.view.View

class ColorFilterView(context: Context, attrs: AttributeSet): View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /*
    * 根据规则计算出新的RGB颜色
    * R' = R * colorMultiply.R + colorAdd.R
    * G' = G * colorMultiply.G + colorAdd.G
    * B' = B * colorMultiply.B + colorAdd.B
    *
    * 0xFFFFFF, 0x000000：颜色不变
    * 0x00FFFF, 0x000000：去除红色部分
    * 0xFF00FF, 0x000000：去除绿色部分
    * 0xFFFF00, 0x000000：去除蓝色部分
    *
    * */
    private val lightingColorFilter = LightingColorFilter(
        0xFF0000, 0x000000
    )

    /* 根据porter-duff mode规则，将color作为源 */
    private val porterDuffColorFilter = PorterDuffColorFilter(
        Color.RED, PorterDuff.Mode.SRC
    )

    /* 使用ColorMatrix进行颜色转换
    * ColorMatrix: 4 * 5的矩阵
    *
    *  [ a, b, c, d, e,
    *    f, g, h, i, j,
    *    k, l, m, n, o,
    *    p, q, r, s, t ]
    *
    * color[R, G, B, A] 转换为 color’[R’, G’, B’, A’]
    * R’ = a*R + b*G + c*B + d*A + e
    * G’ = f*R + g*G + h*B + i*A + j
    * B’ = k*R + l*G + m*B + n*A + o
    * A’ = p*R + q*G + r*B + s*A + t
    *  */

    //留红色
    private val colorMatrixColorFilter = ColorMatrixColorFilter(
        ColorMatrix(
            floatArrayOf(
                0f, 0f, 0f, 0f, 255f,
                0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 255f
            )
        )
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = Color.WHITE
        paint.colorFilter = lightingColorFilter
        paint.colorFilter = porterDuffColorFilter
        paint.colorFilter = colorMatrixColorFilter
        canvas.drawCircle(width/2f, height/2f, width/2f, paint)
    }
}