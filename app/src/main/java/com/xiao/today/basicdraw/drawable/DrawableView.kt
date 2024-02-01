package com.xiao.today.basicdraw.drawable

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.xiao.today.basicdraw.R

class DrawableView(context: Context?, attrs: AttributeSet) : View(context, attrs) {

    private val circleDrawable = RectAvatarDrawable(
        BitmapFactory.decodeResource(resources, R.drawable.avatar)
    )

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(width/2f, height/2f, width/2f, paint)
        /**
         * drawable专注于上层绘制
         * 调用draw方法前需要使用setBounds设置绘制边界
         */
        circleDrawable.setBounds(0, 0, width/2, width/2)
        circleDrawable.draw(canvas)
    }
}