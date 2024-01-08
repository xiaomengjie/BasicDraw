package com.xiao.today.basicdraw.view.text

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.xiao.today.basicdraw.R
import com.xiao.today.basicdraw.dp2px

/*
* 文字大小单位：dp 和 sp 区别
* dp：取决于手机的像素密度，保证不同像素密度的手机同样的dp值显示效果相当
* sp：取决于手机的像素密度以及系统字体大小
* （适合阅读类型文本 - 新闻内容等）
* */
class TextLocationView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        /*
        * typeface：宋体，微软雅黑等
        * font：粗宋体，斜宋体
        * */
        typeface = ResourcesCompat.getFont(context, R.font.font) // 字体设置
        //需要api 26
//        typeface = resources.getFont( R.font.font)

        textSize = 100f.dp2px // 文字大小

        textAlign = Paint.Align.CENTER // 对齐方式
    }

    private val textBounds = Rect()

    private val fontMetrics = Paint.FontMetrics()

    private val dashPathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.style = Paint.Style.STROKE
        canvas.drawCircle(width / 2f, height / 2f, width / 2f, paint)

        paint.style = Paint.Style.FILL
        val baseline = height / 2f
        /*
        * x = 根据textAlign值，为左、中、右文字位置
        * y = 文字baseline位置
        * */
        canvas.drawText(CONTENT, width / 2f, baseline, paint)
        /*
        * getTextBounds：获取文字的显示范围
        * 左右值：相对于文字的起始位置
        * 上下值：相对于文字baseline位置
        * */
        paint.getTextBounds(CONTENT, 0, CONTENT.length, textBounds)

        paint.style = Paint.Style.STROKE
        canvas.drawRect(
            (width - textBounds.width()) / 2f,
            baseline + textBounds.top,
            (width + textBounds.width()) / 2f,
            baseline + textBounds.bottom, paint
        )

        paint.pathEffect = dashPathEffect
        canvas.drawLine(
            (width - textBounds.width()) / 2f - textBounds.left,
            0f,
            (width - textBounds.width()) / 2f - textBounds.left,
            height / 1f,
            paint
        )
        canvas.drawLine(0f, height / 2f, width / 1f, height / 2f, paint)

        println(textBounds.left)
        println(textBounds.top)
        println(textBounds.right)
        println(textBounds.bottom)
        /*
        * System.out I 8
        * System.out I -207
        * System.out I 486
        * System.out I 56
        * */

        paint.style = Paint.Style.FILL
        paint.pathEffect = dashPathEffect
        canvas.drawText(CONTENT, width/2f, height/2f, paint)
        /*
        * getFontMetrics：不同类型文字相对于baseline的ascent , descent , top , bottom , leading值
        * 不受文字内容影响，只和字体类型有关
        * */
        paint.getFontMetrics(fontMetrics)
        canvas.drawLine(
            0f,
            baseline + fontMetrics.top,
            width / 1f,
            baseline + fontMetrics.top,
            paint
        )
        canvas.drawLine(
            0f,
            baseline + fontMetrics.ascent,
            width / 1f,
            baseline + fontMetrics.ascent,
            paint
        )
        canvas.drawLine(
            0f,
            baseline + fontMetrics.descent,
            width / 1f,
            baseline + fontMetrics.descent,
            paint
        )
        canvas.drawLine(
            0f,
            baseline + fontMetrics.bottom,
            width / 1f,
            baseline + fontMetrics.bottom,
            paint
        )
        println(fontMetrics.top)
        println(fontMetrics.ascent)
        println(fontMetrics.descent)
        println(fontMetrics.bottom)
        /*
        * System.out               I  -311.575
        * System.out               I  -275.0
        * System.out               I  68.75
        * System.out               I  59.4
        * */
    }

    private val CONTENT = "abp"
}