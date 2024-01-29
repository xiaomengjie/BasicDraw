package com.xiao.today.basicdraw.draw

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.FontMetrics
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener

class MaterialEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs){

    private val labelTextSize = textSize / 2

    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = labelTextSize
        color = Color.RED
    }

    private val fontMetrics: FontMetrics = paint.fontMetrics

    private var paddingTop: Int = (fontMetrics.descent - fontMetrics.ascent).toInt()

    private var percent: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    private val percentAnimator =
        ObjectAnimator.ofFloat(this, "percent", 0f, 1f)


    private var initFinish = false

    init {
        initFinish = true
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        // TODO: onTextChanged在其父类TextView的构造函数中会调用setText，继而调用onTextChanged。
        // TODO: 导致percentAnimator没有初始化。增加构造执行完成的判断
        if (initFinish){
            if (text.isNullOrEmpty()){
                percentAnimator.reverse()
            }else{
                if (percent == 0f) percentAnimator.start()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        setPadding(0, (paddingTop * percent).toInt(), 0, 0)
        if (hint.isNotBlank()){
            labelPaint.alpha = (percent * 0xFF).toInt()
            canvas.drawText(hint.toString(), 0f, -fontMetrics.ascent, labelPaint)
        }
    }
}