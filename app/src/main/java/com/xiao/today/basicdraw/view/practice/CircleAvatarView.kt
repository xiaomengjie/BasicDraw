package com.xiao.today.basicdraw.view.practice

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.View
import com.xiao.today.basicdraw.R
import com.xiao.today.basicdraw.dp2px
import com.xiao.today.basicdraw.loadAvatar

class CircleAvatarView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val xfermode = PorterDuffXfermode(
        PorterDuff.Mode.SRC_IN
    )

    /*黑边宽度*/
    private val sideWidth = 16.dp2px

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制黑底
        canvas.drawOval(0f, (height - width) / 2f, width.toFloat(), (height + width) / 2f, paint)
        //离屏缓冲
        val saveLayer = canvas.saveLayer(
            0f,
            (height - width) / 2f,
            width.toFloat(),
            (height + width) / 2f,
            paint
        )
        canvas.drawOval(sideWidth, (height - width) / 2f + sideWidth, width.toFloat() - sideWidth, (height + width) / 2f - sideWidth, paint)
        val avatar = context.loadAvatar(R.drawable.avatar, (width - sideWidth * 2).toInt())
        paint.xfermode = xfermode
        canvas.drawBitmap(avatar, sideWidth, (height - width) / 2f + sideWidth, paint)
        paint.xfermode = null
        canvas.restoreToCount(saveLayer)
    }
}