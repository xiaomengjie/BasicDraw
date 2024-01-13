package com.xiao.today.basicdraw.draw.drawsequence

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.FontMetrics
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.xiao.today.basicdraw.R

class HighlightTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {

    private var startCharIndex = -1

    private var endCharIndex = -1

    private val rectFPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val fontMetrics = FontMetrics()

    private val measuredWidth = floatArrayOf(0f)

    private var startCharLineCount: Int = 0
    private var endCharLineCount: Int = 0

    private val lineCountList = mutableListOf<Int>()
    private val lineWidthList = mutableListOf<Float>()

    var startChar: Char? = null
        set(value) {
            if (text != null && value != null){
                startCharIndex = text.indexOf(value)
            }
            field = value
        }

    var endChar: Char? = null
        set(value) {
            if (text != null && value != null){
                endCharIndex = text.indexOf(value)
            }
            field = value
        }

    override fun setText(text: CharSequence, type: BufferType) {
        super.setText(text, type)
        startChar?.let {
            startCharIndex = text.indexOf(it)
        }
        endChar?.let {
            endCharIndex = text.indexOf(it)
        }
    }

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.HighlightTextView)
        array.getString(R.styleable.HighlightTextView_start)?.let {
            startChar = it.toCharArray()[0]
        }
        array.getString(R.styleable.HighlightTextView_end)?.let {
            endChar = it.toCharArray()[0]
        }
        array.getColor(R.styleable.HighlightTextView_color, Color.RED).let {
            rectFPaint.color = it
        }
        array.recycle()
        paint.getFontMetrics(fontMetrics)
    }

    override fun onDraw(canvas: Canvas) {
        if (startCharIndex == -1 || endCharIndex == -1 || startCharIndex > endCharIndex) {
            super.onDraw(canvas)
            return
        }
        var start = 0
        while (start < text.length){
            val textLineCount = paint.breakText(
                text, start, text.length, true, width.toFloat(), measuredWidth
            )
            start += textLineCount
            lineCountList.add(textLineCount)
            lineWidthList.add(measuredWidth[0])
        }
        var index = 0
        var startCharLineStartIndex = 0
        var endCharLineStartIndex = 0
        lineCountList.forEachIndexed { i, lineCount ->
            index += lineCount
            if (startCharIndex <= index && startCharIndex >= (index - lineCount)){
                startCharLineCount = i
                startCharLineStartIndex = index - lineCount
            }
            if (endCharIndex <= index && endCharIndex >= (index - lineCount)){
                endCharLineCount = i
                endCharLineStartIndex = index - lineCount
            }
        }

        val spacing = (fontMetrics.bottom - fontMetrics.top) + (fontMetrics.ascent - fontMetrics.top)
        if (startCharLineCount == endCharLineCount){
            //同一行
            val y = baseline + startCharLineCount * spacing
            val left = paint.measureText(text, startCharLineStartIndex, startCharIndex)
            val right = paint.measureText(text, startCharLineStartIndex, endCharIndex + 1)
            canvas.drawRect(
                left, y + fontMetrics.top, right, y + fontMetrics.bottom, rectFPaint
            )
        }else{
            //不同行
            for (lineCount in startCharLineCount..endCharLineCount) {
                val y = baseline + lineCount * spacing
                when (lineCount) {
                    startCharLineCount -> {
                        val left = paint.measureText(text, startCharLineStartIndex, startCharIndex)
                        canvas.drawRect(
                            left,
                            y + fontMetrics.top,
                            lineWidthList[lineCount],
                            y + fontMetrics.bottom,
                            rectFPaint
                        )
                    }
                    endCharLineCount -> {
                        val right = paint.measureText(text, endCharLineStartIndex, endCharIndex + 1)
                        canvas.drawRect(
                            0f,
                            y + fontMetrics.top,
                            right,
                            y + fontMetrics.bottom,
                            rectFPaint
                        )
                    }
                    else -> {
                        canvas.drawRect(
                            0f,
                            y + fontMetrics.top,
                            lineWidthList[lineCount],
                            y + fontMetrics.bottom,
                            rectFPaint
                        )
                    }
                }
            }
        }
        super.onDraw(canvas)
    }
}