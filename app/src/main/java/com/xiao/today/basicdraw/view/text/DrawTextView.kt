package com.xiao.today.basicdraw.view.text

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import java.util.Locale

/**
 * 绘制文字的基本方法：drawText
 * drawText() 方法参数中的 y 值，就是指定的基线 (baseline) 的位置
 * x值是第一个字符左边再左一点点的位置
 *
 * drawTextOnPath：沿着path绘制文字
 *
 * 多行文字快速绘制：StaticLayout
 * 碰到换行符自动换行，超过宽度上线自动换行
 *
 */
open class DrawTextView(context: Context, attrs: AttributeSet): View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val path = Path()

    private val textPaint = TextPaint().apply {
        textSize = 80f
    }

    private lateinit var staticLayout: StaticLayout

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        path.moveTo(0f, height/2f)
        path.lineTo(width.toFloat(), height/2f)

        staticLayout = StaticLayout.Builder.obtain(
            LONG_TEXT, 0, LONG_TEXT.length, textPaint, width
        ).build()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.textSize = 56f
        paint.style = Paint.Style.STROKE
        canvas.drawPath(path, paint)
        paint.style = Paint.Style.FILL
        /*
        * x值是第一个字符左边再左一点点的位置
        * y值就是指定的基线 (baseline) 的位置
        * */
        canvas.drawText(TEXT, width/2f, height/2f, paint)
        /*
        * hOffset 和 vOffset：是文字相对于 Path 的水平偏移量和竖直偏移量
        * */
        canvas.drawTextOnPath(TEXT, path, 0f, 0f, paint)

        /*
        * Paint.setStrikeThruText：设置删除线
        * */
        textPaint.isStrikeThruText = true

        /*
        * Paint.setUnderlineText：设置下划线
        * */
        textPaint.isUnderlineText = true

        /*
        * Paint.setTextSkewX：文字倾斜度
        * */
        textPaint.textSkewX = -0.5f

        /*
        * Paint.setTextScaleX：横向缩放（变胖变瘦）
        * */
        textPaint.textScaleX = 1.2f

        /*
        * Paint.setLetterSpacing：字间距里（默认为0）
        * */
        textPaint.letterSpacing = 0.2f

        /*
        * Paint.setFontFeatureSettings("")：用css的font-feature-settings 的方式来设置文字
        * 不明白。。。。。。。
        * */
        textPaint.reset()
        textPaint.textSize = 56f
        textPaint.fontFeatureSettings = "smcp"

        /*
        * Paint.setTextAlign：文字对齐方式
        * Paint.Align.LEFT：
        * Paint.Align.CENTER：
        * Paint.Align.RIGHT：
        * */
        textPaint.textAlign = Paint.Align.LEFT

        /*
        * Paint.setTextLocale：设置地域（不同地域显示文字不同）
        * Locale.CHINA：简体中文
        * Locale.TAIWAN：繁体中文
        * */
        textPaint.textLocale = Locale.CHINA

        /*
        * 矢量字体：矢量字体的原理是对每个字体给出一个字形的矢量描述，然后使用这一个矢量来对所有的尺寸的字体来生成对应的字形。
        * 由于不必为所有字号都设计它们的字体形状，所以在字号较大的时候，矢量字体也能够保持字体的圆润，这是矢量字体的优势。
        *
        * 现在几乎不会用到
        * */
        textPaint.hinting = Paint.HINTING_ON

        canvas.save()
        canvas.translate(0f, 100f)
        staticLayout.draw(canvas)
        canvas.restore()
    }

    companion object{
        private const val TEXT = "today is sunday"
        private const val LONG_TEXT = "today is sunday \noh i forget it \nsorry, next time, i must remember it"
    }
}