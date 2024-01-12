package com.xiao.today.basicdraw.draw.drawsequence

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.xiao.today.basicdraw.dp2px

class ShowSizeImageView(context: Context, attrs: AttributeSet): AppCompatImageView(context, attrs) {

    private val debug: Boolean = true

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 20f.dp2px
        color = Color.RED
    }
    override fun onDraw(canvas: Canvas) {
        /*
        * 绘制在super.onDraw(canvas)上方，在原有内容之前绘制
        * 会被盖住
        *
        * 例子：在文字下层绘制纯色矩形来作为强调
        * */
        super.onDraw(canvas)
        /*
        * 继承已有控件，绘制在super.onDraw(canvas)的下方
        * 显示的内容在原先控件内容的上面
        *
        * 例子：调试模式下，在图片左上角显示图片大小
        * */
        val text = "尺寸: ${drawable.intrinsicWidth} * ${drawable.intrinsicHeight}"
        if (debug){
            canvas.drawText(
                text, 0, text.length,
                0f, -paint.fontMetrics.top,
                paint
            )
        }
    }
}