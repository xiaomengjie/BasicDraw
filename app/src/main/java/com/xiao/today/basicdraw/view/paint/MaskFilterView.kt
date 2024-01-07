package com.xiao.today.basicdraw.view.paint

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withSave
import com.xiao.today.basicdraw.R
import com.xiao.today.basicdraw.dp2px
import com.xiao.today.basicdraw.loadAvatar

class MaskFilterView(context: Context, attrs: AttributeSet): View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val bitmapWidth = 200.dp2px

    private val bitmap = context.loadAvatar(R.drawable.avatar, bitmapWidth.toInt())

    private val blurMaskFilter = BlurMaskFilter(
        50f, BlurMaskFilter.Blur.OUTER
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /*
        * Paint.setMaskFilter：在绘制层上方附加效果
        * 1、BlurMaskFilter(float radius, Blur style)：模糊效果
        * radius：模糊的范围
        * style：模糊类型
        *   BlurMaskFilter.Blur.NORMAL：内外部都模糊绘制
        *   BlurMaskFilter.Blur.SOLID： 内部正常绘制，外部模糊
        *   BlurMaskFilter.Blur.INNER： 内部模糊，外部不绘制
        *   BlurMaskFilter.Blur.OUTER： 内部不绘制，外部模糊
        * */
        paint.maskFilter = blurMaskFilter
        drawCenterBitmap(bitmap, canvas, paint)
    }
}

private fun drawCenterBitmap(bitmap: Bitmap, canvas: Canvas, paint: Paint) {
    canvas.withSave {
        canvas.translate((width - bitmap.width) / 2f, (height - bitmap.height) / 2f)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }
}