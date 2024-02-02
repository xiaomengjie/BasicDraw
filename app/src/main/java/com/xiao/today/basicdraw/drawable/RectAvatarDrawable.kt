package com.xiao.today.basicdraw.drawable

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.drawable.Drawable
import com.xiao.today.basicdraw.dp2px

class RectAvatarDrawable(
    private val bitmap: Bitmap,
    private val rx: Float = 8f.dp2px,
    private val ry: Float = 8f.dp2px
) : Drawable() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val rect = RectF()

    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    override fun draw(canvas: Canvas) {
        val saveLayer = canvas.saveLayer(rect, null)
        canvas.drawRoundRect(rect, rx, ry, paint)
        paint.xfermode = xfermode
        canvas.drawBitmap(compressBitmap(bitmap), 0f, 0f, paint)
        paint.xfermode = null
        canvas.restoreToCount(saveLayer)
    }

    private fun compressBitmap(bitmap: Bitmap): Bitmap{
        return Bitmap.createScaledBitmap(bitmap, bounds.width(), bounds.width(), true)
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        rect.left = left.toFloat()
        rect.top = top.toFloat()
        rect.right = right.toFloat()
        rect.bottom = bottom.toFloat()
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getAlpha(): Int {
        return paint.alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getColorFilter(): ColorFilter? {
        return paint.colorFilter
    }

    /**
     * 返回值：
     * PixelFormat.UNKNOWN：不确定？？？
     * PixelFormat.TRANSPARENT：透明（显示下面的内容）
     * PixelFormat.TRANSLUCENT：半透明（下面内容一部分显示）
     * PixelFormat.OPAQUE：不透明，覆盖drawable下面的内容
     *
     * 不知道怎么用----------->用PixelFormat.TRANSLUCENT
     */
    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("PixelFormat.OPAQUE", "android.graphics.PixelFormat")
    )
    override fun getOpacity(): Int {
        // not sure, so be safe
        return PixelFormat.TRANSLUCENT;
    }
}