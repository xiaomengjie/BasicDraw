package com.xiao.today.basicdraw.layout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.xiao.today.basicdraw.dp2px
import kotlin.math.max

class FlowLayout(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 100f.dp2px
        alpha = (0xff * 0.5).toInt()
        textAlign = Paint.Align.CENTER
    }

    var flowAdapter: FlowAdapter<out Any>? = null
        set(value) {
            field = value
            value?.let {
                for (i in 0 until it.count()) {
                    val view = it.onCreateView(i, this)
                    addView(view)
                }
            }
        }

    private val viewBounds = mutableMapOf<View, Rect>()

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val adapter = flowAdapter
        if (adapter == null){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }else{
            var widthUsed = 0
            var heightUsed = 0
            var viewLineMaxHeight = 0
            var viewLineMaxWidth = 0
            for (i in 0 until childCount) {
                val view = getChildAt(i)
                if (i >= viewBounds.size){
                    viewBounds[view] = Rect()
                }
                val rect = viewBounds[view]
                measureChild(view, widthMeasureSpec, heightMeasureSpec)
                if (widthUsed + view.measuredWidth > width){
                    //需要换行显示
                    viewLineMaxWidth = max(viewLineMaxWidth, widthUsed)
                    widthUsed = view.measuredWidth
                    heightUsed += viewLineMaxHeight
                    viewLineMaxHeight = view.measuredHeight
                }else{
                    widthUsed += view.measuredWidth
                    viewLineMaxHeight = max(view.measuredHeight, viewLineMaxHeight)
                }
                rect?.let {
                    it.left = widthUsed - view.measuredWidth
                    it.top = heightUsed
                    it.right = widthUsed
                    it.bottom = heightUsed + view.measuredHeight
                }
            }
            viewLineMaxWidth = max(viewLineMaxWidth, widthUsed)
            heightUsed += viewLineMaxHeight
            setMeasuredDimension(viewLineMaxWidth, heightUsed)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        viewBounds.forEach {
            it.key.layout(it.value.left, it.value.top, it.value.right, it.value.bottom)
        }
    }

    override fun onDrawForeground(canvas: Canvas) {
        super.onDrawForeground(canvas)
        canvas.drawText(childCount.toString(), width / 2f, height / 2f, textPaint)
    }
}