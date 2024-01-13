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

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.HighlightTextView)
        array.getString(R.styleable.HighlightTextView_start)?.let {
            val char = it.toCharArray()[0]
            startCharIndex = text.indexOf(char)
        }
        array.getString(R.styleable.HighlightTextView_end)?.let {
            val char = it.toCharArray()[0]
            endCharIndex = text.indexOf(char)
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
        var left: Float = -1f
        var right: Float = -1f
        while (start < endCharIndex + 1){
            val lineTextCount =
                paint.breakText(text, start, endCharIndex + 1, true, width.toFloat(), measuredWidth)
            if (start + lineTextCount < startCharIndex + 1){
                startCharLineCount++
            }else{
                if (left < 0){
                    paint.breakText(text, start, startCharIndex, true, width.toFloat(), measuredWidth)
                    left = measuredWidth[0]
                }
            }
            if (start + lineTextCount < endCharIndex + 1){
                endCharLineCount++
            }else{
                paint.breakText(text, start, endCharIndex + 1, true, width.toFloat(), measuredWidth)
                right = measuredWidth[0]
            }
            start += lineTextCount
        }
        println(startCharLineCount)
        println(endCharLineCount)
        println(left)
        println(right)

//        //整个文本的长度
//        val measureTextWidth = paint.measureText(text, 0, text.length)
//        if (measureTextWidth > width){ //多行文本
//            val lineIndex = paint.getOffsetForAdvance(
//                text, 0, text.length, 0, text.length, false, width.toFloat()
//            )
//            val startLine = startCharIndex / lineIndex
//            val endLine = endCharIndex / lineIndex
//            val lineWidth = paint.getRunAdvance(
//                text, 0, text.length, 0, text.length, false, lineIndex
//            )
//            val startCharAdvance = paint.getRunAdvance(
//                text, 0, text.length, 0, text.length, false, startCharIndex
//            )
//            val endCharAdvance = paint.getRunAdvance(
//                text, 0, text.length, 0, text.length, false, endCharIndex + 1
//            )
//            val spacing = (fontMetrics.bottom - fontMetrics.top) + (fontMetrics.ascent - fontMetrics.top)
//            val left = startCharAdvance - lineWidth * startLine
//            val right = endCharAdvance - lineWidth * endLine
//            if (startLine == endLine){
//                canvas.drawRect(left, baseline + fontMetrics.top + spacing * startLine, right, baseline + fontMetrics.bottom + spacing * startLine, rectFPaint)
//            }
//        }else{
//            //单行文本
//            val startCharAdvance = paint.getRunAdvance(
//                text, 0, text.length, 0, text.length, false, startCharIndex
//            )
//            val endCharAdvance = paint.getRunAdvance(
//                text, 0, text.length, 0, text.length, false, endCharIndex + 1
//            )
//            canvas.drawRect(startCharAdvance, baseline + fontMetrics.top, endCharAdvance, baseline + fontMetrics.bottom, rectFPaint)
//        }


//        var start = 0
//        var outsideStartChar = false
//        while (start < endCharIndex + 1){
//            val breakText = paint.breakText(text, start, endCharIndex + 1, true, width.toFloat(), measuredWidth)
//            start += breakText
//            endIndexList.add(start to measuredWidth[0])
//            if (!outsideStartChar){
//                if (start > startCharIndex){
//                    val length = paint.breakText(
//                        text, start - breakText, startCharIndex + 1, true, width.toFloat(), measuredWidth
//                    )
//                    startIndexList.add(start - breakText + length - 1 to measuredWidth[0])
//                    outsideStartChar = true
//                }else{
//                    startIndexList.add(start - 1 to measuredWidth[0])
//                }
//            }
//        }
//        val left =  if (startIndexList.size == 1){
//            paint.measureText(text, 0, startIndexList[0].first)
//        }else{
//            paint.measureText(text,
//                startIndexList[startIndexList.lastIndex - 1].first + 1,
//                startIndexList[startIndexList.lastIndex].first
//            )
//        }
//        val right =  if (endIndexList.size == 1){
//            paint.measureText(text, 0, endIndexList[0].first + 1)
//        }else{
//            paint.measureText(text,
//                endIndexList[endIndexList.lastIndex - 1].first + 1,
//                endIndexList[endIndexList.lastIndex].first + 1
//            )
//        }
//        val spacing = (fontMetrics.bottom - fontMetrics.top) + (fontMetrics.ascent - fontMetrics.top)
//        if (startIndexList.size == endIndexList.size){
//            val lineY = baseline + startIndexList.lastIndex * spacing
//            canvas.drawRect(left, lineY + fontMetrics.top, right, lineY + fontMetrics.bottom, rectFPaint)
//        }else{
//            val startY = baseline + startIndexList.lastIndex * spacing
//            canvas.drawRect(left, startY + fontMetrics.top, endIndexList[startIndexList.lastIndex].second, startY + fontMetrics.bottom, rectFPaint)
//            val endY = baseline + endIndexList.lastIndex * spacing
//            canvas.drawRect(0f, endY + fontMetrics.ascent, right, endY + fontMetrics.descent, rectFPaint)
//            val value = endIndexList.size - startIndexList.size
//            if (value > 1){
//                for (i in 1 .. value) {
//                    val y = startY + spacing * i
//                    canvas.drawRect(0f, y + fontMetrics.top, endIndexList[startIndexList.lastIndex + i].second, y + fontMetrics.bottom, rectFPaint)
//                }
//            }
//        }
        super.onDraw(canvas)
    }
}