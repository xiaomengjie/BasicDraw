package com.xiao.today.basicdraw.drawable

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import com.xiao.today.basicdraw.dp2px

class CircleDrawable: Drawable() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(bounds.left + intrinsicWidth/2f, bounds.top + intrinsicHeight/2f, 100f.dp2px, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun getIntrinsicHeight(): Int {
        return bounds.height()
    }

    override fun getIntrinsicWidth(): Int {
        return bounds.width()
    }
}