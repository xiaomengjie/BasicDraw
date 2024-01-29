package com.xiao.today.basicdraw.practice

import android.content.Context
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import com.xiao.today.basicdraw.R
import com.xiao.today.basicdraw.dp2px
import com.xiao.today.basicdraw.loadAvatar

class CircleImage(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val bitmap = context.loadAvatar(R.drawable.avatar, 100f.dp2px.toInt())

    private val bitmapShader = BitmapShader(
        bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT
    )

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        shader = bitmapShader
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(50f.dp2px, 50f.dp2px, 50f.dp2px, paint)
    }
}