package com.xiao.today.basicdraw.view.text

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetrics
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.xiao.today.basicdraw.dp2px

class MeasureTextView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 20.dp2px
    }

    private val fontMetrics = FontMetrics()

    private val textBounds = Rect()

    private val widths = FloatArray(TEXT.length)

    private val measuredWidth = floatArrayOf(0f)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /*
        * Paint.getFontSpacing：推荐行距（系统推荐的两行文字的baseline的距离）
        * 系统根据文字字体和字号自动计算
        * */
        canvas.drawText("abp", 100f, 150f, paint)
        canvas.drawText("abp", 100f, 150f + paint.fontSpacing, paint)
        canvas.drawText("abp", 100f, 150f + paint.fontSpacing * 2, paint)

        /*
        * Paint.getFontMetrics：获得文字排印方面的数值
        * baseline：文字基准线，绘制文字时的y值
        * ascent：限制普通文字的顶部。值为距离baseline的相对距离，负值
        * descent：限制普通文字的底部。值为距离baseline的相对距离，正值
        * top：限制所有文字的顶部（所有文字上不会超过top）。值为相对baseline的相对距离，负值
        * bottom：限制所有文字的底部（所有文字下不会超过bottom）。值为相对baseline的相对距离，正值
        * leading：上行的bottom与下行的top之间的距离。绝对值
        * */
        paint.getFontMetrics(fontMetrics)
        println("fontMetrics.top = ${fontMetrics.top}")
        println("fontMetrics.ascent = ${fontMetrics.ascent}")
        println("fontMetrics.descent = ${fontMetrics.descent}")
        println("fontMetrics.bottom = ${fontMetrics.bottom}")
        println("fontMetrics.leading = ${fontMetrics.leading}")

        /*
        * getFontSpacing：能使两行文字在不显示拥挤的前提下缩短行距。
        * 一般换行绘制时使用fontSpacing（简单，效果好）
        * */
        println(
            "paint.getFontSpacing = ${paint.fontSpacing}, " +
                    "bottom - top + leading = ${fontMetrics.bottom - fontMetrics.top + fontMetrics.leading}"
        )

        /*
        * Paint.getTextBounds：获取文字的显示范围（相对于绘制点（x，y）的距离）(显示宽度)
        * */
        val x = width / 2f
        val y = height / 2f
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(TEXT, x, y, paint)
        paint.getTextBounds(TEXT, 0, TEXT.length, textBounds)
        textBounds.left += x.toInt()
        textBounds.top += y.toInt()
        textBounds.right += x.toInt()
        textBounds.bottom += y.toInt()
        paint.style = Paint.Style.STROKE
        canvas.drawRect(textBounds, paint)

        /*
        * Paint.measureText：测量文字的宽度并返回（实际占用宽度）
        * 会比getTextBounds得到的宽度大一点
        * */
        val measureText = paint.measureText(TEXT)
        println("measureText = $measureText")
        println("textBoundsWidth = ${textBounds.width()}")

        /*
        * Paint.getTextWidths：得到每个文字的宽度
        * 相当于每个字符调用measureText，计算结果存入Float数组widths中
        * */
        paint.getTextWidths(TEXT, widths)
        println("widths.sum() = ${widths.sum()}")

        /*
        * Paint.breakText：测量文字在最大宽度下的显示数
        * text：要测量的文字
        * start，end：测量文字范围
        * measureForwards：向前测量（从左到右）
        * maxWidth：最大宽度
        * measureWidth：可显示文字实际占用宽度（长度为1的Float数组）
        * */
        val breakText = paint.breakText(TEXT, 0, TEXT.length, true, 100f, measuredWidth)
        println("breakText = $breakText")
        println("measuredWidth[0] = ${measuredWidth[0]}")

        /*
        * Paint.getRunAdvance：对于一段文字，计算某个字符的距离
        * text：文字
        * start，end：文字范围
        * contextStart，contextEnd：上下文范围（？？？）
        * isRtl：文字的方向是否从右到左
        * offset：字数偏移（计算第几个字符）
        *
        * 0 <= contextStart <= start <= offset <= end <= contextEnd <= text.length
        *
        * TEXT, 0, TEXT.length, 0, TEXT.length, false, TEXT.length，就是测量整个文字的宽度
        * */
        val runAdvance = paint.getRunAdvance(
            TEXT, 0, TEXT.length, 0, TEXT.length, false, TEXT.length
        )
        println("runAdvance = $runAdvance")

        /*
        * Paint.getOffsetForAdvance：给出一个位置的像素值，计算出文字中最接近这个文字的偏移量
        * 即：文字中的第几个字符最接近给出的坐标位置
        *
        * getOffsetForAdvance() 配合上 getRunAdvance() 一起使用，就可以实现「获取用户点击处的文字坐标」的需求
        *
        * */
        val offsetForAdvance = paint.getOffsetForAdvance(
            TEXT, 0, TEXT.length, 0, TEXT.length, false, 275f
        )
        println("offsetForAdvance = $offsetForAdvance")

        /*
        * Paint.hasGlyph：检查字符串是否为单独字形
        * */
        println(paint.hasGlyph("a"))
    }

    companion object {
        private const val TEXT = "今天星期天"
    }
}