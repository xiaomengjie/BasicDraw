package com.xiao.today.basicdraw.practice

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.getDrawableOrThrow
import androidx.core.graphics.drawable.toBitmap
import com.xiao.today.basicdraw.R
import com.xiao.today.basicdraw.dp2px

class CircleAvatarView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    /*黑边宽度*/
    private val sideWidth: Int

    private val drawable: Drawable

    private val radius = 30f.dp2px

    init {
        val obtainStyledAttributes =
            context.obtainStyledAttributes(attrs, R.styleable.CircleAvatarView)
        sideWidth =
            obtainStyledAttributes.getDimensionPixelSize(R.styleable.CircleAvatarView_sideWidth, 0)
        drawable = obtainStyledAttributes.getDrawableOrThrow(R.styleable.CircleAvatarView_avatar)
        obtainStyledAttributes.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSize(((radius + sideWidth) * 2).toInt(), widthMeasureSpec)
        val height = resolveSize(((radius + sideWidth) * 2).toInt(), heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制黑底
        canvas.drawOval(0f, 0f, width.toFloat(), height.toFloat(), paint)
        //离屏缓冲
        val saveLayer = canvas.saveLayer(
            sideWidth.toFloat(),
            sideWidth.toFloat(),
            width.toFloat() - sideWidth,
            height.toFloat() - sideWidth,
            paint
        )
        //destination
        canvas.drawOval(
            sideWidth.toFloat(),
            sideWidth.toFloat(),
            width.toFloat() - sideWidth,
            height.toFloat() - sideWidth,
            paint
        )
        paint.xfermode = xfermode
        val bitmap = drawable.toBitmap(width - sideWidth * 2, height - sideWidth * 2)
        //source
        canvas.drawBitmap(bitmap, sideWidth.toFloat(), sideWidth.toFloat(), paint)
        paint.xfermode = null
        canvas.restoreToCount(saveLayer)
    }
}